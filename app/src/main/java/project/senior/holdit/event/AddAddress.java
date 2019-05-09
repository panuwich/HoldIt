package project.senior.holdit.event;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import project.senior.holdit.R;
import project.senior.holdit.manager.SharedPrefManager;
import project.senior.holdit.model.ResponseModel;
import project.senior.holdit.retrofit.ApiInterface;
import project.senior.holdit.retrofit.ConnectServer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAddress extends AppCompatActivity {
    TextView textViewProvince;
    TextView textViewDistrict;
    TextView textViewPostCode;
    EditText editTextName;
    EditText editTextTel;
    EditText editTextAddr;
    SwitchCompat switchCompat;
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

        textViewProvince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddAddress.this,ShowAddrList.class);
                intent.putExtra("requestCode", 1);
                startActivityForResult(intent,1);
            }
        });

        textViewDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(textViewProvince.getText().toString().isEmpty()){
                    Toast.makeText(AddAddress.this, "กรุณาเลือกจังหวัด", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(AddAddress.this, ShowAddrList.class);
                    intent.putExtra("province", textViewProvince.getText().toString());
                    intent.putExtra("requestCode", 2);
                    startActivityForResult(intent, 2);
                }
            }
        });

        textViewPostCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(textViewProvince.getText().toString().isEmpty() || textViewDistrict.getText().toString().isEmpty()){
                    Toast.makeText(AddAddress.this, "กรุณาเลือกจังหวัดและเขต/อำเภอ", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(AddAddress.this,ShowAddrList.class);
                    intent.putExtra("district", textViewDistrict.getText().toString());
                    intent.putExtra("requestCode", 3);
                    startActivityForResult(intent,3);
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valid();
            }
        });
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
        int user_id = SharedPrefManager.getInstance(AddAddress.this).getUser().getUserId();

        Call<ResponseModel> call = apiService.addaddress(name, user_id, postcode, province, district, address, tel, addr_default);
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
}
