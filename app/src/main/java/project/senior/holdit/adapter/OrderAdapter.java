package project.senior.holdit.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import project.senior.holdit.R;
import project.senior.holdit.model.Chat;
import project.senior.holdit.model.Order;
import project.senior.holdit.model.User;


public class OrderAdapter extends BaseAdapter {
    Context mContext;
    private int mLayoutResId;
    private ArrayList<Order> resultList;
    private boolean buyer;
    String lastMessage;

    public OrderAdapter(Context context, int mLayoutResId, ArrayList<Order> resultList,boolean buyer) {
        this.mContext = context;
        this.mLayoutResId = mLayoutResId;
        this.resultList = resultList;
        this.buyer = buyer;
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public Object getItem(int i) {
       return resultList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, final View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        final View itemView = inflater.inflate(mLayoutResId, null);
        TextView textViewOrderId = (TextView) itemView.findViewById(R.id.textView_chat_list_id);
        final TextView textViewOrderName = (TextView) itemView.findViewById(R.id.textView_chat_list_name);
        TextView textViewOrderMsg = (TextView) itemView.findViewById(R.id.textView_chat_list_msg);
        TextView textViewOrderStatus = (TextView) itemView.findViewById(R.id.textView_chat_list_status);
        final CircleImageView circleImageView = (CircleImageView) itemView.findViewById(R.id.img_chat_list);
        TextView textViewCount = (TextView)itemView.findViewById(R.id.textView_chat_list_count);

        DatabaseReference reference;
        if(buyer){
            reference = FirebaseDatabase.getInstance().getReference("Users").child(resultList.get(i).getSellerId());
            lastMsg(resultList.get(i).getSellerId(),textViewOrderMsg
                    ,""+resultList.get(i).getId(),textViewCount,resultList.get(i).getStatus());
        }else{
            reference = FirebaseDatabase.getInstance().getReference("Users").child(resultList.get(i).getBuyerId());
            lastMsg(resultList.get(i).getBuyerId(),textViewOrderMsg
                    ,""+resultList.get(i).getId(),textViewCount,resultList.get(i).getStatus());
        }
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                textViewOrderName.setText(user.getUserFirstname());
                if(user.getUserImage().isEmpty()){
                    circleImageView.setImageResource(R.drawable.user);
                }else{
                    String url = "http://pilot.cp.su.ac.th/usr/u07580319/holdit/pics/profile/"+user.getUserImage();
                    Picasso.get().load(url).into(circleImageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        textViewOrderId.setText(""+resultList.get(i).getId());
        if(resultList.get(i).getStatus() == 0){
            textViewOrderStatus.setText("รอผู้ซื้อตอบรับ");
            textViewOrderStatus.setTextColor(mContext.getResources().getColor(R.color.colorOrange));
        }else if (resultList.get(i).getStatus() == 1){
            textViewOrderStatus.setText("รอชำระเงิน");
            textViewOrderStatus.setTextColor(mContext.getResources().getColor(R.color.colorYellow));
        }else if (resultList.get(i).getStatus() == 2){
            textViewOrderStatus.setText("รอรับสินค้า");
            textViewOrderStatus.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        }else if (resultList.get(i).getStatus() == 3){
            textViewOrderStatus.setText("สำเร็จ");
            textViewOrderStatus.setTextColor(mContext.getResources().getColor(R.color.colorGreen));
        }else{
            textViewOrderStatus.setText("ยกเลิก");
            textViewOrderStatus.setTextColor(mContext.getResources().getColor(R.color.colorRed));
            textViewCount.setVisibility(View.GONE);
        }


        return itemView;
    }

    private void lastMsg(final String userid, final TextView lastMsg, String orderId, final TextView textViewCount, final int stauts){
        lastMessage = "";
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats").child(orderId);
        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int countUnseen = 0;
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Chat chat = data.getValue(Chat.class);
                    if(chat.getReceiver().equals(fuser.getUid()) &&  chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(fuser.getUid())){
                        lastMessage = chat.getMessage();
                        if(!chat.isIsseen() && chat.getReceiver().equals(fuser.getUid())){
                            countUnseen++;
                        }else{
                            countUnseen = 0;
                        }
                    }
                }
                lastMsg.setText(lastMessage);
                if (countUnseen == 0 || stauts == -1 ){
                    textViewCount.setVisibility(View.GONE);
                }else {
                    textViewCount.setText(""+countUnseen);
                    textViewCount.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}