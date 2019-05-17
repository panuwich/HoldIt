package project.senior.holdit.info;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import project.senior.holdit.R;
import project.senior.holdit.dialog.AlertDialogService;
import project.senior.holdit.manager.SharedPrefManager;
import project.senior.holdit.model.Address;
import project.senior.holdit.model.ResponseModel;
import project.senior.holdit.retrofit.ApiInterface;
import project.senior.holdit.retrofit.ConnectServer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAddress extends AppCompatActivity implements View.OnClickListener{
    TextView textViewProvince;
    TextView textViewDistrict;
    TextView textViewPostCode;
    EditText editTextName;
    EditText editTextTel;
    EditText editTextAddr;
    SwitchCompat switchCompat;
    int id = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        setToolbar();
        textViewProvince = (TextView) findViewById(R.id.textView_add_addr_province);
        textViewDistrict = (TextView) findViewById(R.id.textView_add_addr_district);
        textViewPostCode = (TextView) findViewById(R.id.textView_add_addr_postcode);
        editTextName = (EditText)findViewById(R.id.editText_add_addr_name);
        editTextTel = (EditText)findViewById(R.id.editText_add_addr_tel);
        editTextAddr = (EditText)findViewById(R.id.editText_add_addr_addr);
        switchCompat = (SwitchCompat)findViewById(R.id.switch_add_addr);
        Button button = (Button)findViewById(R.id.button_addr_add);
        View view = (View)findViewById(R.id.view_add_addr_del);
        LinearLayout layoutDel = (LinearLayout)findViewById(R.id.layout_add_addr_del);

        Intent intent = getIntent();
        int req = intent.getIntExtra("requestCode",-1);
        if(req != -1){
            Address addr = (Address) intent.getSerializableExtra("addr");
            id = addr.getId();
            setDetail(addr);
            view.setVisibility(View.VISIBLE);
            layoutDel.setVisibility(View.VISIBLE);
            layoutDel.setOnClickListener(this);
        }
        textViewProvince.setOnClickListener(this);
        textViewDistrict.setOnClickListener(this);
        textViewPostCode.setOnClickListener(this);
        button.setOnClickListener(this);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String result = data.getStringExtra("result");
                textViewProvince.setText(result);
                textViewDistrict.setText("");
                textViewPostCode.setText("");
            }
        }else if (requestCode == 2) {
            if(resultCode == Activity.RESULT_OK){
                String result = data.getStringExtra("result");
                String postcode = data.getStringExtra("postcode");
                textViewDistrict.setText(result);
                textViewPostCode.setText(postcode);
            }
        }else if (requestCode == 3) {
            if(resultCode == Activity.RESULT_OK){
                String result = data.getStringExtra("result");
                textViewPostCode.setText(result);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.textView_add_addr_province:
                provinceClick();
                break;
            case R.id.textView_add_addr_postcode:
                postcodeClick();
                break;
            case R.id.textView_add_addr_district:
                districtClick();
                break;
            case R.id.button_addr_add:
                validClick();
                break;
            case R.id.layout_add_addr_del:
                delClick();
                break;

        }
    }

    public void delClick(){
        AlertDialog.Builder builder =
                new AlertDialog.Builder(AddAddress.this);
        builder.setTitle("ต้องการลบที่อยู่จัดส่ง?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                delAddress();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog.dismiss();
            }
        });
        builder.show();
    }

    public void validClick(){
        valid();
    }
    public void provinceClick(){
        Intent intent = new Intent(AddAddress.this,ShowAddrList.class);
        intent.putExtra("requestCode", 1);
        startActivityForResult(intent,1);
    }
    public void districtClick(){
        if(textViewProvince.getText().toString().isEmpty()){
            Toast.makeText(AddAddress.this, "กรุณาเลือกจังหวัด", Toast.LENGTH_SHORT).show();
        }else {
            Intent intent = new Intent(AddAddress.this, ShowAddrList.class);
            intent.putExtra("province", textViewProvince.getText().toString());
            intent.putExtra("requestCode", 2);
            startActivityForResult(intent, 2);
        }
    }
    public void postcodeClick(){
        if(textViewProvince.getText().toString().isEmpty() || textViewDistrict.getText().toString().isEmpty()){
            Toast.makeText(AddAddress.this, "กรุณาเลือกจังหวัดและเขต/อำเภอ", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent(AddAddress.this,ShowAddrList.class);
            intent.putExtra("district", textViewDistrict.getText().toString());
            intent.putExtra("requestCode", 3);
            startActivityForResult(intent,3);
        }
    }

    public void valid(){

        String name = editTextName.getText().toString();
        String postcode = textViewPostCode.getText().toString();
        String province = textViewProvince.getText().toString();
        String district = textViewDistrict.getText().toString();

        String address = editTextAddr.getText().toString();
        String tel = editTextTel.getText().toString();
        int addr_default = 0;
        if(switchCompat.isChecked()){
            addr_default = 1;
        }

        if(name.isEmpty() || textViewPostCode.getText().toString().isEmpty() || province.isEmpty() || district.isEmpty() ||
                address.isEmpty() || tel.isEmpty() || postcode.isEmpty()){
            Toast.makeText(this, "กรุณากรอกข้อมูลให้ครบถ้วน", Toast.LENGTH_SHORT).show();

        }else{
            addAddress(name, Integer.parseInt(postcode), province, district, address, tel, addr_default);
        }

    }

    public void addAddress(String name,int postcode,String province,String district,String address,String tel,int addr_default) {
        final ApiInterface apiService = ConnectServer.getClient().create(ApiInterface.class);
        String user_id = SharedPrefManager.getInstance(AddAddress.this).getUser().getUserId();

        Call<ResponseModel> call = apiService.addaddress(id, name, user_id, postcode, province, district, address, tel, addr_default);
        call.enqueue(new Callback<ResponseModel>() {
                         @Override
                         public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                            ResponseModel res = response.body();
                            Toast.makeText(AddAddress.this, res.getResponse(), Toast.LENGTH_SHORT).show();
                            if(res.isStatus()){
                                finish();
                            }
                         }

                         @Override
                         public void onFailure(Call<ResponseModel> call, Throwable t) {
                         }
                     }

        );

    }

    public void delAddress() {
        final ApiInterface apiService = ConnectServer.getClient().create(ApiInterface.class);
        Call<ResponseModel> call = apiService.deladdress(id);
        call.enqueue(new Callback<ResponseModel>() {
                         @Override
                         public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                             ResponseModel res = response.body();
                             Toast.makeText(AddAddress.this, res.getResponse(), Toast.LENGTH_SHORT).show();
                             if(res.isStatus()){
                                 finish();
                             }
                         }

                         @Override
                         public void onFailure(Call<ResponseModel> call, Throwable t) {
                         }
                     }

        );

    }

    public void setToolbar(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_addr_add);
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

    public void setDetail(Address addr) {
         textViewProvince.setText(addr.getProvince());
         textViewDistrict.setText(addr.getDistrict());
         textViewPostCode.setText(addr.getPostcode());
         editTextName.setText(addr.getName());
         editTextTel.setText(addr.getTel());
         editTextAddr.setText(addr.getAddress());
         if(addr.getAddrDefault() == 0){
             switchCompat.setChecked(false);
         }else{
             switchCompat.setChecked(true);
         }

    }

    @Override
    public void onBackPressed() {
        new AlertDialogService(AddAddress.this).showDialogSimple("ละทิ้ง?");


    }
}
