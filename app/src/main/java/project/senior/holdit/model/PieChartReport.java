package project.senior.holdit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PieChartReport {
    @SerializedName("type")
    @Expose
    String nameType;
    @SerializedName("amount")
    @Expose
    int amount;

    @SerializedName("date_start")
    @Expose
    String dateStart;

    @SerializedName("date_stop")
    @Expose
    String dateStop;

    public PieChartReport(String nameType, int amount) {
        this.nameType = getType(Integer.parseInt(nameType));
        this.amount = amount;
    }

    public PieChartReport(String nameType, int amount, String dateStart, String dateStop) {
        this.nameType = nameType;
        this.amount = amount;
        this.dateStart = dateStart;
        this.dateStop = dateStop;
    }

    public String getDateStart() {
        return dateStart;
    }

    public String getDateStop() {
        return dateStop;
    }

    public String getNameType() {
        return nameType;
    }

    public int getAmount() {
        return amount;
    }

    public String getType(int type){
        switch (type){
            case 1:
                return "เสื้อผ้า";
            case 2:
                return "หนังสือ";
            case 3:
                return "รองเท้า";
            case 4:
                return "เครื่องสำอาง";
            case 5:
                return "กระเป๋า";
            case 6:
                return "เครื่องประดับ";
            case 7:
                return "กีฬา";
            case 8:
                return "อุปกรณ์ถ่ายภาพ";
            case 9:
                return "สื่อบันเทิง";
            case 10:
                return "อุปกรณ์เครื่องเขียน";
            case 11:
                return "อุปกรณ์ IT";
            case 12:
                return "อื่นๆ";
        }
        return null;
    }
}
