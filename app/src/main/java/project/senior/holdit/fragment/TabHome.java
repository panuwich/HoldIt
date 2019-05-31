package project.senior.holdit.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import project.senior.holdit.R;
import project.senior.holdit.adapter.EventListAdapter;
import project.senior.holdit.event.DescriptionEvent;
import project.senior.holdit.model.Event;
import project.senior.holdit.retrofit.ApiInterface;
import project.senior.holdit.retrofit.ConnectServer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TabHome extends Fragment {
    SwipeRefreshLayout swipeRefreshLayout;
    Runnable runable;
    Handler handle;
    EventListAdapter mAdapter;
    OnFragmentInteractionListener mListener;
    ArrayList<Event> eventList = new ArrayList<>();
    ArrayList<Event> eventListFilter = new ArrayList<>();
    ListView lv;
    Button buttonGO ;
    LinearLayout layoutFilter;
    ArrayList<String> filterCon = new ArrayList<>();
    ArrayList<String> preFilterCon = new ArrayList<>();
    HashMap<String, String> hashMap = new HashMap<String, String>();
    HashMap<Integer, Integer> hashMapImg0 = new HashMap<Integer, Integer>();
    HashMap<Integer, Integer> hashMapImg1 = new HashMap<Integer, Integer>();
    CircleImageView[] circleImageViews = new CircleImageView[13];
    TextView[] textViews = new TextView[13];
    ImageView imgViewResult;
    ProgressDialog dialog;
    Button buttonFilter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_home, container, false);
        setHashMap();
        setHashMapImg();
        buttonGO = (Button)view.findViewById(R.id.button_home_filter_go);
        filterCon = (ArrayList<String>) preFilterCon.clone();

        for (int i = 1; i <= 12; i++) {
            int id1 = getResources().getIdentifier("imageView_filter_" + i, "id", getContext().getPackageName());
            circleImageViews[i] = (CircleImageView) view.findViewById(id1);
            int id2 = getResources().getIdentifier("textView_filter_" + i, "id", getContext().getPackageName());
            textViews[i] = (TextView) view.findViewById(id2);
            final int finalI = i;

            circleImageViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String resultClick = hashMap.get(textViews[finalI].getText().toString().replace("\n", ""));
                    if (filterCon.contains(resultClick)) {
                        filterCon.remove((String) resultClick);
                        circleImageViews[finalI].setImageResource(hashMapImg0.get(Integer.parseInt(resultClick)));
                        textViews[finalI].setTextColor(getResources().getColor(R.color.colorBlack));
                    } else {
                        filterCon.add(resultClick);
                        circleImageViews[finalI].setImageResource(hashMapImg1.get(Integer.parseInt(resultClick)));
                        textViews[finalI].setTextColor(getResources().getColor(R.color.colorPurple));
                    }

                }
            });
        }
        //setFilter
        setFilter(filterCon);
        imgViewResult = (ImageView) view.findViewById(R.id.img_home_result);
        layoutFilter = (LinearLayout) view.findViewById(R.id.layout_home_filter);
        buttonFilter = (Button) getActivity().findViewById(R.id.button_main_filter);
        buttonFilter.setVisibility(View.VISIBLE);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        lv = (ListView) view.findViewById(R.id.listview_item);


        buttonFilter.setBackgroundResource(R.drawable.ic_tune_white);

        buttonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (layoutFilter.getVisibility() == View.GONE) {
                    lv.setEnabled(false);

                    buttonFilter.setBackgroundResource(R.drawable.ic_tune_purple);
                    layoutFilter.setVisibility(View.VISIBLE);
                    //
                } else {
                    lv.setEnabled(true);

                    buttonFilter.setBackgroundResource(R.drawable.ic_tune_white);
                    layoutFilter.setVisibility(View.GONE);

                    //filterCon = (ArrayList<String>) preFilterCon.clone();
                    //setFilter(filterCon);
                    buttonGO.performClick();
                }
            }
        });
        if (eventList.size() == 0 ) {
            dialog = new ProgressDialog(getContext(),R.style.MyAlertDialogStyle);
            dialog.setMessage("Loading");
            dialog.show();
            setEventList();
        } else{
            resultFromFilter();
        }
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialog.show();
                handle = new Handler();
                runable = new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                };
                handle.postDelayed(runable, 500);

                Intent intent = new Intent(getContext(), DescriptionEvent.class);
                intent.putExtra("eventTitle", eventListFilter.get(i).getTitle());
                intent.putExtra("eventLocation", eventListFilter.get(i).getLocation());
                intent.putExtra("eventDesc", eventListFilter.get(i).getDesc());
                String urlLogo = "http://pilot.cp.su.ac.th/usr/u07580319/holdit/pics/event/"+eventListFilter.get(i).getEventImgLogo();
                intent.putExtra("eventLogo", urlLogo);
                String urlCover = "http://pilot.cp.su.ac.th/usr/u07580319/holdit/pics/event/"+eventListFilter.get(i).getEventImgCover();
                intent.putExtra("eventCover", urlCover);
                String dateEnd = eventListFilter.get(i).getDateEnd();
                String getOnlyDateEnd = dateEnd.substring(8);
                String getOnlyMonth = getMonth(dateEnd.substring(5,7));
                String getOnlyYear = ""+(Integer.parseInt(dateEnd.substring(0,4))+543);
                String date = "วันนี้ - " + getOnlyDateEnd + " " + getOnlyMonth + " " +getOnlyYear;
                intent.putExtra("eventDate", date);
                intent.putExtra("eventID",eventListFilter.get(i).getEventId());
                startActivity(intent);

            }
        });

        buttonGO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultFromFilter();
                lv.setEnabled(true);

                final Animation downtotop = AnimationUtils.loadAnimation(getContext(), R.anim.anim_silde_downtotop);
                final Animation rotateltr = AnimationUtils.loadAnimation(getContext(), R.anim.anim_rotate_ltr);
                preFilterCon = (ArrayList<String>) filterCon.clone();
                buttonFilter.startAnimation(rotateltr);
                buttonFilter.setBackgroundResource(R.drawable.ic_tune_white);
                layoutFilter.startAnimation(downtotop);
                layoutFilter.setVisibility(View.GONE);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                handle = new Handler();
                runable = new Runnable() {

                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        handle.removeCallbacks(runable); // stop runable.
                        setEventList();
                        swipeRefreshLayout.setRefreshing(false);
                        if (eventList.size() != 0)
                            mAdapter.notifyDataSetChanged();
                    }
                };
                handle.postDelayed(runable, 1000); // delay 3 s.
            }
        });


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void setEventList() {

        final ApiInterface apiService = ConnectServer.getClient().create(ApiInterface.class);
        Call<ArrayList<Event>> call = apiService.readevent();
        call.enqueue(new Callback<ArrayList<Event>>() {
                         @Override
                         public void onResponse(Call<ArrayList<Event>> call, Response<ArrayList<Event>> response) {
                             ArrayList<Event> res = response.body();
                             eventList.clear();

                             if (res.size() != 0) {
                                 imgViewResult.setVisibility(View.GONE);
                                 for (Event r : res) {
                                     eventList.add(new Event(r.getEventId(), r.getEventImgCover(), r.getEventImgLogo()
                                             , r.getLocation(), r.getDateStart(), r.getDateEnd() , r.getTitle()
                                             , r.getDesc() , r.getCategory()));
                                 }

                             } else {
                                 imgViewResult.setVisibility(View.VISIBLE);
                                 Snackbar.make(getView(), "No event now", Snackbar.LENGTH_LONG)
                                         .setAction("Action", null).show();
                             }
                             eventListFilter = (ArrayList<Event>) eventList.clone();
                             resultFromFilter();
                             dialog.dismiss();
                         }

                         @Override
                         public void onFailure(Call<ArrayList<Event>> call, Throwable t) {
                             Toast.makeText(getContext(), "Refresh Fail...", Toast.LENGTH_SHORT).show();
                         }
                     }

        );

    }

    void resultFromFilter(){
        ArrayList<Event> event = new ArrayList<>();
        if(filterCon.size() == 0){
            event = eventList;
        }else{
            for(int i = 0 ;  i < eventList.size() ; i++){
                for(int j = 0 ; j < filterCon.size() ; j++){
                    if(eventList.get(i).getCategory().equals(filterCon.get(j))){
                        event.add(eventList.get(i));
                    }
                }
            }
        }
        eventListFilter = event;
        if(event.size() == 0){
            imgViewResult.setVisibility(View.VISIBLE);
        }else{
            imgViewResult.setVisibility(View.GONE);
        }
        mAdapter = new EventListAdapter(
                getContext(),
                R.layout.detail_box_event,
                event
        );
        lv.setAdapter(mAdapter);
    }
    public void setVisFilter(){
        if(layoutFilter.getVisibility() == View.VISIBLE){
            layoutFilter.setVisibility(View.GONE);
            buttonFilter.setBackgroundResource(R.drawable.ic_tune_white);
            lv.setEnabled(true);
            resultFromFilter();
        }
    }
    public void setFilter(ArrayList<String> a) {
        for (int i = 1; i <= 12; i++) {
            circleImageViews[i].setImageResource(hashMapImg0.get(i));
            textViews[i].setTextColor(getResources().getColor(R.color.colorBlack));
        }
        for (int i = 1; i <= a.size(); i++) {
            System.out.println("INDEX : " + a.size());
            circleImageViews[Integer.parseInt(a.get(i-1))].setImageResource(hashMapImg1.get(Integer.parseInt(a.get(i-1))));
            textViews[Integer.parseInt(a.get(i-1))].setTextColor(getResources().getColor(R.color.colorPurple));
        }
    }

    public void setHashMap() {
        hashMap.put("เสื้อผ้า", "1");
        hashMap.put("หนังสือ", "2");
        hashMap.put("รองเท้า", "3");
        hashMap.put("เครื่องสำอาง", "4");
        hashMap.put("กระเป๋า", "5");
        hashMap.put("เครื่องประดับ", "6");
        hashMap.put("กีฬา", "7");
        hashMap.put("อุปกรณ์ถ่ายภาพ", "8");
        hashMap.put("สื่อบันเทิง", "9");
        hashMap.put("อุปกรณ์เครื่องเขียน", "10");
        hashMap.put("อุปกรณ์ IT", "11");
        hashMap.put("อื่นๆ", "12");
    }

    void setHashMapImg(){
        Resources res = getResources();
        for (int i = 1 ; i <= 12 ;i++){
            String mDrawableName = "filter" + i +"0";
            int resID = res.getIdentifier(mDrawableName , "drawable", getContext().getPackageName());
            hashMapImg0.put(i,resID);
        }
        for (int i = 1 ; i <= 12 ;i++){
            String mDrawableName = "filter" + i +"1";
            int resID = res.getIdentifier(mDrawableName , "drawable", getContext().getPackageName());
            hashMapImg1.put(i,resID);
        }
    }

    String getMonth(String month){
        switch (month){
            case "01":
                return "มกราคม";
            case "02":
                return "กุมภาพันธ์";
            case "03":
                return "มีนาคม";
            case "04":
                return "เมษายม";
            case "05":
                return "พฤษภาคม";
            case "06":
                return "มิถุนายม";
            case "07":
                return "กรกฎาคม";
            case "08":
                return "สิงหาคม";
            case "09":
                return "กันยายน";
            case "10":
                return "ตุลาคม";
            case "11":
                return "พฤศจิกายน";
            case "12":
                return "ธันวาคม";
            default: break;
        }
        return "";
    }
}
