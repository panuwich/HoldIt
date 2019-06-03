package project.senior.holdit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BarChartReport {
    @SerializedName("total")
    @Expose
    int total;

    @SerializedName("year")
    @Expose
    String year;

    @SerializedName("month")
    @Expose
    String month;

    public BarChartReport(int total, String year, String month) {
        this.total = total;
        this.year = year;
        this.month = month;
    }

    public int getTotal() {
        return total;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }


}
