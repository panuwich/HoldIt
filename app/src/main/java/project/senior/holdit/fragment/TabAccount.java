package project.senior.holdit.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import project.senior.holdit.R;
import project.senior.holdit.dialog.AlertDialogService;
import project.senior.holdit.info.AddressSelect;
import project.senior.holdit.info.PrivateInfo;
import project.senior.holdit.manager.SharedPrefManager;
import project.senior.holdit.model.User;
import project.senior.holdit.settingitem.SettingList;
import project.senior.holdit.verify.Verification;


public class TabAccount extends Fragment implements View.OnClickListener {
    TextView textViewEmail;
    TextView textViewName;
    CircleImageView imageViewProfile;

    @Override
    public void onStart() {
        super.onStart();
        setAccount();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_account, container, false);

        textViewEmail = (TextView) view.findViewById(R.id.textView_account_email);
        textViewName = (TextView) view.findViewById(R.id.textView_account_name);
        imageViewProfile = (CircleImageView) view.findViewById(R.id.imageView_account_profile);
        final Button buttonFilter = (Button) getActivity().findViewById(R.id.button_main_filter);
        buttonFilter.setVisibility(View.GONE);

        view.findViewById(R.id.layout_account_logout).setOnClickListener(this);
        view.findViewById(R.id.layout_account_verification).setOnClickListener(this);
        view.findViewById(R.id.layout_account_addr).setOnClickListener(this);
        view.findViewById(R.id.layout_account_privateinfo).setOnClickListener(this);
        view.findViewById(R.id.layout_account_report).setOnClickListener(this);
        view.findViewById(R.id.layout_account_setting).setOnClickListener(this);

        return view;
    }

    public void setAccount() {

        User user = SharedPrefManager.getInstance(getContext()).getUser();
        final String email = user.getUserEmail();
        final String firstname = user.getUserFirstname();
        final String lastname = user.getUserLastname();
        final String picture = user.getUserImage();
        if (picture.isEmpty()) {
            imageViewProfile.setImageResource(R.drawable.user);
        } else {
            String url = "http://pilot.cp.su.ac.th/usr/u07580319/holdit/pics/profile/" + picture;
            Picasso.get().load(url).into(imageViewProfile);
            //System.out.println(url);
        }

        String name = firstname + " " + lastname;
        textViewName.setText(name);
        textViewEmail.setText(email);
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void showDialogLogout() {
        AlertDialogService alertDialogService = new AlertDialogService(getContext());
        alertDialogService.showDialogLogOut(getActivity());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_account_logout:
                showDialogLogout();
                break;
            case R.id.layout_account_verification:
                startActivity(new Intent(getContext(), Verification.class));
                break;
            case R.id.layout_account_addr:
                startActivity(new Intent(getContext(), AddressSelect.class));
                break;
            case R.id.layout_account_privateinfo:
                startActivity(new Intent(getContext(), PrivateInfo.class));
                break;
            case R.id.layout_account_report:
                Toast.makeText(getContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.layout_account_setting:
                startActivity(new Intent(getContext(), SettingList.class));
                break;
        }
    }
}
