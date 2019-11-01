package project.senior.holdit.finding;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import project.senior.holdit.MainActivity;
import project.senior.holdit.R;
import project.senior.holdit.enumuration.OrderStatusEnum;
import project.senior.holdit.manager.SharedPrefManager;
import project.senior.holdit.model.Finding;
import project.senior.holdit.model.Item;
import project.senior.holdit.model.ResponseModel;
import project.senior.holdit.retrofit.ApiInterface;
import project.senior.holdit.retrofit.ConnectServer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreOrder extends AppCompatActivity {
    Finding finding;
    EditText editTextPrice;
    EditText editTextPre;
    EditText editTextTran;
    FirebaseUser fuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_order);
        setToolbar();
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        ImageView imageView = findViewById(R.id.imageView_annouce);
        TextView textViewName = findViewById(R.id.textView_preorder_name);
        TextView textViewDesc = findViewById(R.id.textView_preorder_desc);
        TextView textViewAmount = findViewById(R.id.textView_preorder_amount);
        TextView textViewLocation = findViewById(R.id.textView_preorder_location);
        editTextPrice  = findViewById(R.id.editText_price);
        editTextPre  = findViewById(R.id.editText_pre);
        editTextTran  = findViewById(R.id.editText_tran);
        Button btn = findViewById(R.id.button_pre_order);
        Intent i = getIntent();
        finding = (Finding) i.getSerializableExtra("finding");
        String url = "http://pilot.cp.su.ac.th/usr/u07580319/holdit/pics/item/" + finding.getImage();
        Picasso.get().load(url).into(imageView);
        textViewName.setText(finding.getName());
        if (finding.getDescript().isEmpty()){
            textViewDesc.setText("-");
        }else{
            textViewDesc.setText(finding.getDescript());
        }
        textViewAmount.setText(""+finding.getAmount());
        if (finding.getLocation().isEmpty()){
            textViewLocation.setText("-");
        }else{
            textViewLocation.setText(finding.getLocation());
        }
        editTextPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                setEditText(editTextPrice,editable.toString(),this);
            }
        });
        editTextPre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                setEditText(editTextPre,editable.toString(),this);
            }
        });
        editTextTran.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                setEditText(editTextTran,editable.toString(),this);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validInput()){
                    int price = Integer.parseInt(editTextPrice.getText().toString().replace(",",""));
                    int pre = Integer.parseInt(editTextPre.getText().toString().replace(",",""));
                    int tran = Integer.parseInt(editTextTran.getText().toString().replace(",",""));
                   createItem(price,pre,tran);
                }else{
                    Toast.makeText(PreOrder.this, getResources().getString(R.string.toast_input_not_completely), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    boolean validInput(){
        if (editTextPrice.getText().toString().isEmpty() || editTextPre.getText().toString().isEmpty()
                || editTextTran.getText().toString().isEmpty()){
            return false;
        }
        return true;
    }
    public void createItem(int itemPrice, int itemPreRate, int itemTranRate){
        final ApiInterface apiService = ConnectServer.getClient().create(ApiInterface.class);


        Call<Item> call = apiService.createitemfind(finding.getId(),finding.getUserId()
                , finding.getName(), itemPrice, itemPreRate, itemTranRate
                , finding.getDescript(), finding.getImage());
        call.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                Item res = response.body();
                if(res != null){
                    int price = Integer.parseInt(res.getItemPrice())*finding.getAmount();
                    int pre = Integer.parseInt(res.getItemPreRate())*finding.getAmount();
                    int tran = Integer.parseInt(res.getItemTranRate())*finding.getAmount();
                    int total = price+pre+tran;
                    Date d = new Date();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String date = dateFormat.format(d);
                    createOrder(SharedPrefManager.getInstance(PreOrder.this).getUser().getUserId(), finding.getUserId(),res,finding.getAddrId()
                    ,finding.getAmount(),total,date);
                }

            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {

            }
        });

    }
    private void createOrder(final String seller,final String buyer
            , final Item item, int addr, final int num, final int total, String date) {
        final ApiInterface apiService = ConnectServer.getClient().create(ApiInterface.class);
        Call<ResponseModel> call = apiService.createorder(seller, buyer, item.getItemId(), addr, num, total, date, OrderStatusEnum.WAIT_FOR_PAYMENT);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                Toast.makeText(PreOrder.this, response.body().getResponse(), Toast.LENGTH_SHORT).show();
                if (response.body().isStatus()) {
                    String[] res = response.body().getResponse().split(" ");
                    String orderId = res[1];
                    sendMessage(seller,buyer, item, total, num, orderId);
                    Intent intent = new Intent(PreOrder.this, MainActivity.class);
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


    public void sendMessage(String seller , String buyer, Item item, int total, int num, String orderId) {

        String sender = seller;
        final String receiver = buyer;
        String message = "รับซื้อ " + item.getItemName() + "\nจำนวน " + num + " ชิ้น\n" + "ราคารวม " + total + " บาท";

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

    private void setEditText(EditText editText, String str,TextWatcher textWatcher){
        editText.removeTextChangedListener(textWatcher);
        if (!str.isEmpty()){
            String format = String.format("%,d", Integer.parseInt(str.replace(",","")));
            editText.setText(format);
            editText.setSelection(editText.getText().toString().length());
        }else{
            editText.setSelection(0);
        }

        editText.addTextChangedListener(textWatcher);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_finding);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
    }

}
