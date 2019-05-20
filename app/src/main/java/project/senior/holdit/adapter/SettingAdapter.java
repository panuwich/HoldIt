package project.senior.holdit.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import project.senior.holdit.R;
import project.senior.holdit.model.Item;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.ViewHolder> {

    private Context mCtx;
    private List<Item> itemList;

    public SettingAdapter(Context mCtx, List<Item> itemList) {
        this.mCtx = mCtx;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public SettingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.detail_list_setting_item, viewGroup, false);
        return new SettingAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull SettingAdapter.ViewHolder holder, int i) {
        Item item = itemList.get(i);
        String url = "http://pilot.cp.su.ac.th/usr/u07580319/holdit/pics/item/"+ item.getItemImg1();
        Picasso.get().load(url).into(holder.imageView);
        holder.textViewName.setText(item.getItemName());
        holder.textViewEvent.setText(item.getEventName());
        if (item.getStatus() == 1){
            holder.switchCompat.setChecked(true);
        }else{
            holder.switchCompat.setChecked(false);
        }

    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewName;
        TextView textViewEvent;
        SwitchCompat switchCompat;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView =itemView.findViewById(R.id.imageView_setting);
            textViewName=itemView.findViewById(R.id.textView_name_setting);
            textViewEvent=itemView.findViewById(R.id.textView_event_setting);
            switchCompat=itemView.findViewById(R.id.switch_setting);

        }
    }

}
