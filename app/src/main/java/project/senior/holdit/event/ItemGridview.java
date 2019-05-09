package project.senior.holdit.event;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import project.senior.holdit.R;
import project.senior.holdit.adapter.ItemListGridAdapter;
import project.senior.holdit.adapter.RecyclerItemClickListener;
import project.senior.holdit.dialog.AlertDialogService;
import project.senior.holdit.model.Item;

public class ItemGridview extends AppCompatActivity {
    TextView[] mDots;
    LinearLayout dotLayout;
    ArrayList<Item> itemList = new ArrayList<>();
    ViewPager viewPager;
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
        //GridView gridView = (GridView)findViewById(R.id.gridview_item);
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

    public void addDotsIndicator(int position,int size){
        mDots = new TextView[size];
        dotLayout.removeAllViews();
        for(int i = 0 ; i < mDots.length ; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorTranparentWhite));
            dotLayout.addView(mDots[i]);
        }
        mDots[position].setTextColor(getResources().getColor(R.color.colorBlue));

    }

    private Drawable resize(Drawable image) {
        Bitmap b = ((BitmapDrawable)image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 150, 150, false);
        return new BitmapDrawable(getResources(), bitmapResized);
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
