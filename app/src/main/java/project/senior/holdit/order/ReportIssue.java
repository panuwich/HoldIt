package project.senior.holdit.order;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import project.senior.holdit.R;
import project.senior.holdit.adapter.ImageRecyclerAdapter;
import project.senior.holdit.adapter.RecyclerItemClickListener;
import project.senior.holdit.manager.SharedPrefManager;
import project.senior.holdit.model.ResponseModel;
import project.senior.holdit.model.User;
import project.senior.holdit.retrofit.ApiInterface;
import project.senior.holdit.retrofit.ConnectServer;
import project.senior.holdit.util.ImageUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportIssue extends AppCompatActivity {

    ArrayList<String> arImage = new ArrayList<>();
    RecyclerView recyclerView;
    int SELECT_REASON = 0;
    boolean CHECK_REASON = false;
    String[] reason = getReasonList();
    ImageButton imageButton;
    TextView reasonText;
    int orderId;
    EditText desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_issue);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, GridLayoutManager.HORIZONTAL, false);
        orderId = getIntent().getIntExtra("orderId", -1);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_issue);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        arImage.remove(position);
                        //Check remove
                        recyclerView.setAdapter(new ImageRecyclerAdapter(ReportIssue.this, arImage));
                        checkQuota();
                    }
                })
        );
        imageButton = findViewById(R.id.image_add_img);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        reasonText = findViewById(R.id.report_issue_reason_text);
        RelativeLayout reasonLayout = findViewById(R.id.report_issue_reason);
        reasonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectReason();
            }
        });
        desc = findViewById(R.id.report_issue_desc);
        Button button = findViewById(R.id.button_report_issue);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CHECK_REASON){
                    saveImage();
                    saveIssue();
                }else{
                    Toast.makeText(ReportIssue.this, "กรุณาเลือกเหตุผลการรายงานปัญหา", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select Image from Gallery"), 100);
    }
    private void checkQuota(){
        if (arImage.size() == 3){
            imageButton.setVisibility(View.GONE);
        }else{
            imageButton.setVisibility(View.VISIBLE);
        }
    }

    private void selectReason() {
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(ReportIssue.this);

        builder.setSingleChoiceItems(reason, SELECT_REASON, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                String selected = reason[i];
                SELECT_REASON = i;
                CHECK_REASON = true;
                reasonText.setText(selected);
                dialog.dismiss();
            }
        });
        builder.create();

        builder.show();
    }

    private String[] getReasonList(){
        String[] reason = {
                "ฉันไม่ได้รับสินค้า",
                "สินค้ามีสภาพไม่สมบูรณ์",
                "ได้รับสินค้าไม่ถูกต้อง",
                "ได้รับสินค้าที่การทำงานไม่สมบูรณ์"};
        return reason;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            if (data != null) {
                String picturePath = ImageUtil.getPath(data, getContentResolver());
                arImage.add(picturePath);
                recyclerView.setAdapter(new ImageRecyclerAdapter(ReportIssue.this,arImage));
                checkQuota();
            }

        }
    }

    private void saveIssue(){
        final ApiInterface apiService = ConnectServer.getClient().create(ApiInterface.class);
        User user = SharedPrefManager.getInstance(ReportIssue.this).getUser();
        Call<ResponseModel> call = apiService.issue(user.getUserId(), orderId, reasonText.getText().toString(), desc.getText().toString());
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.body().getStatus()){
                    Toast.makeText(ReportIssue.this, response.body().getResponse(), Toast.LENGTH_SHORT).show();
                    finish();
                }else{

                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {

            }
        });
    }

    private void saveImage(){
        final ApiInterface apiService = ConnectServer.getClient().create(ApiInterface.class);
        for (String img : arImage){
            String encodeImage = ImageUtil.bitmapToString(BitmapFactory.decodeFile(img));
            Call<ResponseModel> call = apiService.issue_image(orderId, encodeImage);
            call.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                }
            });
        }

    }
}
