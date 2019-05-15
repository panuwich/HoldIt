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
import project.senior.holdit.model.Bank;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {

    private ArrayList<Bank> resultList;
    private LayoutInflater mInflater;

    // data is passed into the constructor
    public PaymentAdapter(Context context, ArrayList<Bank> resultList) {
        this.mInflater = LayoutInflater.from(context);
        this.resultList = resultList;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public PaymentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.detail_list_cardview, parent, false);
        return new PaymentAdapter.ViewHolder(view);
    }


    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull PaymentAdapter.ViewHolder holder, int position) {

        String url = resultList.get(position).getLogo();
        Picasso.get().load(url).into(holder.imageView);
        holder.textView.setText(resultList.get(position).getName());
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return resultList.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView textView;
        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView_detail_list_cardview);
            textView = itemView.findViewById(R.id.textView_detail_list_name);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {

        }
    }

    // convenience method for getting data at click position
    Bank getItem(int id) {
        return resultList.get(id);
    }


}