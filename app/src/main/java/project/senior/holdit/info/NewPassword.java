package project.senior.holdit.info;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import project.senior.holdit.R;
import project.senior.holdit.manager.SharedPrefManager;
import project.senior.holdit.model.ResponseModel;
import project.senior.holdit.model.User;
import project.senior.holdit.retrofit.ApiInterface;
import project.senior.holdit.retrofit.ConnectServer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewPassword extends AppCompatActivity {
    String OldPassCheck = null;
    TextInputLayout inputOld;
    TextInputLayout inputNew;
    TextInputLayout inputReNew;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        inputOld = findViewById(R.id.editText_old);
        inputNew = findViewById(R.id.editText_new);
        inputReNew = findViewById(R.id.editText_renew);

        OldPassCheck = SharedPrefManager.getInstance(NewPassword.this).getUser().getUserPassword();
        inputOld.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                validOldPass();
            }
        });
        inputNew.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                isCheckPassword();
            }
        });
        inputReNew.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                isCheckConfirmPassword();
            }
        });

        Button btn = findViewById(R.id.button_update);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validOldPass() && isCheckPassword() && isCheckConfirmPassword() ){
                    if (MD5(inputNew.getEditText().getText().toString()).equals(OldPassCheck)){
                        Toast.makeText(NewPassword.this, "ไม่สามารถตั้งรหัสผ่านใหม่เหมือนรหัสเก่าได้", Toast.LENGTH_SHORT).show();
                    }else{
                        updatePass();
                    }

                }else{
                    Toast.makeText(NewPassword.this, getResources().getString(R.string.toast_invalid_input), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updatePass(){
        final User user = SharedPrefManager.getInstance(NewPassword.this).getUser();
        final ApiInterface apiService = ConnectServer.getClient().create(ApiInterface.class);
        Call<ResponseModel> call = apiService.updateuser(2, user.getUserId(), inputReNew.getEditText().getText().toString() , "");
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                Toast.makeText(NewPassword.this, response.body().getResponse(), Toast.LENGTH_SHORT).show();
                if (response.body().isStatus()){
                    user.setUserPassword(MD5(inputReNew.getEditText().getText().toString()));
                    SharedPrefManager.getInstance(NewPassword.this).saveUser(user);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {

            }
        });

    }

    private boolean validOldPass(){
        if(MD5(inputOld.getEditText().getText().toString()).equals(OldPassCheck)){
            inputOld.setError(null);
            return  true;
        }else{
            inputOld.setError("รหัสผ่านไม่ถูกต้อง");
            return false;
        }
    }
    public boolean isCheckPassword() {
        if (inputNew.getEditText().getText().toString().length() < 6 && !inputNew.getEditText().getText().toString().isEmpty()) {
            inputNew.setError("รหัสผ่านต้องมีอย่างน้อย 6 ตัว");
            return false;
        } else {
            inputNew.setError(null);
            return true;
        }
    }

    public boolean isCheckConfirmPassword() {
        if (!inputReNew.getEditText().getText().toString().equals(inputNew.getEditText().getText().toString())) {
            inputReNew.setError("รหัสผ่านไม่ถูกต้อง");
            return false;
        } else {
            inputReNew.setError(null);
            return true;
        }
    }


    private String MD5(String md5) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
        }
        return null;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_newpass);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
    }
}
