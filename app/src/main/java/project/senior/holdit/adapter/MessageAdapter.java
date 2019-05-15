package project.senior.holdit.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import project.senior.holdit.R;
import project.senior.holdit.model.Chat;

 public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context mCtx;
    private List<Chat> chatList;
    private String imageUrl;

    FirebaseUser firebaseUser;
    public MessageAdapter(Context mCtx, List<Chat> chatList, String imageUrl) {
        this.mCtx = mCtx;
        this.chatList = chatList;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if( i == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(mCtx).inflate(R.layout.chat_item_right, viewGroup, false);
            return new MessageAdapter.ViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(mCtx).inflate(R.layout.chat_item_left, viewGroup, false);
            return new MessageAdapter.ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int i) {
        Chat chat = chatList.get(i);
        holder.textViewShowTextMessage.setText(chat.getMessage());
        if(imageUrl.isEmpty()){
            holder.circleImageViewUser.setImageResource(R.mipmap.ic_launcher);
        }else{
            String url = "http://pilot.cp.su.ac.th/usr/u07580319/holdit/pics/profile/"+imageUrl;
            Picasso.get().load(url).into(holder.circleImageViewUser);
        }
        if(chat.isIsseen() && holder.getItemViewType() == MSG_TYPE_RIGHT){
            holder.textViewSeen.setVisibility(View.VISIBLE);
        }else{
            holder.textViewSeen.setVisibility(View.GONE);
        }
        holder.textViewTime.setText(chat.getTime());
    }


    @Override
    public int getItemCount() {
        return chatList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageViewUser;
        TextView textViewShowTextMessage;
        TextView textViewSeen;
        TextView textViewTime;
        public ViewHolder(View itemView) {
            super(itemView);
            circleImageViewUser = itemView.findViewById(R.id.circleImageViewUser);
            textViewShowTextMessage = itemView.findViewById(R.id.textViewShowTextMessage);
            textViewSeen = itemView.findViewById(R.id.textViewSeen);
            textViewTime = itemView.findViewById(R.id.textViewTime);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (chatList.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }
}
