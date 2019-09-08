package project.senior.holdit.settingitem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

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
    Spinner spinner;
    int SELECT_TYPE = 0;
    private ArrayList<String> typeList = new ArrayList<String>();
    HashMap<String,Integer> hashMap = new HashMap<>();
    Button button;
    String userId;
    ArrayList<String> imgAr = new ArrayList<>();
    Item item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_item);
        createTypeData();
        setToolbar();
        setHashMap();
        item = (Item) getIntent().getSerializableExtra("item");
        userId = SharedPrefManager.getInstance(SettingItem.this).getUser().getUserId();
        spinner = findViewById(R.id.spinner_type);
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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.detail_dropdown_type, typeList);
        adapter.setDropDownViewResource(R.layout.detail_dropdown_type);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SELECT_TYPE = (int) hashMap.get(typeList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
        spinner.setSelection(item.getType()-1);
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
    private void createTypeData() {
        typeList.add("เสื้อผ้า");
        typeList.add("หนังสือ");
        typeList.add("รองเท้า");
        typeList.add("เครื่องสำอาง");
        typeList.add("กระเป๋า");
        typeList.add("เครื่องประดับ");
        typeList.add("กีฬา");
        typeList.add("อุปกรณ์ถ่ายภาพ");
        typeList.add("สื่อบันเทิง");
        typeList.add("อุปกรณ์เครื่องเขียน");
        typeList.add("อุปกรณ์ IT");
        typeList.add("อื่นๆ");
    }
    public void setHashMap() {
        hashMap.put("เลือกประเภท", 0);
        hashMap.put("เสื้อผ้า", 1);
        hashMap.put("หนังสือ", 2);
        hashMap.put("รองเท้า", 3);
        hashMap.put("เครื่องสำอาง", 4);
        hashMap.put("กระเป๋า", 5);
        hashMap.put("เครื่องประดับ", 6);
        hashMap.put("กีฬา", 7);
        hashMap.put("อุปกรณ์ถ่ายภาพ", 8);
        hashMap.put("สื่อบันเทิง", 9);
        hashMap.put("อุปกรณ์เครื่องเขียน", 10);
        hashMap.put("อุปกรณ์ IT", 11);
        hashMap.put("อื่นๆ", 12);
    }

    void saveItem(){
        String itemName = editTextName.getText().toString().trim();
        String price = editTextItemPrice.getText().toString().replace(",","");
        String prerate = editTextItemPreRate.getText().toString().replace(",","");
        String tranrate = editTextItemTranRate.getText().toString().replace(",","");
        String itemDesc = editTextDesc.getText().toString().trim();
        if (itemName.isEmpty() || price.isEmpty() || prerate.isEmpty() || tranrate.isEmpty() ){
            Toast.makeText(this, getResources().getString(R.string.toast_input_not_completely), Toast.LENGTH_SHORT).show();
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
                , itemDesc,status,SELECT_TYPE);
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
