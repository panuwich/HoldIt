package project.senior.holdit.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

import project.senior.holdit.Ordering;
import project.senior.holdit.R;
import project.senior.holdit.adapter.SliderAdapter;
import project.senior.holdit.login_signup.Login;
import project.senior.holdit.manager.SharedPrefManager;
import project.senior.holdit.model.Item;

public class AlertDialogService {
    LinearLayout dotLayout;
    Context context;
    LayoutInflater inflater;
    Item item;
    public AlertDialogService(Context context, LayoutInflater inflat) {
        this.context = context;
        this.inflater = inflat;
    }


    public AlertDialogService(Context context) {
        this.context = context;
    }

    public void showDialog(ArrayList<Item> itemList, int position){
        item = itemList.get(position);
        System.out.println("ITEM : " + item.getUserId());
        AlertDialog.Builder builder =
                new AlertDialog.Builder(context);

        View v = inflater.inflate(R.layout.detail_dialog_select_item, null);
        builder.setView(v);
        ViewPager viewPager = (ViewPager)v.findViewById(R.id.viewpager_dialog_item_image);
        dotLayout = (LinearLayout)v.findViewById(R.id.dot) ;

        final ArrayList<String> url = new ArrayList<>();
        url.add(itemList.get(position).getItemImg1());
        url.add(itemList.get(position).getItemImg2());
        url.add(itemList.get(position).getItemImg3());
        for ( int i = 0 ; i < 3 ; i++ ){
            url.remove("");
        }
        SliderAdapter sliderAdapter = new SliderAdapter(context,url,true);
        viewPager.setAdapter(sliderAdapter);
        addDotsIndicator(0,url.size(),context);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addDotsIndicator(position,url.size(),context);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new
                ColorDrawable(Color.TRANSPARENT));

        final Button button_decrease = (Button)v.findViewById(R.id.button_dialog_decrease);
        final Button button_increase = (Button)v.findViewById(R.id.button_dialog_increase);
        final Button button_cancel = (Button)v.findViewById(R.id.button_dialog_cancel);
        final Button button_contract = (Button)v.findViewById(R.id.button_dialog_contact);

        final TextView textView_name = (TextView) v.findViewById(R.id.textView_dialog_item_name);
        final TextView textView_desc = (TextView) v.findViewById(R.id.textView_dialog_item_desc);

        final TextView textView_num = (TextView) v.findViewById(R.id.textView_dialog_num);
        final TextView textView_price = (TextView) v.findViewById(R.id.textView_dialog_item_price);
        final TextView textView_total = (TextView) v.findViewById(R.id.textView_dialog_item_total_cost);
        final TextView textView_tran_rate = (TextView) v.findViewById(R.id.textView_dialog_item_transport_rate);
        final TextView textView_pre_rate = (TextView) v.findViewById(R.id.textView_dialog_item_pre_rate);

        final TextView textView_price_num = (TextView) v.findViewById(R.id.textView_dialog_item_price_num);
        final TextView textView_pre_rate_num = (TextView) v.findViewById(R.id.textView_dialog_item_pre_rate_num);
        final TextView textView_tran_rate_num = (TextView) v.findViewById(R.id.textView_dialog_item_transport_rate_num);

        textView_name.setText(itemList.get(position).getItemName());
        textView_desc.setText(itemList.get(position).getItemDesc());
        textView_price.setText(itemList.get(position).getItemPrice());
        textView_pre_rate.setText(itemList.get(position).getItemPreRate());
        textView_tran_rate.setText(itemList.get(position).getItemTranRate());

        final int price = Integer.parseInt(textView_price.getText().toString());
        final int tran_rate = Integer.parseInt(textView_tran_rate.getText().toString());
        final int pre_rate = Integer.parseInt(textView_pre_rate.getText().toString());
        textView_total.setText(""+(price+tran_rate+pre_rate));

        button_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int numOfitem = Integer.parseInt(textView_num.getText().toString())+1;
                button_decrease.setEnabled(true);
                button_decrease.setBackgroundResource(R.drawable.ic_decrease);
                textView_num.setText(""+numOfitem);
                textView_price_num.setText(""+numOfitem);
                textView_pre_rate_num.setText(""+numOfitem);
                textView_tran_rate_num.setText(""+numOfitem);

                textView_total.setText(""+((price*numOfitem) + (tran_rate * numOfitem) + (pre_rate*numOfitem)));
            }
        });
        button_decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!textView_num.getText().toString().equals("1")){
                    int numOfitem = Integer.parseInt(textView_num.getText().toString())-1;
                    textView_num.setText(""+numOfitem);
                    textView_price_num.setText(""+numOfitem);
                    textView_pre_rate_num.setText(""+numOfitem);
                    textView_tran_rate_num.setText(""+numOfitem);
                    textView_total.setText(""+((price*numOfitem) + (tran_rate * numOfitem) + (pre_rate*numOfitem)));
                    if(textView_num.getText().toString().equals("1")){
                        button_decrease.setEnabled(false);
                        button_decrease.setBackgroundResource(R.drawable.ic_decrease_disable);
                    }

                }
            }
        });
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        button_contract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Ordering.class);
                intent.putExtra("item",(Serializable) item);
                intent.putExtra("num",textView_num.getText().toString());
                intent.putExtra("total",textView_total.getText().toString());
                context.startActivity(intent);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void addDotsIndicator(int position,int size,Context context){
        TextView[]mDots = new TextView[size];
        dotLayout.removeAllViews();
        for(int i = 0 ; i < mDots.length ; i++) {
            mDots[i] = new TextView(context);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(context.getResources().getColor(R.color.colorTranparentWhite));
            dotLayout.addView(mDots[i]);
        }
        mDots[position].setTextColor(context.getResources().getColor(R.color.colorBlue));

    }

    public void showDialogLogOut(final FragmentActivity activity){
        AlertDialog.Builder builder =
                new AlertDialog.Builder(context);
        builder.setTitle("Log out?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SharedPrefManager.getInstance(context).clear();
                context.startActivity(new Intent(context, Login.class));
                activity.finish();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog.dismiss();
            }
        });
        builder.show();
    }

    public void showDialogSimple(String title){
        AlertDialog.Builder builder =
                new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ((Activity)context).finish();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog.dismiss();
            }
        });
        builder.show();
    }
}
