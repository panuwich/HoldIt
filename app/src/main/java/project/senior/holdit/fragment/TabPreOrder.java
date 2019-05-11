package project.senior.holdit.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import project.senior.holdit.R;


public class TabPreOrder extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final Button buttonFilter = (Button)getActivity().findViewById(R.id.button_main_filter);
        buttonFilter.setVisibility(View.GONE);
        return inflater.inflate(R.layout.fragment_tab_pre_order, container, false);
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
