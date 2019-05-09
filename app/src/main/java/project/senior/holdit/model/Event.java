package project.senior.holdit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Event {
    @SerializedName("event_id")
    @Expose
    private int eventId;
    @SerializedName("event_img_cover")
    @Expose
    private String eventImgCover;
    @SerializedName("event_img_logo")
    @Expose
    private String eventImgLogo;
    @SerializedName("event_location")
    @Expose
    private String location;
    @SerializedName("event_date_start")
    @Expose
    private String dateStart;
    @SerializedName("event_date_end")
    @Expose
    private String dateEnd;
    @SerializedName("event_title")
    @Expose
    private String title;
    @SerializedName("event_desc")
    @Expose
    private String desc;
    @SerializedName("event_category")
    @Expose
    private String category;

    public Event(int eventId, String eventImgCover, String eventImgLogo, String location, String dateStart, String dateEnd, String title, String desc, String category) {
        this.eventId = eventId;
        this.eventImgCover = eventImgCover;
        this.eventImgLogo = eventImgLogo;
        this.location = location;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.title = title;
        this.desc = desc;
        this.category = category;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public String getCategory() {
        return category;
    }

    public String getLocation() {

        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }



    public int getEventId() {
        return eventId;
    }



    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventImgCover() {
        return eventImgCover;
    }

    public void setEventImgCover(String eventImgCover) {
        this.eventImgCover = eventImgCover;
    }

    public String getEventImgLogo() {
        return eventImgLogo;
    }

    public void setEventImgLogo(String eventImgLogo) {
        this.eventImgLogo = eventImgLogo;
    }


}
