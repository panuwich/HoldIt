package project.senior.holdit.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import project.senior.holdit.R;
import project.senior.holdit.model.Item;


public class itemListDescAdapter extends RecyclerView.Adapter<itemListDescAdapter.ViewHolder> {

    private ArrayList<Item> resultList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public itemListDescAdapter(Context context, ArrayList<Item> resultList) {
        this.mInflater = LayoutInflater.from(context);
        this.resultList = resultList;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.detail_item_desc, parent, false);

        return new ViewHolder(view);
    }


    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //holder.myTextView.setText();
        String url = "http://pilot.cp.su.ac.th/usr/u07580319/holdit/pics/item/"+resultList.get(position).getItemImg1();
        Picasso.get().load(url).into(holder.imageView);
        holder.desc.setText(resultList.get(position).getItemDesc());
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return resultList.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView desc;
        ImageView imageView;
        ViewHolder(View itemView) {
            super(itemView);
            //myTextView = itemView.findViewById(R.id.info_text);
            imageView = (ImageView)itemView.findViewById(R.id.imageView_deteil_itemdesc);
            desc = (TextView)itemView.findViewById(R.id.textView_detail_itemdesc_desc);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());


        }
    }

    // convenience method for getting data at click position
    Item getItem(int id) {
        return resultList.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}