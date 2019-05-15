package project.senior.holdit.payment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;

import project.senior.holdit.R;
import project.senior.holdit.adapter.PaymentAdapter;
import project.senior.holdit.adapter.RecyclerItemClickListener;
import project.senior.holdit.model.Bank;
import project.senior.holdit.model.Order;

public class SelectPaymentActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Bank> arrayList = new ArrayList<>();
    Order order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_payment);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_itemgrid_select_payment);
        int numberOfColumns = 4;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        setBank();
        setToolbar();
        order = (Order)getIntent().getSerializableExtra("order");
        PaymentAdapter paymentAdapter = new PaymentAdapter(this, arrayList);
        recyclerView.setAdapter(paymentAdapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
                        Intent intent = new Intent(SelectPaymentActivity.this, PaymentLogin.class);
                        intent.putExtra("bank",arrayList.get(position).getName());
                        intent.putExtra("order", (Serializable) order);
                        startActivity(intent);

                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }

    void setBank(){
        arrayList.add(new Bank("ktb","https://is2-ssl.mzstatic.com/image/thumb/Purple124/v4/b6/e4/e6/b6e4e69f-746f-34c8-89e8-ea910aafabfd/AppIcon-0-1x_U007emarketing-0-0-sRGB-85-220-0-10.png/246x0w.jpg"));
        arrayList.add(new Bank("scb","https://www.diamondgrains.com/images/SCB.png"));
        arrayList.add(new Bank("ks","https://is4-ssl.mzstatic.com/image/thumb/Purple113/v4/14/42/08/144208c0-5fab-499c-7052-52d8c7510d1f/AppIcons-0-1x_U007emarketing-0-85-220-3.png/246x0w.jpg"));
        arrayList.add(new Bank("ksk","https://www.asiancasinotop10.com/th/wp-content/uploads/sites/2/2017/09/kbank-icon.png"));
        arrayList.add(new Bank("tnc","https://is3-ssl.mzstatic.com/image/thumb/Purple123/v4/f7/92/24/f792245f-5148-9974-c307-8d6bf466b86d/source/512x512bb.jpg"));
        arrayList.add(new Bank("tmb","https://pbs.twimg.com/profile_images/936198120873930753/iyEeh0ga_400x400.jpg"));
        arrayList.add(new Bank("os","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSD0YK1qMWMsmiyw3uEHmv0ZLrD0yJGdUTrLDp5BxYRKlA3yqjq"));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    public void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_create);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
    }
}
