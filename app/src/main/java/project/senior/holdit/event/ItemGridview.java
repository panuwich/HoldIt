package project.senior.holdit.event;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import project.senior.holdit.R;
import project.senior.holdit.adapter.ItemListGridAdapter;
import project.senior.holdit.adapter.RecyclerItemClickListener;
import project.senior.holdit.dialog.AlertDialogService;
import project.senior.holdit.model.Item;

public class ItemGridview extends AppCompatActivity {

    ArrayList<Item> itemList = new ArrayList<>();
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_gridview);
        setToolbar();

        Intent getIntent = getIntent();
        String getTitle = getIntent.getStringExtra("eventTitle");
        itemList = (ArrayList<Item>) getIntent().getSerializableExtra("itemList");
        TextView eventTitle = (TextView)findViewById(R.id.textView_griditem_event_title);
        eventTitle.setText(getTitle);

        recyclerView = (RecyclerView)findViewById(R.id.recycler_itemgrid);
        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        ItemListGridAdapter adapter = new ItemListGridAdapter(this, itemList);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
                        AlertDialogService a = new AlertDialogService(ItemGridview.this,getLayoutInflater());
                        a.showDialog(itemList,position);

                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void setToolbar(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_grid);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
    }


}
