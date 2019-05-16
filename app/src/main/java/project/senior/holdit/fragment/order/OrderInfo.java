package project.senior.holdit.fragment.order;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import project.senior.holdit.R;
import project.senior.holdit.model.Item;
import project.senior.holdit.model.Order;
import project.senior.holdit.retrofit.ApiInterface;
import project.senior.holdit.retrofit.ConnectServer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderInfo extends AppCompatActivity {
    Item item;
    TextView textViewId;
    TextView textViewName;
    TextView textViewPrice;
    TextView textViewPre;
    TextView textViewTran;
    TextView textViewTotal;
    TextView textViewTrack;
    ImageView imageView;
    ImageView imageViewCilpboard;
    Order order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);
        setToolbar();
        order = (Order)getIntent().getSerializableExtra("order");
        int itemId = order.getItemId();
        getItem(itemId);
        getOrder(order.getId());
         textViewId = (TextView)findViewById(R.id.textView_order_detail_id);
         textViewName = (TextView)findViewById(R.id.textView_order_detail_name);
         textViewPrice = (TextView)findViewById(R.id.textView_order_detail_price);
         textViewPre =(TextView) findViewById(R.id.textView_order_detail_pre_rate);
         textViewTran = (TextView)findViewById(R.id.textView_order_detail_tran_rate);
         textViewTotal = (TextView)findViewById(R.id.textView_order_detail_total);
         textViewTrack = (TextView)findViewById(R.id.textView_order_detail_track);
         imageView = (ImageView)findViewById(R.id.imageView_order_detail);
         imageViewCilpboard = (ImageView)findViewById(R.id.clipboard);



         imageViewCilpboard.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 setClipboard(OrderInfo.this,textViewTrack.getText().toString());
             }
         });

    }

    private void getItem(int itemId){
        final ApiInterface apiService = ConnectServer.getClient().create(ApiInterface.class);
        Call<Item> call = apiService.getitem(itemId);
        call.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                item = response.body();
                textViewId.setText(""+order.getId());
                textViewName.setText(item.getItemName());
                textViewPrice.setText("฿ "+item.getItemPrice()+ " x " + order.getAmount());
                textViewPre.setText("฿ "+item.getItemPreRate() + " x "+ order.getAmount());
                textViewTran.setText("฿ "+item.getItemTranRate()+ " x " + order.getAmount());
                textViewTotal.setText("฿ "+order.getTotal());

                String url = "http://pilot.cp.su.ac.th/usr/u07580319/holdit/pics/item/" + item.getItemImg1();
                Picasso.get().load(url).into(imageView);
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {

            }
        });

    }

    private void getOrder(int orderId){
        final ApiInterface apiService = ConnectServer.getClient().create(ApiInterface.class);
        Call<Order> call = apiService.getorder(orderId);
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                textViewTrack.setText(response.body().getTrack());
            }
            @Override
            public void onFailure(Call<Order> call, Throwable t) {

            }
        });
    }
    public void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_order_detail);
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

    private void setClipboard(Context context, String text) {
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
        Toast.makeText(context, "Copied Text", Toast.LENGTH_SHORT).show();
    }
}
