package project.senior.holdit.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import project.senior.holdit.MainActivity;
import project.senior.holdit.Ordering;
import project.senior.holdit.R;
import project.senior.holdit.adapter.SliderAdapter;
import project.senior.holdit.login_signup.Login;
import project.senior.holdit.manager.SharedPrefManager;
import project.senior.holdit.model.Item;
import project.senior.holdit.model.ResponseModel;
import project.senior.holdit.model.User;
import project.senior.holdit.retrofit.ApiInterface;
import project.senior.holdit.retrofit.ConnectServer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlertDialogService {
    LinearLayout dotLayout;
    Context context;
    LayoutInflater inflater;
    Item item;
    int rate = 1;
    public String track = null;
    public AlertDialogService(Context context, LayoutInflater inflat) {
        this.context = context;
        this.inflater = inflat;
    }


    public AlertDialogService(Context context) {
        this.context = context;
    }

    public void showDialog(ArrayList<Item> itemList, int position) {
        item = itemList.get(position);
        AlertDialog.Builder builder =
                new AlertDialog.Builder(context);

        View v = inflater.inflate(R.layout.detail_dialog_select_item, null);
        builder.setView(v);
        ViewPager viewPager = (ViewPager) v.findViewById(R.id.viewpager_dialog_item_image);
        dotLayout = (LinearLayout) v.findViewById(R.id.dot);

        final ArrayList<String> url = new ArrayList<>();
        url.add(itemList.get(position).getItemImg1());
        url.add(itemList.get(position).getItemImg2());
        url.add(itemList.get(position).getItemImg3());
        for (int i = 0; i < 3; i++) {
            url.remove("");
        }
        SliderAdapter sliderAdapter = new SliderAdapter(context, url, true);
        viewPager.setAdapter(sliderAdapter);
        addDotsIndicator(0, url.size(), context);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addDotsIndicator(position, url.size(), context);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new
                ColorDrawable(Color.TRANSPARENT));

        final Button button_decrease = (Button) v.findViewById(R.id.button_dialog_decrease);
        final Button button_increase = (Button) v.findViewById(R.id.button_dialog_increase);
        final Button button_cancel = (Button) v.findViewById(R.id.button_dialog_cancel);
        final Button button_contract = (Button) v.findViewById(R.id.button_dialog_contact);

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
        textView_total.setText("" + (price + tran_rate + pre_rate));

        button_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int numOfitem = Integer.parseInt(textView_num.getText().toString()) + 1;
                button_decrease.setEnabled(true);
                button_decrease.setBackgroundResource(R.drawable.ic_decrease);
                textView_num.setText("" + numOfitem);
                textView_price_num.setText("" + numOfitem);
                textView_pre_rate_num.setText("" + numOfitem);
                textView_tran_rate_num.setText("" + numOfitem);

                textView_total.setText("" + ((price * numOfitem) + (tran_rate * numOfitem) + (pre_rate * numOfitem)));
            }
        });
        button_decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!textView_num.getText().toString().equals("1")) {
                    int numOfitem = Integer.parseInt(textView_num.getText().toString()) - 1;
                    textView_num.setText("" + numOfitem);
                    textView_price_num.setText("" + numOfitem);
                    textView_pre_rate_num.setText("" + numOfitem);
                    textView_tran_rate_num.setText("" + numOfitem);
                    textView_total.setText("" + ((price * numOfitem) + (tran_rate * numOfitem) + (pre_rate * numOfitem)));
                    if (textView_num.getText().toString().equals("1")) {
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
                intent.putExtra("item", (Serializable) item);
                intent.putExtra("num", textView_num.getText().toString());
                intent.putExtra("total", textView_total.getText().toString());
                context.startActivity(intent);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void addDotsIndicator(int position, int size, Context context) {
        TextView[] mDots = new TextView[size];
        dotLayout.removeAllViews();
        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(context);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(context.getResources().getColor(R.color.colorTranparentWhite));
            dotLayout.addView(mDots[i]);
        }
        mDots[position].setTextColor(context.getResources().getColor(R.color.colorBlue));

    }

    public void showDialogLogOut(final FragmentActivity activity) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.log_out) + "?");
        builder.setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SharedPrefManager.getInstance(context).clear();
                context.startActivity(new Intent(context, Login.class));
                activity.finish();

            }
        });
        builder.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog.dismiss();
            }
        });
        builder.show();
    }

    public void showDialogSimple(String title) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ((Activity) context).finish();

            }
        });
        builder.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog.dismiss();
            }
        });
        builder.show();
    }

    public void dialogRating(final int orderId, final String seller) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(context);

        View view = inflater.inflate(R.layout.detail_dialog_rating, null);
        builder.setView(view);
        builder.show();

        final TextView textViewRating = view.findViewById(R.id.texตราดing);
        final ImageView img[] = new ImageView[5];
        Button button = view.findViewById(R.id.btn_accept_rating);
        for (int i = 0; i < 5; i++) {
            img[i] = (ImageView) view.findViewById(context.getResources()
                    .getIdentifier("imageView_star" + (i + 1), "id", context.getPackageName()));
            final int finalI = i;
            img[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (finalI == 0) {
                        rate = setStar(1, img);
                        textViewRating.setText(context.getString(R.string.rating_too_bad));
                    } else if (finalI == 1) {
                        rate = setStar(2, img);
                        textViewRating.setText(context.getString(R.string.rating_bad));
                    } else if (finalI == 2) {
                        rate = setStar(3, img);
                        textViewRating.setText(context.getString(R.string.rating_fair));
                    } else if (finalI == 3) {
                        rate = setStar(4, img);
                        textViewRating.setText(context.getString(R.string.rating_good));
                    } else {
                        rate = setStar(5, img);
                        textViewRating.setText(context.getString(R.string.rating_excellent));
                    }
                }
            });
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ApiInterface apiService = ConnectServer.getClient().create(ApiInterface.class);
                Call<ResponseModel> call = apiService.receiveorder(orderId, rate, seller);
                call.enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        Toast.makeText(context, response.body().getResponse(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("order", true);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {

                    }
                });
            }
        });

    }

    public void dialogInput(final int edit, final TextView textView , String preString) {

        final User user = SharedPrefManager.getInstance(context).getUser();
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(context);

        View view = inflater.inflate(R.layout.detail_dialog_input, null);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        final EditText editText = view.findViewById(R.id.editText_accept);
        editText.setText(preString);
        editText.setSelection(preString.length());
        Button button = view.findViewById(R.id.button_accept);
        final TextView textViewTitle = view.findViewById(R.id.textView_title_input);
        if (edit == 3){
            textViewTitle.setText("กรุณากรอกชื่อ");
        }else if(edit == 4){
            textViewTitle.setText("กรุณากรอกนามสกุล");
        }else{
            textViewTitle.setText("กรุณากรอกเบอร์โทร");
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ApiInterface apiService = ConnectServer.getClient().create(ApiInterface.class);
                boolean valid = false;
                if(edit == 3){
                    if (!editText.getText().toString().isEmpty()){
                        valid = true;
                        user.setUserFirstname(editText.getText().toString().toUpperCase());
                        setFirebase(user,editText.getText().toString().toUpperCase());
                    }
                }else if(edit == 4){
                    if (!editText.getText().toString().isEmpty()){
                        valid = true;
                        user.setUserLastname(editText.getText().toString().toUpperCase());
                    }
                }else if(edit == 5){
                    valid = isTelValid(editText.getText().toString());
                    if(valid){
                        user.setUserTel(editText.getText().toString());
                    }
                }
                SharedPrefManager.getInstance(context).saveUser(user);
                if (valid){
                    textView.setText(editText.getText().toString().toUpperCase());
                    Call<ResponseModel> call = apiService.updateuser(edit, user.getUserId(), editText.getText().toString().toUpperCase() , "");
                    call.enqueue(new Callback<ResponseModel>() {
                        @Override
                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                            Toast.makeText(context, response.body().getResponse(), Toast.LENGTH_SHORT).show();
                            if (response.body().isStatus()){
                                alertDialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseModel> call, Throwable t) {

                        }
                    });
                }else{
                    Toast.makeText(context, context.getString(R.string.toast_invalid_input), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    
    private void setFirebase(User user,final String text){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUserId());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("userFirstname", text.toUpperCase());
                dataSnapshot.getRef().updateChildren(map);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private int setStar(int ratestar, ImageView imageView[]) {
        for (int i = 0; i < 5; i++) {
            imageView[i].setImageResource(R.drawable.ic_star_grey);
        }
        for (int i = 0; i < ratestar; i++) {
            imageView[i].setImageResource(R.drawable.ic_star_yellow);
        }
        return ratestar;
    }

    private boolean isTelValid(String editTextTel) {
        String tel = editTextTel.trim();
        String expression = "\\d+";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(tel);
        return matcher.matches() && tel.length() == 10;
    }
}
