package project.senior.holdit.info;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;

import project.senior.holdit.R;
import project.senior.holdit.adapter.AddrListAdapter;
import project.senior.holdit.manager.SharedPrefManager;
import project.senior.holdit.model.Address;
import project.senior.holdit.retrofit.ApiInterface;
import project.senior.holdit.retrofit.ConnectServer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressSelect extends AppCompatActivity {
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_select);
        setToolbar();
        listView = (ListView)findViewById(R.id.listview_addr_select);
        LinearLayout linearLayoutAdd = (LinearLayout)findViewById(R.id.layout_addr_select_add);
        linearLayoutAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddressSelect.this,AddAddress.class));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        setAddrList();
    }
    public void setAddrList() {

        final ApiInterface apiService = ConnectServer.getClient().create(ApiInterface.class);
        String user_id = SharedPrefManager.getInstance(AddressSelect.this).getUser().getUserId();
        Call<ArrayList<Address>> call = apiService.readaddress(user_id);
        call.enqueue(new Callback<ArrayList<Address>>() {
                         @Override
                         public void onResponse(Call<ArrayList<Address>> call, Response<ArrayList<Address>> response) {
                             final ArrayList<Address> res = response.body();
                             AddrListAdapter addrListAdapter = new AddrListAdapter(AddressSelect.this
                                     ,R.layout.detail_item_addr,res);
                             listView.setAdapter(addrListAdapter);

                             final Intent getIntent = getIntent();
                             final Integer req = getIntent.getIntExtra("requestCode",-1);
                             listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                 @Override
                                 public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                     Address addr = (Address)res.get(i);
                                     if(req == 100){
                                         Intent returnIntent = new Intent();
                                         returnIntent.putExtra("addr",(Serializable) addr);
                                         setResult(Activity.RESULT_OK,returnIntent);
                                         finish();
                                     }else{
                                         Intent intent = new Intent(AddressSelect.this,AddAddress.class);
                                         intent.putExtra("addr",(Serializable) addr);
                                         intent.putExtra("requestCode",1);
                                         startActivity(intent);
                                     }
                                 }
                             });

                         }

                         @Override
                         public void onFailure(Call<ArrayList<Address>> call, Throwable t) {
                         }
                     }

        );

    }
    public void setToolbar(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_addr_select);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
