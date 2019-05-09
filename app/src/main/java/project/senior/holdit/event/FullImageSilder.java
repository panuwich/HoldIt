package project.senior.holdit.event;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ooo.oxo.library.widget.PullBackLayout;
import project.senior.holdit.R;
import project.senior.holdit.adapter.SliderAdapter;

public class FullImageSilder extends AppCompatActivity implements PullBackLayout.Callback{
    TextView mDots;
    LinearLayout dotLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image_silder);

        ViewPager viewPager = (ViewPager)findViewById(R.id.viewpager_full_image);
        dotLayout = (LinearLayout)findViewById(R.id.dot_full) ;
        PullBackLayout puller = (PullBackLayout)findViewById(R.id.puller);
        puller.setCallback(this);
        Intent intent = getIntent();
        final ArrayList<String> url = intent.getStringArrayListExtra("urlArr");
        SliderAdapter sliderAdapter = new SliderAdapter(FullImageSilder.this,url,false);
        viewPager.setAdapter(sliderAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addDotsIndicator(position,url.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        addDotsIndicator(0,url.size());
    }
    public void addDotsIndicator(int position,int size){
        mDots = new TextView(getApplicationContext());
        dotLayout.removeAllViews();
        mDots.setText("" + (position+1) + "/" + size);
        mDots.setTextSize(20);
        dotLayout.addView(mDots);


    }
    @Override
    public void onPullStart() {

        supportFinishAfterTransition();
    }

    @Override
    public void onPull(float v) {
    }

    @Override
    public void onPullCancel() {

    }

    @Override
    public void onPullComplete() {
        supportFinishAfterTransition();
    }
}
