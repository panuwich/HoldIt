package project.senior.holdit.login_signup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import project.senior.holdit.MainActivity;
import project.senior.holdit.R;
import project.senior.holdit.manager.SharedPrefManager;
import project.senior.holdit.model.User;
import project.senior.holdit.retrofit.ApiInterface;
import project.senior.holdit.retrofit.ConnectServer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    CallbackManager callbackManager;
    RelativeLayout layout_face;
    LoginButton loginButtonFace;
    ProgressDialog dialog;
    Button loginButtonNormal;
    TextView textViewSignup,textViewLogin;
    EditText editTextEmail, editTextPassword;
    private static final String MY_PREFS = "prefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        callbackManager = CallbackManager.Factory.create();
        editTextEmail = (EditText)findViewById(R.id.editText_login_email);
        editTextPassword = (EditText)findViewById(R.id.editText_login_password);
        textViewLogin = (TextView)findViewById(R.id.textView_forget_password);
        textViewSignup = (TextView)findViewById(R.id.textView_login_signup);
        loginButtonNormal = (Button)findViewById(R.id.button_login_normal);
        loginButtonFace = (LoginButton) findViewById(R.id.button_login_face);
        loginButtonFace.setReadPermissions("editTextEmail");

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,ForgetPassword.class));
            }
        });
        // If using in a fragment
        //loginButtonFace.setFragment();

        //checkHashKey();
        setFacebookButton();
        layout_face = (RelativeLayout) findViewById(R.id.layout_face);
        layout_face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButtonFace.performClick();
            }
        });
        loginButtonNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email  = editTextEmail.getText().toString().toLowerCase();
                String password = editTextPassword.getText().toString();
                if(email.isEmpty() || password.isEmpty()){
                    Toast.makeText(Login.this, "กรุณาใส่ email และ password", Toast.LENGTH_SHORT).show();
                }else {
                    dialog = new ProgressDialog(Login.this,R.style.MyAlertDialogStyle);
                    dialog.setMessage("Loading");
                    dialog.show();
                    login(email, password);
                }
            }
        });

        textViewSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,Signup.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    void setFacebookButton() {
        // Callback registration
        loginButtonFace.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
               /* Toast.makeText(Login.this, "Success" + loginResult.getAccessToken().getUserId(), Toast.LENGTH_SHORT).show();

                Bundle paramrters = new Bundle();
                paramrters.putString("fields", "id,name,last_name,link,editTextEmail,picture");
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                        Log.e("LOGIN",jsonObject.toString());
                        try {
                            String url = jsonObject.getJSONObject("picture").getJSONObject("data").getString("url");
                            Log.e("LOGIN",jsonObject.getJSONObject("picture").getJSONObject("data").getString("url"));
                            //Picasso.get().load(url).into(img);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                request.setParameters(paramrters);
                request.executeAsync();
*/
                SharedPreferences shared = getSharedPreferences(MY_PREFS,
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = shared.edit();
                editor.putString("editTextEmail", "test");
                editor.putBoolean("booleanKey", true);
                editor.commit();
                startActivity(new Intent(Login.this, MainActivity.class));
                finish();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });


    }
    public void login(String email, String password) {

        final ApiInterface apiService = ConnectServer.getClient().create(ApiInterface.class);
        Call<User> call = apiService.login(email , password);
        call.enqueue(new Callback<User>() {
                         @Override
                         public void onResponse(Call<User> call, Response<User> response) {
                             User user = response.body();
                             if(user != null){
                                 if(user.getUserStatusLogin() == 0){
                                     Toast.makeText(Login.this, "กรุณาตรวจสอบอีเมลเพื่อใช้งานแอปพลิเคชัน", Toast.LENGTH_SHORT).show();
                                 }else {
                                     SharedPrefManager.getInstance(Login.this).saveUser(user);

                                     Toast.makeText(Login.this, "เข้าสู่ระบบเสร็จสิ้น", Toast.LENGTH_SHORT).show();
                                     startActivity(new Intent(Login.this, MainActivity.class));
                                     finish();
                                 }
                             }else{
                                 Toast.makeText(Login.this, "ตรวจ email และ password อีกครั้ง", Toast.LENGTH_SHORT).show();
                             }
                             dialog.dismiss();

                         }

                         @Override
                         public void onFailure(Call<User> call, Throwable t) {
                             Toast.makeText(Login.this, "ตรวจ email และ password อีกครั้ง", Toast.LENGTH_SHORT).show();
                             dialog.dismiss();
                         }
                     }

        );

    }
    void checkHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "project.senior.holdit", PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                Log.d("KeyHash: ", Base64.encodeToString(messageDigest.digest(),
                        Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }
}
