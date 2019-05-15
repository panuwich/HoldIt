package project.senior.holdit.fragment.order;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.Serializable;
import java.util.ArrayList;

import project.senior.holdit.R;
import project.senior.holdit.adapter.OrderAdapter;
import project.senior.holdit.manager.SharedPrefManager;
import project.senior.holdit.model.Order;
import project.senior.holdit.model.Token;
import project.senior.holdit.retrofit.ApiInterface;
import project.senior.holdit.retrofit.ConnectServer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubTabBuyer extends Fragment {
    ListView listView;
    OrderAdapter adapter;

    @Override
    public void onStart() {
        super.onStart();
        readOrder();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_sub_tab_buyer, container, false);
        listView = (ListView) view.findViewById(R.id.recycler_order_buyer);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (((Order) listView.getItemAtPosition(i)).getStatus() != -1) {
                    Intent intent = new Intent(getContext(), MessageActivity.class);
                    Order order = (Order) listView.getItemAtPosition(i);
                    intent.putExtra("order", ((Serializable) order));
                    intent.putExtra("userId", (order.getSellerId()));
                    intent.putExtra("status", ( order.getStatus()));
                    intent.putExtra("orderId", (order.getId()));
                    startActivity(intent);
                }
            }
        });
        updateToken(FirebaseInstanceId.getInstance().getToken());
        return view;
    }

    private void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(SharedPrefManager.getInstance(getContext()).getUser().getUserId()).setValue(token);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void readOrder() {
        final ApiInterface apiService = ConnectServer.getClient().create(ApiInterface.class);
        final String user_id = SharedPrefManager.getInstance(getContext()).getUser().getUserId();
        Call<ArrayList<Order>> call = apiService.readorder(user_id);
        call.enqueue(new Callback<ArrayList<Order>>() {
            @Override
            public void onResponse(Call<ArrayList<Order>> call, Response<ArrayList<Order>> response) {
                ArrayList<Order> res = response.body();
                ArrayList<Order> orders = new ArrayList<>();
                for (Order order : res) {
                    if (order.getBuyerId().equals(user_id)) {
                        orders.add(order);
                    }
                }
                adapter = new OrderAdapter(getContext(), R.layout.detail_chat_list, orders, true);
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ArrayList<Order>> call, Throwable t) {

            }
        });
    }
}
