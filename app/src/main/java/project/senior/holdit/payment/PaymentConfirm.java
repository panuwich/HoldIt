package project.senior.holdit.payment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;

import project.senior.holdit.R;
import project.senior.holdit.manager.SharedPrefManager;
import project.senior.holdit.model.Order;

public class PaymentConfirm extends AppCompatActivity {
    Order order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_confirm);
        final String bank = getIntent().getStringExtra("bank");
        ImageView imageView = (ImageView)findViewById(R.id.imageView_payment_con_tran2);
        Picasso.get().load("https://www.asiancasinotop10.com/th/wp-content/uploads/sites/2/2017/09/kbank-icon.png").into(imageView);
        setLogo(bank);
        Button btn = (Button)findViewById(R.id.btn_payment_confirm);
        String fname = SharedPrefManager.getInstance(PaymentConfirm.this).getUser().getUserFirstname();
        String lname = SharedPrefManager.getInstance(PaymentConfirm.this).getUser().getUserLastname();
        String name = fname + " " + lname;
        TextView textViewName = (TextView)findViewById(R.id.textView_payment_name);
        textViewName.setText(name);
        order = (Order)getIntent().getSerializableExtra("order");
        TextView textViewId = findViewById(R.id.textView_payment_id);
        TextView textViewPrice = findViewById(R.id.textView_payment_price);
        textViewId.setText(getId());
        textViewPrice.setText("" + order.getTotal() +".00 บาท");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentConfirm.this,PaymentComplete.class);
                intent.putExtra("bank",bank);
                intent.putExtra("order", (Serializable) order);
                startActivity(intent);
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
        ImageView imageView = (ImageView)findViewById(R.id.imageView_payment_con_tran);
        if(bank.equals("ktb")){
            linearLayout.setBackgroundColor(Color.parseColor("#07A5E7"));
            Picasso.get().load("https://is2-ssl.mzstatic.com/image/thumb/Purple124/v4/b6/e4/e6/b6e4e69f-746f-34c8-89e8-ea910aafabfd/AppIcon-0-1x_U007emarketing-0-0-sRGB-85-220-0-10.png/246x0w.jpg").into(imageView);
        }else if(bank.equals("scb")){
            linearLayout.setBackgroundColor(Color.parseColor("#4E2A81"));
            Picasso.get().load("https://www.diamondgrains.com/images/SCB.png").into(imageView);
        }else if(bank.equals("ks")){
            linearLayout.setBackgroundColor(Color.parseColor("#705F5F"));
            Picasso.get().load("https://is4-ssl.mzstatic.com/image/thumb/Purple113/v4/14/42/08/144208c0-5fab-499c-7052-52d8c7510d1f/AppIcons-0-1x_U007emarketing-0-85-220-3.png/246x0w.jpg").into(imageView);
        }else if(bank.equals("ksk")){
            linearLayout.setBackgroundColor(Color.parseColor("#019A3F"));
            Picasso.get().load("https://www.asiancasinotop10.com/th/wp-content/uploads/sites/2/2017/09/kbank-icon.png").into(imageView);
        }else if(bank.equals("tnc")){
            linearLayout.setBackgroundColor(Color.parseColor("#F36F21"));
            Picasso.get().load("https://is3-ssl.mzstatic.com/image/thumb/Purple123/v4/f7/92/24/f792245f-5148-9974-c307-8d6bf466b86d/source/512x512bb.jpg").into(imageView);
        }else if(bank.equals("tmb")){
            linearLayout.setBackgroundColor(Color.parseColor("#007EC3"));
            Picasso.get().load("https://pbs.twimg.com/profile_images/936198120873930753/iyEeh0ga_400x400.jpg").into(imageView);
        }else{
            linearLayout.setBackgroundColor(Color.parseColor("#EC068D"));
            Picasso.get().load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSD0YK1qMWMsmiyw3uEHmv0ZLrD0yJGdUTrLDp5BxYRKlA3yqjq").into(imageView);
        }
    }
}
