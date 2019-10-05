package project.senior.holdit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import project.senior.holdit.enumuration.OrderStatusEnum;
import project.senior.holdit.info.AddressSelect;
import project.senior.holdit.manager.SharedPrefManager;
import project.senior.holdit.model.Address;
import project.senior.holdit.model.Item;
import project.senior.holdit.model.ResponseModel;
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
    TextView textViewItemName;
    TextView textViewTotal;
    int addrId;
    FirebaseUser fuser;
    DatabaseReference reference;

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
        Button buttonContract = (Button) findViewById(R.id.button_contract);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        setAddr();
        setToolbar();

        textViewItemName = (TextView) findViewById(R.id.textView_ordering_item_name);
        TextView textViewItemPrice = (TextView) findViewById(R.id.textView_ordering_item_price);
        TextView textViewItemNum = (TextView) findViewById(R.id.textView_ordering_item_num);
        TextView textViewItemPre = (TextView) findViewById(R.id.textView_ordering_item_pre_rate);
        TextView textViewItemTran = (TextView) findViewById(R.id.textView_ordering_item_tran_rate);
        textViewTotal = (TextView) findViewById(R.id.textView_ordering_total);
        ImageView imageViewItem = (ImageView) findViewById(R.id.imageView_ordering_item);

        Intent i = getIntent();
        final Item item = (Item) i.getSerializableExtra("item");
        final int num = Integer.parseInt(i.getStringExtra("num"));
        final int total = Integer.parseInt(i.getStringExtra("total"));

        textViewItemName.setText(item.getItemName());
        textViewItemPrice.setText("฿" + item.getItemPrice());
        textViewItemNum.setText("x" + num);

        String url = "http://pilot.cp.su.ac.th/usr/u07580319/holdit/pics/item/" + item.getItemImg1();
        Picasso.get().load(url).into(imageViewItem);
        ;

        textViewItemPre.setText("฿" + (Integer.parseInt(item.getItemPreRate()) * num));
        textViewItemTran.setText("฿" + (Integer.parseInt(item.getItemTranRate()) * num));
        textViewTotal.setText("฿" + total);

        layoutAddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Ordering.this, AddressSelect.class);
                intent.putExtra("requestCode", 100);
                startActivityForResult(intent, 100);
            }
        });

        buttonContract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addrId == 0) {
                    Toast.makeText(Ordering.this, "กรุณาเลือกที่อยู่จัดส่ง", Toast.LENGTH_SHORT).show();
                } else {
                    String seller = item.getUserId();
                    String buyer = SharedPrefManager.getInstance(Ordering.this).getUser().getUserId();
                    int addr = addrId;
                    Date d = new Date();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String date = dateFormat.format(d);
                    createOrder(seller, buyer, item, addr, num, total, date);
                }
            }


        });
    }

    private void createOrder(final String seller, String buyer
            , final Item item, int addr, final int num, final int total, String date) {
        final ApiInterface apiService = ConnectServer.getClient().create(ApiInterface.class);
        Call<ResponseModel> call = apiService.createorder(seller, buyer, item.getItemId(), addr, num, total, date, OrderStatusEnum.WAIT_FOR_ACCEPT);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                Toast.makeText(Ordering.this, response.body().getResponse(), Toast.LENGTH_SHORT).show();
                if (response.body().isStatus()) {
                    String[] res = response.body().getResponse().split(" ");
                    String orderId = res[1];
                    sendMessage(seller, item, total, num, orderId);
                    Intent intent = new Intent(Ordering.this, MainActivity.class);
                    intent.putExtra("order", true);
                    startActivity(intent);
                    finishAffinity();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {

            }
        });
    }

    public void sendMessage(String seller, Item item, int total, int num, String orderId) {

        String sender = fuser.getUid();
        final String receiver = seller;
        String message = "สั่งซื้อ " + item.getItemName() + "\nจำนวน " + num + " ชิ้น\n" + "ราคารวม " + total + " บาท";

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("isseen", false);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        hashMap.put("time", formatter.format(date));

        reference.child("Chats").child(orderId).push().setValue(hashMap);

    }

    public void setAddr() {
        final ApiInterface apiService = ConnectServer.getClient().create(ApiInterface.class);
        String user_id = SharedPrefManager.getInstance(Ordering.this).getUser().getUserId();
        Call<Address> call = apiService.readdefaultaddress(user_id);
        call.enqueue(new Callback<Address>() {
                         @Override
                         public void onResponse(Call<Address> call, Response<Address> response) {
                             Address res = response.body();
                             addrId = res.getId();
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
            if (resultCode == Activity.RESULT_OK) {
                Address addr = (Address) data.getSerializableExtra("addr");
                addrId = addr.getId();
                textViewAddrName.setText(addr.getName());
                textViewAddrTel.setText(addr.getTel());
                textViewAddrAddr.setText(addr.getAddress());
                textViewAddrDistPro.setText(addr.getDistrict() + " " + addr.getProvince());
                textViewAddrPostcode.setText(addr.getPostcode());
            }
        } else if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

            }
        }
    }

    public void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_ordering);
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
