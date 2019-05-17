package project.senior.holdit.verify;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import project.senior.holdit.R;
import project.senior.holdit.manager.SharedPrefManager;
import project.senior.holdit.model.User;
import project.senior.holdit.retrofit.ApiInterface;
import project.senior.holdit.retrofit.ConnectServer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Verification extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        CardView cardViewPassport = (CardView)findViewById(R.id.cardView_verification_passport);
        CardView cardViewIDCard = (CardView)findViewById(R.id.cardView_verification_id_card);
        final TextView textViewStatus = (TextView)findViewById(R.id.textView_verification_status);

        final User user  = SharedPrefManager.getInstance(Verification.this).getUser();
        ApiInterface api = ConnectServer.getClient().create(ApiInterface.class);
        Call<User> call = api.getverified(user.getUserId());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.body().getUserStatusVerified() == 1){
                    textViewStatus.setText("ยืนยันตัวตนแล้ว");
                    textViewStatus.setTextColor(ContextCompat.getColor(Verification.this, R.color.colorGreen));
                }else{
                    textViewStatus.setText("ยังไม่ยืนยันตัวตน");
                    textViewStatus.setTextColor(ContextCompat.getColor(Verification.this, R.color.colorRedOp));
                }
                user.setUserStatusVerified(response.body().getUserStatusVerified());
                SharedPrefManager.getInstance(Verification.this).saveUser(user);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });


        cardViewPassport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Verification.this,VerificationPassport.class));
            }
        });
        cardViewIDCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Verification.this,VerificationIDCard.class));
            }
        });
    }
}
