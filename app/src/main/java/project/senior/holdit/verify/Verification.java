package project.senior.holdit.verify;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import project.senior.holdit.R;

public class Verification extends AppCompatActivity {
    private static final String MY_PREFS = "prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        CardView cardViewPassport = (CardView)findViewById(R.id.cardView_verification_passport);
        CardView cardViewIDCard = (CardView)findViewById(R.id.cardView_verification_id_card);
        TextView textViewStatus = (TextView)findViewById(R.id.textView_verification_status);

        SharedPreferences shared = getSharedPreferences(MY_PREFS,
                Context.MODE_PRIVATE);
        final int status = shared.getInt("user_status_verified", 0);
        System.out.println("stauts : " + status);
        if(status == 1){
            textViewStatus.setText("ยืนยันตัวตนแล้ว");
            textViewStatus.setTextColor(ContextCompat.getColor(this, R.color.colorGreen));

        }else{
            textViewStatus.setText("ยังไม่ยืนยันตัวตน");
            textViewStatus.setTextColor(ContextCompat.getColor(this, R.color.colorRedOp));
        }

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
