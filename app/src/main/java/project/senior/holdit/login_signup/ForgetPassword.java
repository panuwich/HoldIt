package project.senior.holdit.login_signup;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import project.senior.holdit.R;
import project.senior.holdit.model.ResponseModel;
import project.senior.holdit.retrofit.ApiInterface;
import project.senior.holdit.retrofit.ConnectServer;
import project.senior.holdit.sendemail.GMailSender;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPassword extends AppCompatActivity {
    EditText editTextEmail, etRecipient;
    Button btnSend;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        editTextEmail = (EditText) findViewById(R.id.editText_forget_password);
        btnSend = (Button) findViewById(R.id.button_forget_password);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEmailValid(editTextEmail.getText().toString())){
                    sendEmail(editTextEmail.getText().toString().toLowerCase());
                }else{
                    Toast.makeText(ForgetPassword.this, "กรุณาใส่ email ให้ถูกต้อง", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    public void sendMessage(final String email) {
        dialog = new ProgressDialog(ForgetPassword.this);
        dialog.setTitle("Sending Email");
        dialog.setMessage("Please wait");
        dialog.show();
        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GMailSender sender = new GMailSender();
                    String enEmail = encryptEmail(email);
                    String url = " Follow the link below to set a new password: \n" +
                            "http://pilot.cp.su.ac.th/usr/u07580319/holdit/forgetpassword/formpassword.php?email="+enEmail+
                            "\n If you don't wish to reset your password, disregard this email and no action will be taken.\n" +
                            "\n" +
                            "HOLDIT Team";
                    sender.sendMail(email,url,"Reset HOLDIT Password");

                } catch (Exception e) {
                    Log.e("mylog", "Error: " + e.getMessage());
                }
            }
        });
        sender.start();
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
    public void sendEmail(final String email) {

        final ApiInterface apiService = ConnectServer.getClient().create(ApiInterface.class);
        Call<ResponseModel> call = apiService.checkemail(email);
        call.enqueue(new Callback<ResponseModel>() {
                         @Override
                         public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                             ResponseModel res = response.body();
                             Toast.makeText(ForgetPassword.this, res.getResponse(), Toast.LENGTH_SHORT).show();
                             if(res.isStatus()){
                                 sendMessage(email.toLowerCase());
                             }
                             dialog.dismiss();
                         }

                         @Override
                         public void onFailure(Call<ResponseModel> call, Throwable t) {
                             Toast.makeText(ForgetPassword.this, "ตรวจ email อีกครั้ง", Toast.LENGTH_SHORT).show();
                             dialog.dismiss();
                         }
                     }

        );

    }
    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
