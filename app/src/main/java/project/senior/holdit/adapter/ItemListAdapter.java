package project.senior.holdit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import project.senior.holdit.model.Item;

public class ItemListAdapter extends BaseAdapter {
    Context mContext;
    private int mLayoutResId;
    private ArrayList<Item> resultList;
    public ItemListAdapter(Context context, int mLayoutResId, ArrayList<Item> resultList){
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
        return itemLayout;
    }
}