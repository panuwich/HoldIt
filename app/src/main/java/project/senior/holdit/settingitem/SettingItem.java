package project.senior.holdit.settingitem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import project.senior.holdit.R;
import project.senior.holdit.dialog.AlertDialogService;
import project.senior.holdit.manager.SharedPrefManager;
import project.senior.holdit.model.Item;
import project.senior.holdit.model.ResponseModel;
import project.senior.holdit.retrofit.ApiInterface;
import project.senior.holdit.retrofit.ConnectServer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingItem extends AppCompatActivity implements View.OnClickListener {
    EditText editTextName, editTextDesc, editTextItemPrice, editTextItemPreRate, editTextItemTranRate;
    TextView textViewCountName, textViewCountDesc;
    ImageView buttonPic1, buttonPic2, buttonPic3;
    CardView cardView2, cardView3;
    SwitchCompat switchCompat;
    Button button;
    String userId;
    ArrayList<String> imgAr = new ArrayList<>();
    Item item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_item);

        setToolbar();
        item = (Item) getIntent().getSerializableExtra("item");
        userId = SharedPrefManager.getInstance(SettingItem.this).getUser().getUserId();

        buttonPic1 = (ImageView) findViewById(R.id.button_create_picture1);
        switchCompat = (SwitchCompat) findViewById(R.id.switch_setting);
        buttonPic2 = (ImageView) findViewById(R.id.button_create_picture2);
        buttonPic3 = (ImageView) findViewById(R.id.button_create_picture3);
        cardView2 = (CardView) findViewById(R.id.cardView_create_picture2);
        cardView3 = (CardView) findViewById(R.id.cardView_create_picture3);
        editTextName = (EditText) findViewById(R.id.editText_create_item_name);
        editTextDesc = (EditText) findViewById(R.id.editText_create_item_desc);
        editTextItemPreRate = (EditText) findViewById(R.id.editText_create_item_pre_rate);
        editTextItemTranRate = (EditText) findViewById(R.id.editText_create_item_pre_tran_rate);
        editTextItemPrice = (EditText) findViewById(R.id.editText_create_item_price);
        textViewCountName = (TextView) findViewById(R.id.editText_create_count_name);
        textViewCountDesc = (TextView) findViewById(R.id.editText_create_count_desc);
        button = (Button) findViewById(R.id.button_create);

        editTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                textViewCountName.setText("" + editable.toString().length());
            }
        });
        editTextDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                textViewCountDesc.setText("" + editable.toString().length());
            }
        });
        editTextItemPrice.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                setEditText(editTextItemPrice,editable.toString(),this);
            }
        });
        editTextItemPreRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                setEditText(editTextItemPreRate,editable.toString(),this);
            }
        });
        editTextItemTranRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                setEditText(editTextItemTranRate,editable.toString(),this);
            }
        });
        button.setOnClickListener(this);
        setItem();

    }
    void setItem(){
        editTextName.setText(item.getItemName());
        editTextDesc.setText(item.getItemDesc());
        editTextItemPrice.setText(""+item.getItemPrice());
        editTextItemPreRate.setText(""+item.getItemPreRate());
        editTextItemTranRate.setText(""+item.getItemTranRate());
        String url = "http://pilot.cp.su.ac.th/usr/u07580319/holdit/pics/item/";
        if(item.getStatus() == 1){
            switchCompat.setChecked(true);
        }else{
            switchCompat.setChecked(false);
        }
        if (!item.getItemImg1().isEmpty()){
            imgAr.add(item.getItemImg1());
            Picasso.get().load(url+item.getItemImg1()).into(buttonPic1);
        }
        if (!item.getItemImg2().isEmpty()){
            imgAr.add(item.getItemImg2());
            Picasso.get().load(url+item.getItemImg2()).into(buttonPic2);
            cardView2.setVisibility(View.VISIBLE);
        }
        if (!item.getItemImg3().isEmpty()){
            imgAr.add(item.getItemImg3());
            Picasso.get().load(url+item.getItemImg3()).into(buttonPic3);
            cardView3.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_create);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_create:
                //action
                saveItem();
                break;
        }
    }

    void saveItem(){
        String itemName = editTextName.getText().toString().trim();
        String price = editTextItemPrice.getText().toString().replace(",","");
        String prerate = editTextItemPreRate.getText().toString().replace(",","");
        String tranrate = editTextItemTranRate.getText().toString().replace(",","");
        String itemDesc = editTextDesc.getText().toString().trim();
        if (itemName.isEmpty() || price.isEmpty() || prerate.isEmpty() || tranrate.isEmpty() ){
            Toast.makeText(this, "กรุณากรอกข้อมูลให้ครบถ้วน", Toast.LENGTH_SHORT).show();
        }else {
            int itemPrice = Integer.parseInt(price);
            int itemPreRate = Integer.parseInt(prerate);
            int itemTranRate = Integer.parseInt(tranrate);
            int status = 0;
            if (switchCompat.isChecked()){
                status = 1;
            }
            updateItem(itemName,  itemPrice,  itemPreRate,  itemTranRate,  itemDesc,status);
        }
    }
    public void updateItem(String itemName, int itemPrice, int itemPreRate, int itemTranRate, String itemDesc, int status){
        final ApiInterface apiService = ConnectServer.getClient().create(ApiInterface.class);
        Call<ResponseModel> call = apiService.updateitem(item.getItemId(),  itemName, itemPrice, itemPreRate, itemTranRate
                , itemDesc,status);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                ResponseModel res = response.body();
                Toast.makeText(SettingItem.this, res.getResponse(), Toast.LENGTH_SHORT).show();
                if(res.getStatus()){
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(SettingItem.this, "fail", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        new AlertDialogService(SettingItem.this).showDialogSimple("ละทิ้ง?");
    }
}
