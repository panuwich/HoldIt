package project.senior.holdit.verify;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import project.senior.holdit.R;

public class VerificationIDCard extends AppCompatActivity {
    int CAMERA_PIC_REQUEST = 200;
    private String currentPhotoPath = "";
    final int RequestPermissionCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_id_card);
        int permissionCheck1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int permissionCheck2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCheck3 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck1 == PackageManager.PERMISSION_DENIED || permissionCheck2 == PackageManager.PERMISSION_DENIED
                || permissionCheck3 == PackageManager.PERMISSION_DENIED)
            RequestRuntimePermission();


        CircleImageView circleImageView = (CircleImageView) findViewById(R.id.imageView_veri_id_card_camera);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_PIC_REQUEST);
            }
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {
            File file = new File(currentPhotoPath);
            try {
                Bitmap bitmap = MediaStore.Images.Media
                        .getBitmap(getBaseContext().getContentResolver(), Uri.fromFile(file));

                final AlertDialog.Builder builder =
                        new AlertDialog.Builder(VerificationIDCard.this);
                LayoutInflater inflater = getLayoutInflater();

                View v = inflater.inflate(R.layout.detail_dialog_veri_idcard, null);
                builder.setView(v);
                final AlertDialog alertDialog = builder.create();

                alertDialog.getWindow().setBackgroundDrawable(new
                        ColorDrawable(Color.TRANSPARENT));
                ImageView imageViewCard = (ImageView) v.findViewById(R.id.imageView_dialog_veri_idcard);
                //  Button buttonConfirm = (Button)v.findViewById(R.id.button_dialog_veri_idcard_confirm);
                Button buttonCancel = (Button) v.findViewById(R.id.button_dialog_veri_idcard_cancel);

                imageViewCard.setImageBitmap(bitmap);
                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.cancel();
                    }
                });
                alertDialog.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //ImageView imageview = (ImageView) findViewById(R.id.ImageView01); //sets imageview as the bitmap
                //imageview.setImageBitmap(image);


            }

    }

    private void RequestRuntimePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA))
            Toast.makeText(this, "CAMERA permission allows us to access CAMERA app", Toast.LENGTH_SHORT).show();
        else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, RequestPermissionCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Permission Canceled", Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

}
