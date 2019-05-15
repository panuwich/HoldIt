package project.senior.holdit.payment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import project.senior.holdit.MainActivity;
import project.senior.holdit.R;
import project.senior.holdit.manager.SharedPrefManager;
import project.senior.holdit.model.Order;
import project.senior.holdit.model.ResponseModel;
import project.senior.holdit.retrofit.ApiInterface;
import project.senior.holdit.retrofit.ConnectServer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentComplete extends AppCompatActivity {
    Order order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_complete);
        String bank = getIntent().getStringExtra("bank");
        setLogo(bank);
        TextView textViewDate = (TextView)findViewById(R.id.textView_payment_complete);
        Date d = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = dateFormat.format(d);
        String fname = SharedPrefManager.getInstance(PaymentComplete.this).getUser().getUserFirstname();
        String lname = SharedPrefManager.getInstance(PaymentComplete.this).getUser().getUserLastname();
        String name = fname + " " + lname;
        TextView textViewName = (TextView)findViewById(R.id.textView_payment_name);
        textViewName.setText(name);
        textViewDate.setText(date);
        order = (Order)getIntent().getSerializableExtra("order");
        TextView textViewId = findViewById(R.id.textView_payment_id);
        TextView textViewPrice = findViewById(R.id.textView_payment_price);
        textViewId.setText(getId());
        textViewPrice.setText("" + order.getTotal() +".00 บาท");
        Button btn = findViewById(R.id.btn_payment_complete);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pay();
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
    private void pay(){
        final ApiInterface apiService = ConnectServer.getClient().create(ApiInterface.class);
        Call<ResponseModel> call = apiService.pay(order.getId());
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                Toast.makeText(PaymentComplete.this, response.body().getResponse(), Toast.LENGTH_SHORT).show();
                if(response.body().getStatus()){
                    Intent intent = new Intent(PaymentComplete.this, MainActivity.class);
                    intent.putExtra("order",true);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {

            }
        });
    }
    private void setLogo(String bank){

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.layout_payment);
        if(bank.equals("ktb")){
            linearLayout.setBackgroundColor(Color.parseColor("#07A5E7"));
        }else if(bank.equals("scb")){
            linearLayout.setBackgroundColor(Color.parseColor("#4E2A81"));
        }else if(bank.equals("ks")){
            linearLayout.setBackgroundColor(Color.parseColor("#705F5F"));
        }else if(bank.equals("ksk")){
            linearLayout.setBackgroundColor(Color.parseColor("#019A3F"));
        }else if(bank.equals("tnc")){
            linearLayout.setBackgroundColor(Color.parseColor("#F36F21"));
        }else if(bank.equals("tmb")){
            linearLayout.setBackgroundColor(Color.parseColor("#007EC3"));
        }else{
            linearLayout.setBackgroundColor(Color.parseColor("#EC068D"));
        }
    }
}
