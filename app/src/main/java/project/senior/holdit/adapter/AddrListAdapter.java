package project.senior.holdit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import project.senior.holdit.R;
import project.senior.holdit.model.Address;

public class AddrListAdapter extends BaseAdapter {
    Context mContext;
    private int mLayoutResId;
    private ArrayList<Address> resultList;
    public AddrListAdapter(Context context, int mLayoutResId, ArrayList<Address> resultList) {
        this.mContext = context;
        this.mLayoutResId = mLayoutResId;
        this.resultList = resultList;
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemLayout = inflater.inflate(mLayoutResId, null);
        TextView textViewName = (TextView)itemLayout.findViewById(R.id.textView_detail_item_addr_name);
        TextView textViewTel = (TextView)itemLayout.findViewById(R.id.textView_detail_item_addr_tel);
        TextView textViewAddr= (TextView)itemLayout.findViewById(R.id.textView_detail_item_addr_addr);
        TextView textViewDis = (TextView)itemLayout.findViewById(R.id.textView_detail_item_addr_district);
        TextView textViewProvince = (TextView)itemLayout.findViewById(R.id.textView_detail_item_addr_province);
        TextView textViewPostcode = (TextView)itemLayout.findViewById(R.id.textView_detail_item_addr_postcode);
        TextView textViewDefault = (TextView)itemLayout.findViewById(R.id.textView_detail_item_addr_default);

        textViewName.setText(resultList.get(i).getName());
        textViewTel.setText(resultList.get(i).getTel());
        textViewAddr.setText(resultList.get(i).getAddress());
        textViewDis.setText(resultList.get(i).getDistrict());
        textViewProvince.setText(resultList.get(i).getProvince());
        textViewPostcode.setText(resultList.get(i).getPostcode());

        if(resultList.get(i).getAddrDefault() == 1){
            textViewDefault.setVisibility(View.VISIBLE);
        }else{
            textViewDefault.setVisibility(View.GONE);
        }
        return itemLayout;
    }
}