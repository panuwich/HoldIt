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

import java.util.List;

import project.senior.holdit.R;
import project.senior.holdit.manager.SharedPrefManager;
import project.senior.holdit.model.Finding;

public class FindingAdapter extends RecyclerView.Adapter<FindingAdapter.ViewHolder> {

    private Context mCtx;
    private List<Finding> itemList;

    public FindingAdapter(Context mCtx, List<Finding> itemList) {
        this.mCtx = mCtx;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(mCtx).inflate(R.layout.detail_finding_list, viewGroup, false);
            return new FindingAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull FindingAdapter.ViewHolder holder, int i) {
        Finding finding = itemList.get(i);

        String url = "http://pilot.cp.su.ac.th/usr/u07580319/holdit/pics/item/"+ finding.getImage();
        Picasso.get().load(url).into(holder.imageView);

        holder.textViewName.setText(finding.getName());
        if (finding.getLocation().isEmpty()){
            holder.textViewLocation.setText("-");
        }else{
            holder.textViewLocation.setText(finding.getLocation());
        }
        if (finding.getUserId().equals(SharedPrefManager.getInstance(mCtx).getUser().getUserId())){
            holder.textViewMyorder.setVisibility(View.VISIBLE);
        }else{
            holder.textViewMyorder.setVisibility(View.GONE);
        }
        holder.textViewAmount.setText(""+finding.getAmount());
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewName;
        TextView textViewLocation;
        TextView textViewAmount;
        TextView textViewMyorder;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView =itemView.findViewById(R.id.imageView_finding);
            textViewName=itemView.findViewById(R.id.textView_findind_name);
            textViewLocation=itemView.findViewById(R.id.textView_findind_location);
            textViewAmount=itemView.findViewById(R.id.textView_findind_amount);
            textViewMyorder=itemView.findViewById(R.id.textView_myorder);

        }
    }

}
