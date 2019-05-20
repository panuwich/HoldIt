package project.senior.holdit.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

import project.senior.holdit.R;
import project.senior.holdit.adapter.FindingAdapter;
import project.senior.holdit.adapter.RecyclerItemClickListener;
import project.senior.holdit.finding.AddFindingActivity;
import project.senior.holdit.finding.PreOrder;
import project.senior.holdit.manager.SharedPrefManager;
import project.senior.holdit.model.Finding;
import project.senior.holdit.model.ResponseModel;
import project.senior.holdit.model.User;
import project.senior.holdit.retrofit.ApiInterface;
import project.senior.holdit.retrofit.ConnectServer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TabPreOrder extends Fragment {

    User user;
    RecyclerView recyclerView;
    ArrayList<Finding> list;
    @Override
    public void onStart() {
        super.onStart();
        setRecyclerView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        user = SharedPrefManager.getInstance(getContext()).getUser();
        final Button buttonFilter = (Button)getActivity().findViewById(R.id.button_main_filter);
        buttonFilter.setVisibility(View.GONE);
        View view =  inflater.inflate(R.layout.fragment_tab_pre_order, container, false);
        recyclerView = view.findViewById(R.id.recycler_finding);
        FloatingActionButton actionButton = view.findViewById(R.id.floating_finding);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
                        Finding finding = list.get(position);
                        if (user.getUserStatusVerified()==0){
                            Toast.makeText(getContext(), "กรุณายืนยันตัวตนก่อนใช้งานระบบนี้", Toast.LENGTH_SHORT).show();
                        }else if(finding.getUserId().equals(SharedPrefManager.getInstance(getContext()).getUser().getUserId())){
                            showDialogdeleteFinding(finding);
                        }else{
                            Intent intent = new Intent(getContext(), PreOrder.class);
                            intent.putExtra("finding", (Serializable) finding);
                            startActivity(intent);
                        }

                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddFindingActivity.class));
            }
        });
        return view;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void showDialogdeleteFinding(final Finding finding) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getContext());
        builder.setTitle("ยกเลิกรายการ");
        builder.setMessage("คุณต้องการยกเลิกประกาศหา " + finding.getName() + " ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                del(finding);
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
private void del(Finding finding){
        ApiInterface api = ConnectServer.getClient().create(ApiInterface.class);
        Call<ResponseModel> call = api.delfinding(finding.getId());
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                Toast.makeText(getContext(), response.body().getResponse(), Toast.LENGTH_SHORT).show();
                if (response.body().isStatus()){
                    setRecyclerView();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {

            }
        });
}
    private void setRecyclerView(){
        ApiInterface api = ConnectServer.getClient().create(ApiInterface.class);
        Call<ArrayList<Finding>> call = api.readfinding();
        call.enqueue(new Callback<ArrayList<Finding>>() {
            @Override
            public void onResponse(Call<ArrayList<Finding>> call, Response<ArrayList<Finding>> response) {
                list = response.body();
                FindingAdapter adapter = new FindingAdapter(getContext(),list);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ArrayList<Finding>> call, Throwable t) {

            }
        });
    }
}
