package project.senior.holdit.order;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;

import project.senior.holdit.R;
import project.senior.holdit.adapter.OrderAdapter;
import project.senior.holdit.manager.SharedPrefManager;
import project.senior.holdit.model.Order;
import project.senior.holdit.retrofit.ApiInterface;
import project.senior.holdit.retrofit.ConnectServer;
import project.senior.holdit.util.Validator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubTabSeller extends Fragment {
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
        View view = inflater.inflate(R.layout.activity_sub_tab_seller, container, false);
        listView = (ListView) view.findViewById(R.id.recycler_order_seller);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Order order = (Order) listView.getItemAtPosition(i);
                if (!Validator.isOrderSuccessOrCancelOrApprovedIssue(order)) {
                    Intent intent = new Intent(getContext(), MessageActivity.class);
                    intent.putExtra("order", ((Serializable) order));
                    intent.putExtra("userId", (order.getBuyerId()));
                    intent.putExtra("status", ( order.getStatus()));
                    intent.putExtra("orderId", (order.getId()));
                    startActivity(intent);
                }
            }
        });
        return view;
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
                    if (order.getSellerId().equals(user_id)) {
                        orders.add(order);
                    }
                }
                adapter = new OrderAdapter(getContext(), R.layout.detail_chat_list, orders, false);
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ArrayList<Order>> call, Throwable t) {

            }
        });
    }

}
