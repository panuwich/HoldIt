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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import project.senior.holdit.R;
import project.senior.holdit.manager.SharedPrefManager;
import project.senior.holdit.model.BarChartReport;
import project.senior.holdit.model.User;
import project.senior.holdit.retrofit.ApiInterface;
import project.senior.holdit.retrofit.ConnectServer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SalesReport extends Fragment implements View.OnClickListener {
    EditText editTextStart, editTextStop;
    private DatePickerDialog.OnDateSetListener mDateSetListenerStart;
    private DatePickerDialog.OnDateSetListener mDateSetListenerStop;
    TextView textViewNodata;
    ArrayList<BarChartReport> list = new ArrayList<>();
    BarChart chart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sales_report, container, false);
        editTextStart = view.findViewById(R.id.date_start);
        editTextStop = view.findViewById(R.id.date_stop);
        chart = view.findViewById(R.id.bar_chart);
        view.findViewById(R.id.layout_start).setOnClickListener(this);
        view.findViewById(R.id.layout_stop).setOnClickListener(this);
        view.findViewById(R.id.button_search).setOnClickListener(this);
        textViewNodata = view.findViewById(R.id.textView_nodata);
        Date dstart = new Date();
        dstart.setYear(dstart.getYear() - 1);
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

    private void valid() {
        String start = editTextStart.getText().toString();
        String stop = editTextStop.getText().toString();
        try {
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(start);
            Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(stop);
            if (date1.after(date2)) {
                Toast.makeText(getContext(), "วันสิ้นสุดต้องมาก่อนวันเริ่มต้น", Toast.LENGTH_SHORT).show();
            } else {
                setListReport();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    String oneDigit(int num) {
        if (num < 10) {
            return "0" + num;
        }
        return "" + num;
    }

    private void setListReport() {
        ApiInterface api = ConnectServer.getClient().create(ApiInterface.class);
        User user = SharedPrefManager.getInstance(getContext()).getUser();
        Call<ArrayList<BarChartReport>> call = api.barchart(user.getUserId()
                , editTextStart.getText().toString(), editTextStop.getText().toString());
        call.enqueue(new Callback<ArrayList<BarChartReport>>() {
            @Override
            public void onResponse(Call<ArrayList<BarChartReport>> call, Response<ArrayList<BarChartReport>> response) {
                list.clear();
                if (response.body().size() == 0) {
                    textViewNodata.setVisibility(View.VISIBLE);
                    chart.setVisibility(View.GONE);
                } else {
                    for (BarChartReport p : response.body()) {
                        list.add(new BarChartReport(p.getTotal(), p.getYear(), p.getMonth()));
                    }
                    textViewNodata.setVisibility(View.GONE);
                    chart.setVisibility(View.VISIBLE);
                }
                setChart();
            }

            @Override
            public void onFailure(Call<ArrayList<BarChartReport>> call, Throwable t) {

            }
        });
    }

    private void setChart() {
        final List<BarEntry> entries = new ArrayList<>();
        int index = 0;
        for (BarChartReport b : list) {
            entries.add(new BarEntry((float) index, (float) b.getTotal()));
            index++;
        }

        BarDataSet dataset = new BarDataSet(entries, "Income");
        dataset.setValueTextSize(8);
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);

        BarData data = new BarData(dataset);
        data.setBarWidth(0.8f);
        ValueFormatter xAxisFormatter = new DayAxisValueFormatter(chart,list);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);
        chart.setData(data);
        chart.getXAxis().setLabelRotatioน่านgle(0);

        chart.animateXY(500, 1200);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setPinchZoom(false);
        Description desc = new Description();
        desc.setText("");
        chart.setDescription(desc);
        chart.invalidate();
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
