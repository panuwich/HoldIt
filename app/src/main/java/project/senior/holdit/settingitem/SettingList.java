package project.senior.holdit.settingitem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import project.senior.holdit.R;
import project.senior.holdit.adapter.RecyclerItemClickListener;
import project.senior.holdit.adapter.SettingAdapter;
import project.senior.holdit.manager.SharedPrefManager;
import project.senior.holdit.model.Item;
import project.senior.holdit.retrofit.ApiInterface;
import project.senior.holdit.retrofit.ConnectServer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingList extends AppCompatActivity  {
    RecyclerView recyclerView;
    List<Item> itemList;
    LinearLayout layout;
    @Override
    protected void onStart() {
        super.onStart();
        setRecyclerView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_list);
        setToolbar();
        layout = findViewById(R.id.layout_noitem);
        recyclerView = findViewById(R.id.recycler_setting);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SettingList.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(SettingList.this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent intent = new Intent(SettingList.this,SettingItem.class);
                        intent.putExtra("item",(Serializable)itemList.get(position));
                        startActivity(intent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }

    private void setRecyclerView(){
        ApiInterface api = ConnectServer.getClient().create(ApiInterface.class);
        Call<ArrayList<Item>> call = api.readitembyuser(SharedPrefManager.getInstance(SettingList.this).getUser().getUserId());
        call.enqueue(new Callback<ArrayList<Item>>() {
            @Override
            public void onResponse(Call<ArrayList<Item>> call, Response<ArrayList<Item>> response) {
                itemList = response.body();
                if (itemList.size() > 0){
                    SettingAdapter settingAdapter = new SettingAdapter(SettingList.this,itemList);
                    recyclerView.setAdapter(settingAdapter);
                    recyclerView.setVisibility(View.VISIBLE);
                    layout.setVisibility(View.GONE);
                }else{
                    layout.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<ArrayList<Item>> call, Throwable t) {

            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_setting);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
    }

}
