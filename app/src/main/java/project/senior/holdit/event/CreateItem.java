package project.senior.holdit.event;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import project.senior.holdit.R;
import project.senior.holdit.manager.SharedPrefManager;
import project.senior.holdit.model.ResponseModel;
import project.senior.holdit.retrofit.ApiInterface;
import project.senior.holdit.retrofit.ConnectServer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateItem extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    EditText editTextName, editTextDesc, editTextItemPrice, editTextItemPreRate, editTextItemTranRate;
    TextView textViewCountName, textViewCountDesc;
    ImageView buttonPic1, buttonPic2, buttonPic3;
    CardView cardView2, cardView3;
    Spinner spinner;
    Button button;
    int SELECT_TYPE = 0;
    int SELECT_IMAGE1 = 1001;
    int SELECT_IMAGE2 = 1002;
    int SELECT_IMAGE3 = 1003;
    HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
    int eventID;
    String userId;
    private ArrayList<String> typeList = new ArrayList<String>();
    ArrayList<String> imgAr = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);
        setToolbar();
        setHashMap();
        eventID = getIntent().getIntExtra("eventID", -1);
        userId = SharedPrefManager.getInstance(CreateItem.this).getUser().getUserId();
        spinner = findViewById(R.id.spinner_type);
        buttonPic1 = (ImageView) findViewById(R.id.button_create_picture1);
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
        createTypeData();
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
        buttonPic1.setOnClickListener(this);
        buttonPic1.setOnLongClickListener(this);
        buttonPic2.setOnClickListener(this);
        buttonPic2.setOnLongClickListener(this);
        buttonPic3.setOnClickListener(this);
        buttonPic3.setOnLongClickListener(this);

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
                setEditText(editTextItemPrice, editable.toString(), this);
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
                setEditText(editTextItemPreRate, editable.toString(), this);
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
                setEditText(editTextItemTranRate, editable.toString(), this);
            }
        });
        button.setOnClickListener(this);
    }

    private void createTypeData() {
        typeList.add("เลือกประเภท");
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


    private void setEditText(EditText editText, String str, TextWatcher textWatcher) {
        editText.removeTextChangedListener(textWatcher);
        if (!str.isEmpty()) {
            String format = String.format("%,d", Integer.parseInt(str.replace(",", "")));
            editText.setText(format);
            editText.setSelection(editText.getText().toString().length());
        } else {
            editText.setSelection(0);
        }

        editText.addTextChangedListener(textWatcher);
    }

    void selectImage(int req) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select Image from Gallery"), req);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_create_picture1:
                //action
                selectImage(SELECT_IMAGE1);
                break;
            case R.id.button_create_picture2:
                selectImage(SELECT_IMAGE2);
                break;
            case R.id.button_create_picture3:
                selectImage(SELECT_IMAGE3);
                break;
            case R.id.button_create:
                saveItem();
                break;
        }
    }


    public String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }

    public Bitmap StringToBitMap(String image) {
        try {
            byte[] encodeByte = Base64.decode(image, Base64.DEFAULT);

            InputStream inputStream = new ByteArrayInputStream(encodeByte);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    void saveItem() {
        String id = userId;
        int eventId = eventID;
        String itemName = editTextName.getText().toString().trim();
        String price = editTextItemPrice.getText().toString().replace(",", "");
        String prerate = editTextItemPreRate.getText().toString().replace(",", "");
        String tranrate = editTextItemTranRate.getText().toString().replace(",", "");
        String itemDesc = editTextDesc.getText().toString().trim();
        String[] str = {"", "", ""};
        for (int i = 0; i < imgAr.size(); i++) {
            str[i] = imgAr.get(i);
        }
        if (imgAr.size() == 0) {
            Toast.makeText(this, "กรุณาอัพโหลดรูปภาพ", Toast.LENGTH_SHORT).show();
        } else if (itemName.isEmpty() || price.isEmpty() || prerate.isEmpty() || tranrate.isEmpty()) {
            Toast.makeText(this, "กรุณากรอกข้อมูลให้ครบถ้วน", Toast.LENGTH_SHORT).show();
        } else if (SELECT_TYPE == 0) {
            Toast.makeText(this, "กรุณาเลือกประเภทสินค้า", Toast.LENGTH_SHORT).show();
        } else {
            int itemPrice = Integer.parseInt(price);
            int itemPreRate = Integer.parseInt(prerate);
            int itemTranRate = Integer.parseInt(tranrate);
            createItem(id, eventId, itemName, itemPrice, itemPreRate, itemTranRate, itemDesc
                    , str[0], str[1], str[2], SELECT_TYPE);
        }
    }

    public void createItem(String id, int eventId, String itemName, int itemPrice, int itemPreRate, int itemTranRate, String itemDesc
            , String img1, String img2, String img3 , int type) {
        final ApiInterface apiService = ConnectServer.getClient().create(ApiInterface.class);


        Call<ResponseModel> call = apiService.createitem(id, eventId, itemName, itemPrice, itemPreRate, itemTranRate
                , itemDesc, img1, img2, img3, type);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                ResponseModel res = response.body();
                Toast.makeText(CreateItem.this, res.getResponse(), Toast.LENGTH_SHORT).show();
                if (res.getStatus()) {
                    finish();
                }

            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {

            }
        });

    }

    private String getPath(Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String path = cursor.getString(columnIndex);
        cursor.close();
        return path;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_IMAGE1) {
                if (data != null) {
                    String picturePath = getPath(data);
                    buttonPic1.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                    cardView2.setVisibility(View.VISIBLE);
                    imgAr.add(bitmapToString(BitmapFactory.decodeFile(picturePath)));
                }
            } else if (requestCode == SELECT_IMAGE2) {
                if (data != null) {
                    String picturePath = getPath(data);
                    buttonPic2.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                    cardView3.setVisibility(View.VISIBLE);
                    imgAr.add(bitmapToString(BitmapFactory.decodeFile(picturePath)));
                }
            } else if (requestCode == SELECT_IMAGE3) {
                if (data != null) {
                    String picturePath = getPath(data);
                    buttonPic3.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                    imgAr.add(bitmapToString(BitmapFactory.decodeFile(picturePath)));
                }
            }
        } else {
            Toast.makeText(CreateItem.this, "Canceled", Toast.LENGTH_SHORT).show();
        }
    }

    private void delPic1() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(CreateItem.this);
        builder.setTitle("ต้องการลบรูปภาพ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (imgAr.size() != 0) {
                    if (imgAr.size() == 3) {
                        Bitmap bitmap = StringToBitMap(imgAr.get(1));
                        buttonPic1.setImageBitmap(bitmap);
                        bitmap = StringToBitMap(imgAr.get(2));
                        buttonPic2.setImageBitmap(bitmap);
                        buttonPic3.setImageResource(R.drawable.ic_photo);
                    } else if (imgAr.size() == 2) {
                        Bitmap bitmap = StringToBitMap(imgAr.get(1));
                        buttonPic1.setImageBitmap(bitmap);
                        buttonPic2.setImageResource(R.drawable.ic_photo);
                        cardView3.setVisibility(View.INVISIBLE);
                    } else {
                        buttonPic1.setImageResource(R.drawable.ic_photo);
                        cardView2.setVisibility(View.INVISIBLE);
                    }
                    imgAr.remove(0);
                }
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

    private void delPic2() {
        if (imgAr.size() > 1) {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(CreateItem.this);
            builder.setTitle("ต้องการลบรูปภาพ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    if (imgAr.size() == 3) {
                        Bitmap bitmap = StringToBitMap(imgAr.get(2));
                        buttonPic2.setImageBitmap(bitmap);
                        buttonPic3.setImageResource(R.drawable.ic_photo);
                    } else if (imgAr.size() == 2) {
                        buttonPic2.setImageResource(R.drawable.ic_photo);
                        cardView3.setVisibility(View.INVISIBLE);
                    }
                    imgAr.remove(1);
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
    }

    private void delPic3() {
        if (imgAr.size() == 3) {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(CreateItem.this);
            builder.setTitle("ต้องการลบรูปภาพ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    buttonPic3.setImageResource(R.drawable.ic_photo);
                    imgAr.remove(2);
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
    }

    @Override
    public boolean onLongClick(View view) {

        switch (view.getId()) {
            case R.id.button_create_picture1:
                delPic1();
                break;
            case R.id.button_create_picture2:
                delPic2();
                break;
            case R.id.button_create_picture3:
                delPic3();
                break;
        }
        return false;
    }
}
