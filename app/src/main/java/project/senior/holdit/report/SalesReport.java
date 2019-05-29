package project.senior.holdit.report;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

import project.senior.holdit.R;


public class SalesReport extends Fragment implements View.OnClickListener{
    EditText editTextStart,editTextStop;
    private DatePickerDialog.OnDateSetListener mDateSetListenerStart;
    private DatePickerDialog.OnDateSetListener mDateSetListenerStop;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =   inflater.inflate(R.layout.fragment_sales_report, container, false);
        editTextStart = view.findViewById(R.id.date_start);
        editTextStop = view.findViewById(R.id.date_stop);
        view.findViewById(R.id.layout_start).setOnClickListener(this);
        view.findViewById(R.id.layout_stop).setOnClickListener(this);

        mDateSetListenerStart = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = year + "-" + month + "-" + day;
                editTextStart.setText(date);
            }
        };
        mDateSetListenerStop = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = year + "-" + month + "-" + day;
                editTextStop.setText(date);
            }
        };
        return view;
    }

    private void datePicker(DatePickerDialog.OnDateSetListener listener){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                getContext(),
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                listener,
                year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layout_start:
                datePicker(mDateSetListenerStart);
                break;
            case R.id.layout_stop:
                datePicker(mDateSetListenerStop);
                break;
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
