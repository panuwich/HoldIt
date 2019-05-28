package project.senior.holdit.event;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import project.senior.holdit.R;
import project.senior.holdit.adapter.RecyclerItemClickListener;
import project.senior.holdit.adapter.itemListDescAdapter;
import project.senior.holdit.dialog.AlertDialogService;
import project.senior.holdit.manager.SharedPrefManager;
import project.senior.holdit.model.Item;
import project.senior.holdit.retrofit.ApiInterface;
import project.senior.holdit.retrofit.ConnectServer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DescriptionEvent extends AppCompatActivity {
    TextView[] mDots;
    LinearLayout dotLayout;
    ViewPager viewPager;
    RecyclerView recyclerView;
    ArrayList<Item> itemList = new ArrayList<>();
    ImageView imgViewNoItem;

    @Override
    protected void onStart() {
        super.onStart();

        Intent getIntent = getIntent();
        final int eventID = getIntent.getIntExtra("eventID",-1);
        setItemList(eventID);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_event);

        setToolbar();

        int status = SharedPrefManager.getInstance(DescriptionEvent.this).getUser().getUserStatusVerified();;
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.coltoolbar_item_grid_event);

        Intent getIntent = getIntent();
        final String eventTitle = getIntent.getStringExtra("eventTitle");
        String eventDesc = getIntent.getStringExtra("eventDesc");
        String eventLocation = getIntent.getStringExtra("eventLocation");
        String eventLogo = getIntent.getStringExtra("eventLogo");
        String eventCover = getIntent.getStringExtra("eventCover");
        String eventDate = getIntent.getStringExtra("eventDate");
        final int eventID = getIntent.getIntExtra("eventID",-1);
        collapsingToolbarLayout.setTitle(getTitle());
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        collapsingToolbarLayout.setTitle(eventTitle);

        collapsingToolbarLayout.setContentScrim(getResources().getDrawable( R.color.colorBlackOp ));
        recyclerView = (RecyclerView)findViewById(R.id.recycler_itemdesc);
        //GridView gridView = (GridView)findViewById(R.id.gridview_item);

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.layout_itemdesc_create);
        ImageView imageViewLogo = (ImageView)findViewById(R.id.imageView_itemdesc_logo);
        TextView textViewLocation = (TextView)findViewById(R.id.textView_itemdesc_location);
        TextView textViewEventName = (TextView)findViewById(R.id.textView_itemdesc_event_name);
        TextView textViewDate = (TextView)findViewById(R.id.textView_itemdesc_date);
        TextView textViewDesc = (TextView)findViewById(R.id.textView_itemdesc_desc);
        imgViewNoItem = (ImageView)findViewById(R.id.imgView_itemdesc_noitem);
        try {
            Drawable cover = drawableFromUrl(eventCover);
            collapsingToolbarLayout.setBackground(cover);
        } catch (IOException e) {
            e.printStackTrace();
        }
        textViewDate.setText(eventDate);
        textViewEventName.setText(eventTitle);
        textViewLocation.setText(eventLocation);
        textViewDesc.setText(eventDesc);
        Picasso.get().load(eventLogo).into(imageViewLogo);
        if(status==0 || status==-1){
            linearLayout.setVisibility(View.GONE);
        }
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DescriptionEvent.this,CreateItem.class);
                intent.putExtra("eventID",eventID);
                startActivity(intent);
            }
        });
        int numberOfColumns = 1;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
                        AlertDialogService a = new AlertDialogService(DescriptionEvent.this,getLayoutInflater());
                        a.showDialog(itemList,position);

                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DescriptionEvent.this,ItemGridview.class);
                intent.putExtra("eventTitle",eventTitle);
                intent.putExtra("eventId",eventID);
                intent.putExtra("itemList",itemList);
                startActivity(intent);
            }
        });
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

    public  Drawable drawableFromUrl(String req) throws IOException {
        URL url = new URL(req);
        Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        Drawable d = new BitmapDrawable(getResources(), bmp);
        return d;
    }
    public void setItemList(int eventID) {
        final ApiInterface apiService = ConnectServer.getClient().create(ApiInterface.class);
        String uid  = SharedPrefManager.getInstance(DescriptionEvent.this).getUser().getUserId();
        Call<ArrayList<Item>> call = apiService.readitem(eventID,uid,1);
        call.enqueue(new Callback<ArrayList<Item>>() {
                         @Override
                         public void onResponse(Call<ArrayList<Item>> call, Response<ArrayList<Item>> response) {
                             ArrayList<Item> res = response.body();
                             itemList.clear();

                             if (res.size() != 0) {
                                 imgViewNoItem.setVisibility(View.GONE);
                                 for (Item r : res) {
                                     itemList.add(new Item(r.getItemId(),r.getEventId(),r.getUserId(),r.getItemName()
                                             ,r.getItemPrice(),r.getItemPreRate(),r.getItemTranRate(),r.getItemDesc()
                                             ,r.getItemImg1(),r.getItemImg2(),r.getItemImg3(),r.getUserRateScore(),r.getUserRateVote()));
                                 }
                                 itemListDescAdapter adapter = new itemListDescAdapter(DescriptionEvent.this, itemList);
                                 recyclerView.setAdapter(adapter);
                             } else {
                                 imgViewNoItem.setVisibility(View.VISIBLE);
                             }
                         }

                         @Override
                         public void onFailure(Call<ArrayList<Item>> call, Throwable t) {
                             Toast.makeText(DescriptionEvent.this, "Refresh Fail...", Toast.LENGTH_SHORT).show();
                         }
                     }

        );

    }

}
