package project.senior.holdit.event;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import project.senior.holdit.R;
import project.senior.holdit.adapter.ItemListGridAdapter;
import project.senior.holdit.adapter.RecyclerItemClickListener;
import project.senior.holdit.dialog.AlertDialogService;
import project.senior.holdit.manager.SharedPrefManager;
import project.senior.holdit.model.Item;
import project.senior.holdit.retrofit.ApiInterface;
import project.senior.holdit.retrofit.ConnectServer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemGridview extends AppCompatActivity implements View.OnClickListener{
    ItemListGridAdapter adapter;
    ArrayList<Item> itemList = new ArrayList<>();
    ArrayList<Item> list = new ArrayList<>();
    LinearLayout layout;
    EditText editText;
    RecyclerView recyclerView;
    int SORT = 1;
    int eventId;
    Map<String,Integer> map = new HashMap<>();
    String [] FILTER = {"ยอดนิยม" , "ล่าสุด", "ราคา (ต่ำ - สูง)" , "ราคา (สูง - ต่ำ)"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_gridview);
        setToolbar();
        setHashMap();
        editText = findViewById(R.id.edit_search);
        findViewById(R.id.button_filter).setOnClickListener(this);
        layout = findViewById(R.id.layout_noitem);
        Intent getIntent = getIntent();
        String getTitle = getIntent.getStringExtra("eventTitle");
        eventId = getIntent.getIntExtra("eventId",-1);
        setItemList(eventId);
        itemList = (ArrayList<Item>) getIntent().getSerializableExtra("itemList");
        list = (ArrayList<Item>) itemList.clone();
        TextView eventTitle = (TextView) findViewById(R.id.textView_griditem_event_title);
        eventTitle.setText(getTitle);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_itemgrid);
        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        setAdapter(itemList);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // do whatever
                        AlertDialogService a = new AlertDialogService(ItemGridview.this, getLayoutInflater());
                        a.showDialog(list, position);

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                setItemSearch(editable.toString());
            }
        });
    }

    private void setItemSearch(String find) {
        list.clear();
        for (Item i : itemList) {
            String str = i.getItemName() + i.getItemDesc();
            if (str.toLowerCase().indexOf(find.toLowerCase().trim()) != -1) {
                list.add(i);
            }
        }
        setAdapter(list);
    }

    private void setAdapter(ArrayList<Item> list) {
        if (list.size() > 0) {
            adapter = new ItemListGridAdapter(this, list);
            recyclerView.setAdapter(adapter);
            recyclerView.setVisibility(View.VISIBLE);
            layout.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);
        }
    }

    private void setHashMap(){
        map.put("ยอดนิยม",1);
        map.put("ล่าสุด",2);
        map.put("ราคา (ต่ำ - สูง)",3);
        map.put("ราคา (สูง - ต่ำ)",4);
    }
    public void setItemList(int eventID) {
        final ApiInterface apiService = ConnectServer.getClient().create(ApiInterface.class);
        String uid = SharedPrefManager.getInstance(ItemGridview.this).getUser().getUserId();
        Call<ArrayList<Item>> call = apiService.readitem(eventID, uid , SORT);
        call.enqueue(new Callback<ArrayList<Item>>() {
                         @Override
                         public void onResponse(Call<ArrayList<Item>> call, Response<ArrayList<Item>> response) {
                             itemList = response.body();
                             setItemSearch(editText.getText().toString());
                         }

                         @Override
                         public void onFailure(Call<ArrayList<Item>> call, Throwable t) {
                             Toast.makeText(ItemGridview.this, "Refresh Fail...", Toast.LENGTH_SHORT).show();
                         }
                     }

        );

    }
    private void filter() {
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(ItemGridview.this);

        builder.setSingleChoiceItems(FILTER, SORT-1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                String selected = FILTER[i];
                SORT = map.get(selected);
                setItemList(eventId);
                dialog.dismiss();
            }
        });
        builder.create();

        builder.show();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_grid);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_filter:
                filter();
        }
    }


}
