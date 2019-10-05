package project.senior.holdit.order;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import project.senior.holdit.R;
import project.senior.holdit.adapter.MessageAdapter;
import project.senior.holdit.dialog.AlertDialogService;
import project.senior.holdit.enumuration.OrderStatusEnum;
import project.senior.holdit.manager.SharedPrefManager;
import project.senior.holdit.model.Chat;
import project.senior.holdit.model.Data;
import project.senior.holdit.model.NotiResponse;
import project.senior.holdit.model.Order;
import project.senior.holdit.model.ResponseModel;
import project.senior.holdit.model.Sender;
import project.senior.holdit.model.Token;
import project.senior.holdit.model.User;
import project.senior.holdit.payment.SelectPaymentActivity;
import project.senior.holdit.retrofit.ApiInterface;
import project.senior.holdit.retrofit.ConnectServer;
import project.senior.holdit.retrofit.ConnectServerNoti;
import project.senior.holdit.retrofit.NotiApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener,NavigationView.OnNavigationItemSelectedListener {
    FirebaseUser fuser;
    DatabaseReference reference;
    RecyclerView recyclerView;
    List<Chat> chatList;
    private EditText editTextSend;
    MessageAdapter messageAdapter;
    TextView textViewOrderId;
    TextView textViewStatus;
    ValueEventListener seenListener;
    ImageButton imageButton;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    NavigationView navigationView;
    boolean  notify = false;
    NotiApi apiService;
    Order order;
    String track = null;
    OrderStatusEnum status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);
        navigationView = (NavigationView)findViewById(R.id.navi_message);

        navigationView.setNavigationItemSelectedListener(this);
        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        apiService = ConnectServerNoti.getClient("https://fcm.googleapis.com/").create(NotiApi.class);
        Intent getIntent = getIntent();
        final String userId = getIntent.getStringExtra("userId");
        order = (Order)getIntent.getSerializableExtra("order");
        status = order.getStatus();
        final int orderId = order.getId();

        imageButton = (ImageButton)findViewById(R.id.imageBtn_message_menu);
        textViewOrderId = (TextView) findViewById(R.id.textView_message_orderid);
        textViewStatus = (TextView) findViewById(R.id.textView_message_status);
        imageButton.setOnClickListener(this);
        findViewById(R.id.imageButtonSend_message).setOnClickListener(this);
        final CircleImageView circleImageView = (CircleImageView) findViewById(R.id.circleImageViewUser_message);
        final TextView textViewUserId = (TextView) findViewById(R.id.textView_message_name);
        textViewOrderId.setText("" + orderId);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_message);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        editTextSend = (EditText) findViewById(R.id.editTextSend_message);

        setToolbar();

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                textViewUserId.setText(user.getUserFirstname());
                if (user.getUserImage().isEmpty()) {
                    circleImageView.setImageResource(R.drawable.user);
                } else {
                    String url = "http://pilot.cp.su.ac.th/usr/u07580319/holdit/pics/profile/" + user.getUserImage();
                    Picasso.get().load(url).into(circleImageView);
                }
                readMessages(fuser.getUid(), userId, user.getUserImage());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if(isSeller()){
            navigationView.inflateMenu(R.menu.menu_command_seller);
        }else{
            navigationView.inflateMenu(R.menu.menu_command_buyer);
        }
        setMenu();
        seenMsg(userId);
    }

    private boolean isSeller(){
        return order.getSellerId().equals(fuser.getUid());
    }
    private boolean isBuyer(){
        return order.getBuyerId().equals(fuser.getUid());
    }
    public void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_message);
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

    @Override
    protected void onStart() {
        super.onStart();
        setMenu();
        setStatus(status);
    }

    public void sendMessage() {
        notify = true;
        String sender = fuser.getUid();
        final String receiver = getIntent().getStringExtra("userId");
        String message = editTextSend.getText().toString();
        if(!message.isEmpty()) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("sender", sender);
            hashMap.put("receiver", receiver);
            hashMap.put("message", message);
            hashMap.put("isseen", false);
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            Date date = new Date();
            hashMap.put("time", formatter.format(date));

            reference.child("Chats").child(textViewOrderId.getText().toString()).push().setValue(hashMap);
/*
            final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
                    .child(fuser.getUid())
                    .child(receiver);

            chatRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        chatRef.child("id").setValue(receiver);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });*/
        }
        editTextSend.setText("");

        final String msg = message;
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (notify) {
                    sendNotification(receiver, SharedPrefManager.getInstance(MessageActivity.this).getUser().getUserFirstname(), msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendNotification(String receiver, final String name, final String message){
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = new Token((String)snapshot.getValue());
                    Data data = new Data(fuser.getUid(),R.mipmap.ic_launcher,name +": "+message,"New Message"
                            , getIntent().getStringExtra("userId"));

                    Sender sender = new Sender(data,token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<NotiResponse>() {
                                @Override
                                public void onResponse(Call<NotiResponse> call, Response<NotiResponse> response) {
                                    if(response.code()==200){
                                        if(response.body().success != 1 ){
                                            Toast.makeText(MessageActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<NotiResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void seenMsg(final String userid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats").child(textViewOrderId.getText().toString());
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(fuser.getUid()) && chat.getSender().equals(userid)) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("isseen", true);
                        snapshot.getRef().updateChildren(map);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void readMessages(final String myid, final String userid, final String imageurl) {
        chatList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats").child(textViewOrderId.getText().toString());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)) {
                        chatList.add(chat);
                    }
                    messageAdapter = new MessageAdapter(MessageActivity.this, chatList, imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.imageButtonSend_message:
                sendMessage();
                break;
            case R.id.imageBtn_message_menu:
                setDrawer();
                break;

        }
    }

    public void setDrawer(){
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            drawerLayout.closeDrawer(Gravity.RIGHT);
        }
        else {
            drawerLayout.openDrawer(Gravity.RIGHT);
        }
    }

    private void setStatus(OrderStatusEnum status) {
        if (status.equals(OrderStatusEnum.WAIT_FOR_ACCEPT)) {
            textViewStatus.setText(getResources().getString(R.string.status_wait_for_accept)); //orange
            textViewStatus.setTextColor(getResources().getColor(R.color.colorOrange));
        } else if (status.equals(OrderStatusEnum.WAIT_FOR_PAYMENT)) {
            textViewStatus.setText(getResources().getString(R.string.status_wait_for_payment)); // yellow
            textViewStatus.setTextColor(getResources().getColor(R.color.colorYellow));
        } else if (status.equals(OrderStatusEnum.WAIT_FOR_RECEIVE)) {
            textViewStatus.setText(getResources().getString(R.string.status_wait_for_receive)); // blue
            textViewStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else if (status.equals(OrderStatusEnum.SUCCESS)) {
            textViewStatus.setText(getResources().getString(R.string.status_success));
            textViewStatus.setTextColor(getResources().getColor(R.color.colorGreen));
        } else {
            textViewStatus.setText(getResources().getString(R.string.status_cancel));
            textViewStatus.setTextColor(getResources().getColor(R.color.colorRed));
        }
    }

    public void cancelorder(){
        final ApiInterface apiService = ConnectServer.getClient().create(ApiInterface.class);
        Call<ResponseModel> call = apiService.cancelorder(Integer.parseInt(textViewOrderId.getText().toString()));
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                Toast.makeText(MessageActivity.this, response.body().getResponse(), Toast.LENGTH_SHORT).show();
                if(response.body().isStatus()){
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {

            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListener);
    }

    void startToInfo(){
        Intent intent = new Intent(MessageActivity.this,OrderInfo.class);
        Order order = (Order)getIntent().getSerializableExtra("order");
        intent.putExtra("order",(Serializable) order);
        startActivity(intent);
        setDrawer();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.icon_cancel:
                cancelorder();
                break;
            case R.id.icon_info:
                startToInfo();
                break;
            case R.id.icon_accept:
                acceptOrder();
                break;
            case R.id.icon_money:
                startToSelectPayment();
                break;
            case R.id.icon_received:
                dialogReceived();
                break;
            case R.id.icon_delivery:
                dialogTrack(order.getId(),MessageActivity.this,getLayoutInflater());
                break;
            case R.id.icon_report_issue:
                Intent intent = new Intent(MessageActivity.this, ReportIssue.class);
                intent.putExtra("orderId", order.getId());
                startActivity(intent);
                break;
        }
        return true;
    }



    private void dialogReceived() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(MessageActivity.this);
        builder.setTitle("ยืนยันว่าได้รับสินค้าเรียบร้อยเเล้ว");
        builder.setMessage("ทำการโอนเงิน ฿" + order.getTotal() + " ไปยังผู้รับซื้อ");
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialogRating();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.close), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog.dismiss();
            }
        });
        builder.show();
    }
    private void dialogRating() {
        AlertDialogService alertDialogService = new AlertDialogService(MessageActivity.this,getLayoutInflater());
        alertDialogService.dialogRating(Integer.parseInt(textViewOrderId.getText().toString()) , order.getSellerId());

    }

    private void startToSelectPayment(){
        Intent intent = new Intent(MessageActivity.this,SelectPaymentActivity.class);
        intent.putExtra("order", (Serializable) order);
        startActivity(intent);
    }
    private void setMenu(){
        Menu menuNav=navigationView.getMenu();
        if(status.equals(OrderStatusEnum.WAIT_FOR_ACCEPT)){
            if(isBuyer()){
                menuNav.findItem(R.id.icon_money).setEnabled(false);
                menuNav.findItem(R.id.icon_received).setEnabled(false);
            }else{
                menuNav.findItem(R.id.icon_delivery).setEnabled(false);
            }
        }else if(status.equals(OrderStatusEnum.WAIT_FOR_PAYMENT)){
            if(isSeller()){
                menuNav.findItem(R.id.icon_accept).setEnabled(false);
                menuNav.findItem(R.id.icon_delivery).setEnabled(false);
            }else{
                menuNav.findItem(R.id.icon_received).setEnabled(false);
            }

        }else if(status.equals(OrderStatusEnum.WAIT_FOR_RECEIVE)){
            if(isBuyer()) {
                menuNav.findItem(R.id.icon_money).setEnabled(false);
                menuNav.findItem(R.id.icon_cancel).setEnabled(false);
            }else{
                menuNav.findItem(R.id.icon_accept).setEnabled(false);
            }
        }
    }

    private void acceptOrder() {
        final ApiInterface apiService = ConnectServer.getClient().create(ApiInterface.class);
        Call<ResponseModel> call = apiService.acceptorder(Integer.parseInt(textViewOrderId.getText().toString()));
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                Toast.makeText(MessageActivity.this, response.body().getResponse(), Toast.LENGTH_SHORT).show();
                status = OrderStatusEnum.WAIT_FOR_PAYMENT;
                setStatus(status);
                setMenu();
                setDrawer();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {

            }
        });
    }

    public void dialogTrack(final int orderId,final  Context context ,LayoutInflater inflater) {

        final AlertDialog.Builder builder =
                new AlertDialog.Builder(context);

        View view = inflater.inflate(R.layout.detail_dialog_input, null);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        final EditText editText = view.findViewById(R.id.editText_accept);
        Button button = view.findViewById(R.id.button_accept);
        TextView textView = view.findViewById(R.id.textView_title_input);
        textView.setText(getResources().getString(R.string.menu_enter_track));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().isEmpty() || editText.getText().toString().length() < 10) {
                    Toast.makeText(context, getResources().getString(R.string.toast_input_not_completely), Toast.LENGTH_SHORT).show();
                } else {
                    final ApiInterface apiService = ConnectServer.getClient().create(ApiInterface.class);
                    Call<ResponseModel> call = apiService.updatetrack(orderId, editText.getText().toString());
                    call.enqueue(new Callback<ResponseModel>() {
                        @Override
                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                            Toast.makeText(context, response.body().getResponse(), Toast.LENGTH_SHORT).show();
                            if (response.body().isStatus()){
                                track = editText.getText().toString();
                                order.setTrack(track);
                                alertDialog.dismiss();
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseModel> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }
}
