package project.senior.holdit.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import project.senior.holdit.event.FullImageSilder;
import project.senior.holdit.R;

public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<String> url;
    boolean canClick = false;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    public SliderAdapter(Context context, ArrayList<String> url, boolean canClick) {
        this.context = context;
        this.url = url;
        this.canClick = canClick;
    }

    @Override
    public int getCount() {
        return url.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.detail_item_image, container, false);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageView_silde_item);
        Picasso.get().load("http://pilot.cp.su.ac.th/usr/u07580319/holdit/pics/item/"+url.get(position)).into(imageView);
        container.addView(view);
        if (canClick)
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        else
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canClick) {
                    Intent intent = new Intent(view.getContext(), FullImageSilder.class);
                    intent.putStringArrayListExtra("urlArr", url);
                    view.getContext().startActivity(intent);
                }

            }

        });
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
