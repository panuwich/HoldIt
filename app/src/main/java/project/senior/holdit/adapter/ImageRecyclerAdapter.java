package project.senior.holdit.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import project.senior.holdit.R;

public class ImageRecyclerAdapter  extends RecyclerView.Adapter<ImageRecyclerAdapter.ViewHolder> {

    private ArrayList<String> resultList;
    private LayoutInflater mInflater;
    private ItemListGridAdapter.ItemClickListener mClickListener;

    // data is passed into the constructor
    public ImageRecyclerAdapter(Context context, ArrayList<String> resultList) {
        this.mInflater = LayoutInflater.from(context);
        this.resultList = resultList;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ImageRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.detail_issue_image, parent, false);
        return new ImageRecyclerAdapter.ViewHolder(view);
    }


    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ImageRecyclerAdapter.ViewHolder holder, int position) {
        holder.imageView.setImageBitmap(BitmapFactory.decodeFile(resultList.get(position)));
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return resultList.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return resultList.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemListGridAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}