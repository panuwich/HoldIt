package project.senior.holdit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import project.senior.holdit.event.AddressSelect;
import project.senior.holdit.manager.SharedPrefManager;
import project.senior.holdit.model.Address;
import project.senior.holdit.model.Item;
import project.senior.holdit.retrofit.ApiInterface;
import project.senior.holdit.retrofit.ConnectServer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Ordering extends AppCompatActivity {
    TextView textViewAddrName;
    TextView textViewAddrTel;
    TextView textViewAddrAddr;
    TextView textViewAddrDistPro;
    TextView textViewAddrPostcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordering);
        LinearLayout layoutAddr = (LinearLayout) findViewById(R.id.layout_addr_ordering);
        textViewAddrName = (TextView) findViewById(R.id.textView_ordering_name);
        textViewAddrTel = (TextView) findViewById(R.id.textView_ordering_tel);
        textViewAddrAddr = (TextView) findViewById(R.id.textView_ordering_addr);
        textViewAddrDistPro = (TextView) findViewById(R.id.textView_ordering_dist_pro);
        textViewAddrPostcode = (TextView) findViewById(R.id.textView_ordering_postcode);
        setAddr();
        setToolbar();

        TextView textViewItemName = (TextView)findViewById(R.id.textView_ordering_item_name);
        TextView textViewItemPrice = (TextView)findViewById(R.id.textView_ordering_item_price);
        TextView textViewItemNum = (TextView)findViewById(R.id.textView_ordering_item_num);
        TextView textViewItemPre = (TextView)findViewById(R.id.textView_ordering_item_pre_rate);
        TextView textViewItemTran = (TextView)findViewById(R.id.textView_ordering_item_tran_rate);
        TextView textViewTotal = (TextView)findViewById(R.id.textView_ordering_total);
        ImageView imageViewItem = (ImageView)findViewById(R.id.imageView_ordering_item);

        Intent i = getIntent();
        Item item = (Item) i.getSerializableExtra("item");
        int num = Integer.parseInt(i.getStringExtra("num"));
        String total = i.getStringExtra("total");

        textViewItemName.setText(item.getItemName());
        textViewItemPrice.setText("฿" + item.getItemPrice());
        textViewItemNum.setText("x"+num);

        String url = "http://pilot.cp.su.ac.th/usr/u07580319/holdit/pics/item/"+item.getItemImg1();
        Picasso.get().load(url).into(imageViewItem);;

        textViewItemPre.setText("฿"+(Integer.parseInt(item.getItemPreRate()) * num));
        textViewItemTran.setText("฿"+(Integer.parseInt(item.getItemTranRate()) * num));
        textViewTotal.setText("฿"+total);

        layoutAddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(Ordering.this, AddressSelect.class);
                intent.putExtra("requestCode", 100);
                startActivityForResult(intent,100);
            }
        });
    }

    public void setAddr() {
        final ApiInterface apiService = ConnectServer.getClient().create(ApiInterface.class);
        int user_id = SharedPrefManager.getInstance(Ordering.this).getUser().getUserId();
        Call<Address> call = apiService.readdefaultaddress(user_id);
        call.enqueue(new Callback<Address>() {
                         @Override
                         public void onResponse(Call<Address> call, Response<Address> response) {
                             Address res = response.body();
                             textViewAddrName.setText(res.getName());
                             textViewAddrTel.setText(res.getTel());
                             textViewAddrAddr.setText(res.getAddress());
                             textViewAddrDistPro.setText(res.getDistrict() + " " + res.getProvince());
                             textViewAddrPostcode.setText(res.getPostcode());
                         }

                         @Override
                         public void onFailure(Call<Address> call, Throwable t) {
                         }
                     }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if(resultCode == Activity.RESULT_OK){
                Address addr = (Address) data.getSerializableExtra("addr");
                textViewAddrName.setText(addr.getName());
                textViewAddrTel.setText(addr.getTel());
                textViewAddrAddr.setText(addr.getAddress());
                textViewAddrDistPro.setText(addr.getDistrict() + " " + addr.getProvince());
                textViewAddrPostcode.setText(addr.getPostcode());
            }
        }else if (requestCode == 2) {
            if(resultCode == Activity.RESULT_OK){

            }
        }
    }

    public void setToolbar(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_ordering);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
