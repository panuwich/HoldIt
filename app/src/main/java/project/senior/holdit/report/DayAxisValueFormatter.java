package project.senior.holdit.report;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;

import project.senior.holdit.model.BarChartReport;

public class DayAxisValueFormatter extends ValueFormatter
{
    ArrayList<BarChartReport> list = new ArrayList<>();
    private final String[] mMonths = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    private final BarLineChartBase<?> chart;

    public DayAxisValueFormatter(BarLineChartBase<?> chart ,ArrayList<BarChartReport> list ) {
        this.chart = chart;
        this.list = (ArrayList<BarChartReport>)list.clone();
    }

    @Override
    public String getFormattedValue(float value) {
        int index = (int)value;
        System.out.println("index " + index + "size" + list.size());
        if(index < 0 || index >= list.size()){
            return "";
        }
        return "" + mMonths[Integer.parseInt(list.get(index).getMonth())] + " " + list.get(index).getYear();

    }
}