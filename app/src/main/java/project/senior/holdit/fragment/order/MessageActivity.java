package project.senior.holdit.fragment.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import project.senior.holdit.R;
import project.senior.holdit.adapter.MessageAdapter;
import project.senior.holdit.model.Chat;
import project.senior.holdit.model.User;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener{
    FirebaseUser fuser;
    DatabaseReference reference;
    RecyclerView recyclerView;
    List<Chat> chatList;
    private EditText editTextSend;
    MessageAdapter messageAdapter;
    TextView textViewStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Intent getIntent = getIntent();
        final String userId = getIntent.getStringExtra("userId");
        final int status = getIntent.getIntExtra("status",-1);
        final int orderId = getIntent.getIntExtra("orderId",-1);

        TextView textViewOrderId = (TextView)findViewById(R.id.textView_message_orderid);
        textViewStatus = (TextView)findViewById(R.id.textView_message_status);

        findViewById(R.id.imageButtonSend_message).setOnClickListener(this);
        final CircleImageView circleImageView = (CircleImageView)findViewById(R.id.circleImageViewUser_message);
        final TextView textViewUserId = (TextView)findViewById(R.id.textView_message_name);
        textViewOrderId.setText(""+orderId);
        setStatus(status);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView_message);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        editTextSend = (EditText)findViewById(R.id.editTextSend_message);

        setToolbar();

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                textViewUserId.setText(user.getUserFirstname());
                if(user.getUserImage().isEmpty()){
                    circleImageView.setImageResource(R.mipmap.ic_launcher);
                }else{
                    String url = "http://pilot.cp.su.ac.th/usr/u07580319/holdit/pics/profile/"+user.getUserImage();
                    Picasso.get().load(url).into(circleImageView);
                }
                readMessages(fuser.getUid(), userId, user.getUserImage());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void setToolbar(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_message);
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

    public void sendMessage(){

        String sender = fuser.getUid();
        final String receiver = getIntent().getStringExtra("userId");
        String message = editTextSend.getText().toString();

        if(!message.equals("")){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("sender", sender);
            hashMap.put("receiver", receiver);
            hashMap.put("message", message);

            reference.child("Chats").push().setValue(hashMap);

            final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
                    .child(fuser.getUid())
                    .child(receiver);

            chatRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.exists()){
                        chatRef.child("id").setValue(receiver);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else{
            Toast.makeText(MessageActivity.this, "ยังไม่ได้พิมพ์ข้อความ", Toast.LENGTH_SHORT).show();
        }
        editTextSend.setText("");
    }

    public void readMessages(final String myid , final String userid, final String imageurl){
        chatList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(myid) &&  chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){
                        chatList.add(chat);
                    }
                    messageAdapter = new MessageAdapter(MessageActivity.this , chatList, imageurl);
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
        }
    }

    public void setStatus(int status) {
        if(status == 0){
            textViewStatus.setText("รอชำระเงิน");
            textViewStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
        }else if(status == 1){
            textViewStatus.setText("รอรับสินค้า");
            textViewStatus.setTextColor(getResources().getColor(R.color.colorAccent));
        }else if(status == 2){
            textViewStatus.setText("สำเร็จ");
            textViewStatus.setTextColor(getResources().getColor(R.color.colorGreen));
        }else{
            textViewStatus.setText("ยกเลิก");
            textViewStatus.setTextColor(getResources().getColor(R.color.colorRed));
        }
    }
}
