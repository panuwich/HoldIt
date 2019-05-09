package project.senior.holdit.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import project.senior.holdit.R;
import project.senior.holdit.dialog.AlertDialogService;
import project.senior.holdit.event.AddressSelect;
import project.senior.holdit.manager.SharedPrefManager;
import project.senior.holdit.model.User;
import project.senior.holdit.verify.Verification;


public class TabAccount extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TabAccount() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_tab_account, container, false);

        final Button buttonFilter = (Button)getActivity().findViewById(R.id.button_main_filter);
        buttonFilter.setVisibility(View.GONE);
        setAccount(view);

        view.findViewById(R.id.layout_account_logout).setOnClickListener(this);
        view.findViewById(R.id.layout_account_verification).setOnClickListener(this);
        view.findViewById(R.id.layout_account_addr).setOnClickListener(this);

        return view;
}

    public void setAccount(View view){
        TextView textViewEmail = (TextView)view.findViewById(R.id.textView_account_email);
        TextView textViewName = (TextView)view.findViewById(R.id.textView_account_name);
        CircleImageView imageViewProfile = (CircleImageView)view.findViewById(R.id.imageView_account_profile);

        User user = SharedPrefManager.getInstance(getContext()).getUser();
        final String email = user.getUserEmail();
        final String firstname = user.getUserFirstname();
        final String lastname = user.getUserLastname();
        final String picture = user.getUserImage();
        if(picture.isEmpty()){
            imageViewProfile.setImageResource(R.drawable.user);
        }else{
            String url = "http://pilot.cp.su.ac.th/usr/u07580319/holdit/pics/profile/"+picture;
            Picasso.get().load(url).into(imageViewProfile);
            //System.out.println(url);
        }

        String name = firstname + " " + lastname;
        textViewName.setText(name);
        textViewEmail.setText(email);
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public void showDialogLogout(){
        AlertDialogService alertDialogService = new AlertDialogService(getContext());
        alertDialogService.showDialogLogOut(getActivity());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layout_account_logout:
                showDialogLogout();
                break;
            case R.id.layout_account_verification:
                startActivity(new Intent(getContext(), Verification.class));
                break;
            case R.id.layout_account_addr:
                startActivity(new Intent(getContext(), AddressSelect.class));
                break;

        }
    }
}
