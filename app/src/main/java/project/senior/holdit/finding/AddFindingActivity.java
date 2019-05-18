package project.senior.holdit.finding;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import project.senior.holdit.R;
import project.senior.holdit.info.AddressSelect;
import project.senior.holdit.manager.SharedPrefManager;
import project.senior.holdit.model.Address;
import project.senior.holdit.model.ResponseModel;
import project.senior.holdit.model.User;
import project.senior.holdit.retrofit.ApiInterface;
import project.senior.holdit.retrofit.ConnectServer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFindingActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView imageView;
    String SELECT_IMAGE = null;
    int SELECT_IMAGE_REQ = 1001;
    int ADDR = 0 ;
    int REQ_ADDR = 100;
    EditText editTextName;
    EditText editTextDesc;
    EditText editTextLocation;
    TextView textViewAddrName;
    TextView textViewAddrTel;
    TextView textViewAddrAddr;
    TextView textViewAddrDistPro;
    TextView textViewAddrPostcode;
    TextView textViewAmount;
    Button buttonDe;
    Button buttonIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_finding);
        setToolbar();
        setAddr();
        LinearLayout layoutAddr = (LinearLayout) findViewById(R.id.layout_addr_annouce);
        textViewAddrName = (TextView) findViewById(R.id.textView_annouce_name);
        textViewAddrTel = (TextView) findViewById(R.id.textView_annouce_tel);
        textViewAddrAddr = (TextView) findViewById(R.id.textView_annouce_addr);
        textViewAddrDistPro = (TextView) findViewById(R.id.textView_annouce_dist_pro);
        textViewAddrPostcode = (TextView) findViewById(R.id.textView_annouce_postcode);
        imageView = findViewById(R.id.imageView_annouce);
        editTextName = findViewById(R.id.editText_annouce_item_name);
        editTextDesc = findViewById(R.id.editText_annouce_item_desc);
        editTextLocation = findViewById(R.id.editText_annouce_item_location);

        final TextView textViewCountName = findViewById(R.id.editText_annouce_count_name);
        final TextView textViewCountDesc = findViewById(R.id.editText_annouce_count_desc);
        final TextView textViewCountLocation = findViewById(R.id.editText_annouce_count_location);
        textViewAmount = findViewById(R.id.textView_annouce_num);

        Button buttonAnnouce = findViewById(R.id.button_annouce);
        buttonIn = findViewById(R.id.button_annouce_increase);
        buttonDe = findViewById(R.id.button_annouce_decrease);

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

        editTextLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                textViewCountLocation.setText("" + editable.toString().length());
            }
        });

        RelativeLayout relativeLayout = findViewById(R.id.layout_image);
        relativeLayout.setOnClickListener(this);
        layoutAddr.setOnClickListener(this);
        buttonAnnouce.setOnClickListener(this);
        buttonIn.setOnClickListener(this);
        buttonDe.setOnClickListener(this);
    }

    private void addFinding() {
        User user = SharedPrefManager.getInstance(AddFindingActivity.this).getUser();
        ApiInterface api = ConnectServer.getClient().create(ApiInterface.class);
        Call<ResponseModel>call = api.createfinding(user.getUserId(),editTextName.getText().toString(),editTextDesc.getText().toString()
                ,editTextLocation.getText().toString(),Integer.parseInt(textViewAmount.getText().toString()), SELECT_IMAGE
                ,ADDR);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                Toast.makeText(AddFindingActivity.this, response.body().getResponse(), Toast.LENGTH_SHORT).show();
                if(response.body().isStatus()){
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {

            }
        });
    }

    private boolean isValidInput() {
        if (editTextName.getText().toString().isEmpty()) {
            Toast.makeText(this, "กรุณาใส่ชื่อ", Toast.LENGTH_SHORT).show();
            return false;
        } else if (SELECT_IMAGE == null) {
            Toast.makeText(this, "กรุณาอัพโหลดรูป", Toast.LENGTH_SHORT).show();
            return false;
        } else if(ADDR == 0){
            Toast.makeText(this, "กรุณาเลือกที่อยู่จัดส่ง", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void decrease(){
        if (!textViewAmount.getText().toString().equals("1")) {
            int numOfitem = Integer.parseInt(textViewAmount.getText().toString()) - 1;
            textViewAmount.setText("" + numOfitem);
            if (textViewAmount.getText().toString().equals("1")) {
                buttonDe.setEnabled(false);
                buttonDe.setBackgroundResource(R.drawable.ic_decrease_disable);
            }

        }
    }
    private void increase(){
        int numOfitem = Integer.parseInt(textViewAmount.getText().toString()) + 1;
        buttonDe.setEnabled(true);
        buttonDe.setBackgroundResource(R.drawable.ic_decrease);
        textViewAmount.setText("" + numOfitem);
    }
    void selectImage(int req) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select Image from Gallery"), req);
    }

    public String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_IMAGE_REQ) {
                if (data != null) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                    SELECT_IMAGE = bitmapToString(BitmapFactory.decodeFile(picturePath));
                }
            }else if (requestCode == REQ_ADDR){
                Address addr = (Address) data.getSerializableExtra("addr");
                ADDR = addr.getId();
                textViewAddrName.setText(addr.getName());
                textViewAddrTel.setText(addr.getTel());
                textViewAddrAddr.setText(addr.getAddress());
                textViewAddrDistPro.setText(addr.getDistrict() + " " + addr.getProvince());
                textViewAddrPostcode.setText(addr.getPostcode());
            }
        } else {
            Toast.makeText(AddFindingActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
        }
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

    public void setAddr() {
        final ApiInterface apiService = ConnectServer.getClient().create(ApiInterface.class);
        String user_id = SharedPrefManager.getInstance(AddFindingActivity.this).getUser().getUserId();
        Call<Address> call = apiService.readdefaultaddress(user_id);
        call.enqueue(new Callback<Address>() {
                         @Override
                         public void onResponse(Call<Address> call, Response<Address> response) {
                             Address res = response.body();
                             ADDR = res.getId();
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_annouce:
                if (isValidInput()){
                    addFinding();
                }
                break;
            case R.id.button_annouce_increase:
                increase();
                break;
            case R.id.button_annouce_decrease:
                decrease();
                break;
            case R.id.layout_image:
                selectImage(SELECT_IMAGE_REQ);
                break;
            case R.id.layout_addr_annouce:
                Intent intent = new Intent(AddFindingActivity.this, AddressSelect.class);
                intent.putExtra("requestCode", REQ_ADDR);
                startActivityForResult(intent, REQ_ADDR);
                break;
        }
    }
}
