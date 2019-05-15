package project.senior.holdit.payment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

import project.senior.holdit.R;
import project.senior.holdit.manager.SharedPrefManager;
import project.senior.holdit.model.Order;

public class PaymentLogin extends AppCompatActivity {
    Order order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_login);
        final String bank = getIntent().getStringExtra("bank");
        setLogo(bank);
        final EditText editTextUsername = findViewById(R.id.editText_payment_username);
        TextView textViewId = findViewById(R.id.textView_payment_id);
        TextView textViewPrice = findViewById(R.id.textView_payment_price);
        order = (Order)getIntent().getSerializableExtra("order");
        textViewId.setText(getId());
        textViewPrice.setText("" + order.getTotal() +".00 บาท");
        Button button = findViewById(R.id.btn_payment_login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = editTextUsername.getText().toString();
                String email = SharedPrefManager.getInstance(PaymentLogin.this).getUser().getUserEmail();
                String usernameCheck = email.substring(0,email.indexOf("@"));
                if(username.equals(usernameCheck)) {
                    Intent intent = new Intent(PaymentLogin.this, PaymentConfirm.class);
                    intent.putExtra("bank", bank);
                    intent.putExtra("order", (Serializable) order);
                    startActivity(intent);
                }else{
                    Toast.makeText(PaymentLogin.this, "กรุณากรอก Username และ Password ให้ถูกต้อง", Toast.LENGTH_SHORT).show();
                }
            }
        });

        
        
    }
    private String getId(){
        String orderId = ""+order.getId();
        String num="";
        for(int i = 0 ; i < 10-orderId.length() ; i++){
            num+= "0";
        }
        num +=orderId;
        return num;
    }
    private void setLogo(String bank){
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.layout_payment);
        ImageView imageView = (ImageView)findViewById(R.id.imageView_payment_login);
        if(bank.equals("ktb")){
            linearLayout.setBackgroundColor(Color.parseColor("#07A5E7"));
            imageView.setImageResource(R.drawable.ktb);
        }else if(bank.equals("scb")){
            linearLayout.setBackgroundColor(Color.parseColor("#4E2A81"));
            imageView.setImageResource(R.drawable.scb);
        }else if(bank.equals("ks")){
            linearLayout.setBackgroundColor(Color.parseColor("#705F5F"));
            imageView.setImageResource(R.drawable.ks);
        }else if(bank.equals("ksk")){
            linearLayout.setBackgroundColor(Color.parseColor("#019A3F"));
            imageView.setImageResource(R.drawable.kbank);
        }else if(bank.equals("tnc")){
            linearLayout.setBackgroundColor(Color.parseColor("#F36F21"));
            imageView.setImageResource(R.drawable.tnc);
        }else if(bank.equals("tmb")){
            linearLayout.setBackgroundColor(Color.parseColor("#007EC3"));
            imageView.setImageResource(R.drawable.tmb);
        }else{
            linearLayout.setBackgroundColor(Color.parseColor("#EC068D"));
            imageView.setImageResource(R.drawable.omsin);
        }
    }
}
