package project.senior.holdit.login_signup;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import project.senior.holdit.R;
import project.senior.holdit.model.ResponseModel;
import project.senior.holdit.retrofit.ApiInterface;
import project.senior.holdit.retrofit.ConnectServer;
import project.senior.holdit.sendemail.GMailSender;
import project.senior.holdit.util.ImageUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Signup extends AppCompatActivity {
    CheckBox checkBox;
    Button buttonSignup;
    int SELECT_IMAGE = 1001;
    int CROP_IMAGE = 2001;
    CircleImageView imgProfile;
    ProgressDialog dialog;
    private static final String MY_PREFS = "prefs";
    String image_profile = "";
    TextInputLayout editTextEmail;
    TextInputLayout editTextPassword;
    TextInputLayout editTextConfirmPassword;
    TextInputLayout editTextFirstname;
    TextInputLayout editTextLastname;
    TextInputLayout editTextId;
    TextInputLayout editTextTel;

    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        imgProfile = (CircleImageView) findViewById(R.id.circle_signup_profile);
        editTextEmail = (TextInputLayout) findViewById(R.id.editText_signup_email);
        editTextPassword = (TextInputLayout) findViewById(R.id.editText_signup_password);
        editTextConfirmPassword = (TextInputLayout) findViewById(R.id.editText_signup_confirm_password);
        editTextFirstname = (TextInputLayout) findViewById(R.id.editText_signup_firstname);
        editTextLastname = (TextInputLayout) findViewById(R.id.editText_signup_lastname);
        editTextId = (TextInputLayout) findViewById(R.id.editText_signup_id);
        editTextTel = (TextInputLayout) findViewById(R.id.editText_signup_tel);
        buttonSignup = (Button) findViewById(R.id.button_signup);
        checkBox = (CheckBox) findViewById(R.id.checkBox_signup);
        TextView textViewCondition = (TextView) findViewById(R.id.textView_signup_condition);
        textViewCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Signup.this, SignupCondition.class));
            }
        });
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, "Select Image from Gallery"), SELECT_IMAGE);
            }
        });
        editTextEmail.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                validateEmail();
                setSignUpButton();
            }
        });
        editTextPassword.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                isCheckPassword();
                setSignUpButton();
            }
        });
        editTextConfirmPassword.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                isCheckConfirmPassword();
                setSignUpButton();
            }
        });
        editTextFirstname.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty()) {
                    editTextFirstname.setError("กรุณากรอกชื่อ");
                } else {
                    editTextFirstname.setError(null);
                }
                setSignUpButton();
            }
        });
        editTextLastname.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty()) {
                    editTextLastname.setError("กรุณากรอกนามสกุล");
                } else {
                    editTextLastname.setError(null);
                }
                setSignUpButton();
            }
        });
        editTextId.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                isCitizenIDValid();
                setSignUpButton();
            }
        });
        editTextTel.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                isTelValid();
                setSignUpButton();
            }
        });
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSignUpButton();
            }
        });
        auth = FirebaseAuth.getInstance();
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new ProgressDialog(Signup.this);
                dialog.setMessage(getResources().getString(R.string.loading));
                dialog.show();
                //  if (checkCompleteInput()) {
                String email = editTextEmail.getEditText().getText().toString().toLowerCase();
                String password = editTextPassword.getEditText().getText().toString();
                String firstname = editTextFirstname.getEditText().getText().toString().toUpperCase();
                String lastname = editTextLastname.getEditText().getText().toString().toUpperCase();
                String tel = editTextTel.getEditText().getText().toString();
                String citizen = editTextId.getEditText().getText().toString();
                saveToFirebase(email, password, firstname, lastname, image_profile, citizen, tel);
               /* } else {
                    Toast.makeText(Signup.this, "Fail", Toast.LENGTH_SHORT).show();
                }*/
            }
        });
    }

    boolean checkCompleteInput() {
        return validateEmail() && isCheckPassword() && isCheckConfirmPassword() && editTextFirstname.getEditText().getText().toString().length() != 0
                && editTextLastname.getEditText().getText().toString().length() != 0 && isCitizenIDValid() && isTelValid() && checkBox.isChecked();
    }

    void setSignUpButton() {
        if (checkCompleteInput()) {
            buttonSignup.setEnabled(true);
        } else {
            buttonSignup.setEnabled(false);
        }
    }

    public boolean isCheckPassword() {
        if (editTextPassword.getEditText().getText().toString().length() < 6 && !editTextPassword.getEditText().getText().toString().isEmpty()) {
            editTextPassword.setError("รหัสผ่านต้องมีอย่างน้อย 6 ตัว");
            return false;
        } else {
            editTextPassword.setError(null);
            return true;
        }
    }

    public boolean isCheckConfirmPassword() {
        if (!editTextConfirmPassword.getEditText().getText().toString().equals(editTextPassword.getEditText().getText().toString())) {
            editTextConfirmPassword.setError("รหัสผ่านไม่ถูกต้อง");
            return false;
        } else {
            editTextConfirmPassword.setError(null);
            return true;
        }
    }

    public boolean isTelValid() {
        String tel = editTextTel.getEditText().getText().toString().trim();
        String expression = "\\d+";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(tel);
        return matcher.matches() && tel.length() == 10;
    }

    public boolean validateEmail() {

        String emailInput = editTextEmail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            editTextEmail.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            editTextEmail.setError("Please enter a valid email address");
            return false;
        } else {
            editTextEmail.setError(null);
            return true;
        }
    }

    public boolean isCitizenIDValid() {

        String id = editTextId.getEditText().getText().toString().trim();
        boolean check = false;
        if (id.length() != 13) {
            check = false;
        } else {
            int sum = 0;
            for (int i = 0; i < 12; i++) {
                sum += Integer.parseInt("" + Integer.parseInt("" + id.charAt(i)) * (13 - i));
            }
            sum = sum % 11;
            int ans = 11 - sum;
            check = String.valueOf(ans).equals(String.valueOf(id.charAt(12)));
        }
        if (check) {
            editTextId.setError(null);
        } else {
            editTextId.setError("กรุณาใส่รหัสบัตรประชาชนให้ถูกต้อง");
        }
        return check;
    }

    public void signUp(String uid , final String email, final String password, String firstname, String lastname, String image, String citizen, String tel) {

        final ApiInterface apiService = ConnectServer.getClient().create(ApiInterface.class);
        Call<ResponseModel> call = apiService.signup(uid, email, password, firstname, lastname, image, citizen, tel);
        call.enqueue(new Callback<ResponseModel>() {
                         @Override
                         public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                             ResponseModel res = response.body();
                             Toast.makeText(Signup.this, res.getResponse(), Toast.LENGTH_LONG).show();
                             if (res.getStatus()) {
                                 Thread sender = new Thread(new Runnable() {
                                     @Override
                                     public void run() {
                                         try {
                                             GMailSender sender = new GMailSender();
                                             String enEmail = encryptEmail(email);
                                             String url = " Follow the link below to verified email: \n" +
                                                     "http://pilot.cp.su.ac.th/usr/u07580319/holdit/verify/verifiedlogin.php?email=" + enEmail +
                                                     "\n" +
                                                     "\n" +
                                                     "HOLDIT Team";
                                             sender.sendMail(email, url, "Verified HOLDIT Email");

                                         } catch (Exception e) {
                                             Log.e("mylog", "Error: " + e.getMessage());
                                         }
                                     }
                                 });
                                 sender.start();

                                 Intent intent = new Intent(Signup.this, Login.class);
                                 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                 startActivity(intent);
                             }
                             dialog.dismiss();

                         }

                         @Override
                         public void onFailure(Call<ResponseModel> call, Throwable t) {
                             Toast.makeText(Signup.this, "Fail...", Toast.LENGTH_SHORT).show();
                             dialog.dismiss();
                         }
                     }

        );
    }

    public void saveToFirebase(final String email, final String password
            ,final String firstname,final String lastname, String image,final String citizen,final String tel) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            final String uid = firebaseUser.getUid();
                            reference = FirebaseDatabase.getInstance().getReference("Users").child(uid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("userId", uid);
                            hashMap.put("userFirstname", firstname);
                            hashMap.put("userImage", "default");
                            signUp(uid, email, password, firstname, lastname, image_profile, citizen, tel);
                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });


                        }
                    }
                });
    }

    public String encryptEmail(String email){
        String encrypt = "";
        for (int i = 0 ; i < email.length() ; i++){
            if(email.charAt(i)== '.'){
                encrypt += 'D';
            }else if(email.charAt(i) == '@'){
                encrypt += 'A';
            }else if(email.charAt(i)== '_'){
                encrypt += 'U';
            }else if(email.charAt(i) < 97 || email.charAt(i) >122){
                encrypt += email.charAt(i);
            }else{
                if(email.charAt(i)+i+1 >122){
                    encrypt += (char)(97+(email.charAt(i)+i)%122);
                }else{
                    encrypt += (char)(email.charAt(i)+i+1);
                }
            }
        }
        return  encrypt;
    }

    private void CropImage(Uri uri) {

        try {
            Intent CropIntent = new Intent("com.android.camera.action.CROP");
            CropIntent.setDataAndType(uri, "image/*");

            CropIntent.putExtra("crop", "true");
            CropIntent.putExtra("outputX", 150);
            CropIntent.putExtra("outputY", 150);
            CropIntent.putExtra("aspectX", 4);
            CropIntent.putExtra("aspectY", 4);
            CropIntent.putExtra("scaleUpIfNeeded", true);
            CropIntent.putExtra("return-data", true);

            startActivityForResult(CropIntent, CROP_IMAGE);
        } catch (ActivityNotFoundException ex) {

        }

    }

    public void clearInput() {
        image_profile = "";
        editTextEmail.getEditText().setText("");
        editTextPassword.getEditText().setText("");
        editTextConfirmPassword.getEditText().setText("");
        editTextFirstname.getEditText().setText("");
        editTextLastname.getEditText().setText("");
        editTextId.getEditText().setText("");
        editTextTel.getEditText().setText("");
        checkBox.setChecked(false);
        imgProfile.setImageResource(R.drawable.user);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_IMAGE) {
                if (data != null) {
                    CropImage(data.getData());
                }

            } else if (requestCode == CROP_IMAGE) {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = bundle.getParcelable("data");
                image_profile = ImageUtil.bitmapToString(bitmap);
                imgProfile.setImageBitmap(bitmap);
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(Signup.this, getResources().getString(R.string.cancel), Toast.LENGTH_SHORT).show();
        }
    }
}
