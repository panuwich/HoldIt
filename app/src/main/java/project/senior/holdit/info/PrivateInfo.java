package project.senior.holdit.info;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import project.senior.holdit.R;
import project.senior.holdit.dialog.AlertDialogService;
import project.senior.holdit.manager.SharedPrefManager;
import project.senior.holdit.model.ResponseModel;
import project.senior.holdit.model.User;
import project.senior.holdit.retrofit.ApiInterface;
import project.senior.holdit.retrofit.ConnectServer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrivateInfo extends AppCompatActivity implements View.OnClickListener {
    CircleImageView circleImageView;
    TextView textViewEmail;
    TextView textViewFirstname;
    TextView textViewLastname;
    TextView textViewTel;
    String image_profile = "";
    int SELECT_IMAGE = 1001;
    int CROP_IMAGE = 2001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_info);
        setToolbar();
        circleImageView = findViewById(R.id.imageView_info);
        textViewEmail = findViewById(R.id.textView_info_email);
        textViewFirstname = findViewById(R.id.textView_info_firstname);
        textViewLastname = findViewById(R.id.textView_info_lastname);
        textViewTel = findViewById(R.id.textView_info_tel);

        findViewById(R.id.imageView_info).setOnClickListener(this);
        findViewById(R.id.layout_set_newpass).setOnClickListener(this);
        findViewById(R.id.layout_set_firstname).setOnClickListener(this);
        findViewById(R.id.layout_set_lastname).setOnClickListener(this);
        findViewById(R.id.layout_set_tel).setOnClickListener(this);


        User user = SharedPrefManager.getInstance(PrivateInfo.this).getUser();

        if(!user.getUserImage().isEmpty()){
        String url = "http://pilot.cp.su.ac.th/usr/u07580319/holdit/pics/profile/" + user.getUserImage();
        Picasso.get().load(url).into(circleImageView);
        }
        textViewEmail.setText(user.getUserEmail());
        textViewFirstname.setText(user.getUserFirstname());
        textViewLastname.setText(user.getUserLastname());
        textViewTel.setText(user.getUserTel());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_info);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
    }

    public String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }

    private void CropImage(Uri uri) {

        try {
            Intent CropIntent = new Intent("com.android.camera.action.CROP");
            CropIntent.setDataAndType(uri, "image/*");

            CropIntent.putExtra("crop", "true");
            CropIntent.putExtra("outputX", 150);
            CropIntent.putExtra("outputY", 150);
            CropIntent.putExtra("aspectX", 4);
            CropIntent.putExtra("aspectY", 4);
            CropIntent.putExtra("scaleUpIfNeeded", true);
            CropIntent.putExtra("return-data", true);

            startActivityForResult(CropIntent, CROP_IMAGE);
        } catch (ActivityNotFoundException ex) {

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView_info:
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, "Select Image from Gallery"), SELECT_IMAGE);
                break;
            case R.id.layout_set_firstname:
                new AlertDialogService(PrivateInfo.this, getLayoutInflater()).dialogInput(3
                        , textViewFirstname, textViewFirstname.getText().toString());
                break;
            case R.id.layout_set_lastname:
                new AlertDialogService(PrivateInfo.this, getLayoutInflater()).dialogInput(4
                        , textViewLastname, textViewLastname.getText().toString());
                break;
            case R.id.layout_set_tel:
                new AlertDialogService(PrivateInfo.this, getLayoutInflater()).dialogInput(5
                        , textViewTel, textViewTel.getText().toString());
                break;
            case R.id.layout_set_newpass:
                startActivity(new Intent(PrivateInfo.this,NewPassword.class));
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_IMAGE) {
                if (data != null) {
                    CropImage(data.getData());
                }

            } else if (requestCode == CROP_IMAGE) {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = bundle.getParcelable("data");
                image_profile = imageToString(bitmap);
                circleImageView.setImageBitmap(bitmap);
                setDB();
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(PrivateInfo.this, getResources().getString(R.string.cancel), Toast.LENGTH_SHORT).show();
        }
    }

    private void setDB() {
        final ApiInterface apiService = ConnectServer.getClient().create(ApiInterface.class);
        final User user = SharedPrefManager.getInstance(PrivateInfo.this).getUser();
        Call<ResponseModel> call = apiService.updateuser(1, user.getUserId(), "", image_profile);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, final Response<ResponseModel> response) {
                if (response.body().isStatus()) {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUserId());
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("userImage", response.body().getResponse());
                                snapshot.getRef().updateChildren(map);
                                user.setUserImage(response.body().getResponse());
                                SharedPrefManager.getInstance(PrivateInfo.this).saveUser(user);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    Toast.makeText(PrivateInfo.this, response.body().getResponse(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {

            }
        });

    }
}
