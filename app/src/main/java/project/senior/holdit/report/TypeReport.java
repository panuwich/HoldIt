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
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import project.senior.holdit.R;
import project.senior.holdit.manager.SharedPrefManager;
import project.senior.holdit.model.PieChartReport;
import project.senior.holdit.model.User;
import project.senior.holdit.retrofit.ApiInterface;
import project.senior.holdit.retrofit.ConnectServer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TypeReport extends Fragment implements View.OnClickListener {
    EditText editTextStart,editTextStop;
    private DatePickerDialog.OnDateSetListener mDateSetListenerStart;
    private DatePickerDialog.OnDateSetListener mDateSetListenerStop;
    PieChart chart;
    TextView textViewNodata;
    ArrayList<PieChartReport> list = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_type_report, container, false);
        editTextStart = view.findViewById(R.id.date_start);
        editTextStop = view.findViewById(R.id.date_stop);
        view.findViewById(R.id.layout_start).setOnClickListener(this);
        view.findViewById(R.id.layout_stop).setOnClickListener(this);
        view.findViewById(R.id.button_search).setOnClickListener(this);
        textViewNodata = view.findViewById(R.id.textView_nodata);
        chart = view.findViewById(R.id.pie_chart);
        Date dstart = new Date();
        dstart.setYear(dstart.getYear()-1);
        Date dstop = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        editTextStart.setText(dateFormat.format(dstart));
        editTextStop.setText(dateFormat.format(dstop));
        mDateSetListenerStart = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = year + "-" + oneDigit(month) + "-" + oneDigit(day);
                editTextStart.setText(date);
            }
        };
        mDateSetListenerStop = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = year + "-" + oneDigit(month) + "-" + oneDigit(day);
                editTextStop.setText(date);
            }
        };
        view.findViewById(R.id.button_search).performClick();
        return view;
    }

    private void datePicker(DatePickerDialog.OnDateSetListener listener,EditText editText){
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(editText.getText().toString());
            int year = date.getYear()+1900;
            int month = date.getMonth();
            int day = date.getDate();
            System.out.println(year + " " + month + " " + day);
            DatePickerDialog dialog = new DatePickerDialog(
                    getContext(),
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    listener,
                    year,month,day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layout_start:
                datePicker(mDateSetListenerStart,editTextStart);
                break;
            case R.id.layout_stop:
                datePicker(mDateSetListenerStop,editTextStop);
                break;
            case R.id.button_search:
                valid();
                break;
        }
    }

    private void valid() {
        String start = editTextStart.getText().toString();
        String stop = editTextStop.getText().toString();
        try {
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(start);
            Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(stop);
            if (date1.after(date2)){
                Toast.makeText(getContext(), "วันสิ้นสุดต้องมาก่อนวันเริ่มต้น", Toast.LENGTH_SHORT).show();
            }else{
                setListReport();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    String oneDigit(int num){
        if(num < 10){
            return "0" + num;
        }
        return "" + num;
    }
    private void setListReport(){
        ApiInterface api = ConnectServer.getClient().create(ApiInterface.class);
        User user = SharedPrefManager.getInstance(getContext()).getUser();
        Call<ArrayList<PieChartReport>> call = api.piechart(user.getUserId()
                ,editTextStart.getText().toString(),editTextStop.getText().toString());
        call.enqueue(new Callback<ArrayList<PieChartReport>>() {
            @Override
            public void onResponse(Call<ArrayList<PieChartReport>> call, Response<ArrayList<PieChartReport>> response) {
                list.clear();
                if (response.body().size() == 0){
                    textViewNodata.setVisibility(View.VISIBLE);
                    chart.setVisibility(View.GONE);
                }else{
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    for(PieChartReport p : response.body()){
                        list.add(new PieChartReport(p.getNameType(),p.getAmount()));

                    }
                    textViewNodata.setVisibility(View.GONE);
                    chart.setVisibility(View.VISIBLE);
                }
                setChart();
            }

            @Override
            public void onFailure(Call<ArrayList<PieChartReport>> call, Throwable t) {

            }
        });
    }

    private void setChart(){
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (PieChartReport p : list) {
            entries.add(new PieEntry(p.getAmount(), p.getNameType() + " (" + +p.getAmount() + ") "));
        }
        PieDataSet dataset = new PieDataSet(entries, "");
        dataset.setSelectionShift(10);
        dataset.setValueTextSize(14);
        dataset.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData data = new PieData(dataset);
        chart.setData(data);
        chart.setHoleRadius(30);
        chart.setTransparentCircleRadius(40);
        chart.animateY(1000);
        Description desc = new Description();
        desc.setText("");
        chart.setDescription(desc);
        chart.setEntryLabelColor(getResources().getColor(R.color.colorBlack));
        dataset.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataset.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataset.setValueLinePart1Length(0.5f);
        dataset.setValueLinePart2Length(0.5f);
        chart.setUsePercentValues(true);
        dataset.setValueFormatter(new PercentFormatter());
        chart.invalidate();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
