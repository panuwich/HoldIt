package project.senior.holdit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Item implements Serializable {
    @SerializedName("item_id")
    @Expose
    private int itemId;

    @SerializedName("event_id")
    @Expose
    private int eventId;

    @SerializedName("user_id")
    @Expose
    private String userId;

    @SerializedName("item_name")
    @Expose
    private String itemName;

    @SerializedName("item_price")
    @Expose
    private String itemPrice;

    @SerializedName("item_pre_rate")
    @Expose
    private String itemPreRate;

    @SerializedName("item_tran_rate")
    @Expose
    private String itemTranRate;

    @SerializedName("item_desc")
    @Expose
    private String itemDesc;

    @SerializedName("item_img1")
    @Expose
    private String itemImg1;

    @SerializedName("item_img2")
    @Expose
    private String itemImg2;

    @SerializedName("item_img3")
    @Expose
    private String itemImg3;

    @SerializedName("user_rating_score")
    @Expose
    private int userRateScore;

    @SerializedName("user_rating_vote")
    @Expose
    private int userRateVote;

    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("type")
    @Expose
    private int type;

    @SerializedName("event_title")
    @Expose
    private String eventName;

    public Item(int itemId, int eventId, String userId, String itemName, String itemPrice
            , String itemPreRate, String itemTranRate, String itemDesc
            , String itemImg1, String itemImg2, String itemImg3, int status, int type, String eventName) {
        this.itemId = itemId;
        this.eventId = eventId;
        this.userId = userId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemPreRate = itemPreRate;
        this.itemTranRate = itemTranRate;
        this.itemDesc = itemDesc;
        this.itemImg1 = itemImg1;
        this.itemImg2 = itemImg2;
        this.itemImg3 = itemImg3;
        this.status = status;
        this.type = type;
        this.eventName = eventName;
    }

    public Item(int itemId, int eventId, String userId, String itemName, String itemPrice, String itemPreRate, String itemTranRate
            , String itemDesc, String itemImg1, String itemImg2, String itemImg3, int status, String eventName) {
        this.itemId = itemId;
        this.eventId = eventId;
        this.userId = userId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemPreRate = itemPreRate;
        this.itemTranRate = itemTranRate;
        this.itemDesc = itemDesc;
        this.itemImg1 = itemImg1;
        this.itemImg2 = itemImg2;
        this.itemImg3 = itemImg3;
        this.status = status;
        this.eventName = eventName;
    }

    public Item(int itemId, int eventId, String userId, String itemName, String itemPrice, String itemPreRate, String itemTranRate
            , String itemDesc, String itemImg1, String itemImg2, String itemImg3, String eventName) {
        this.itemId = itemId;
        this.eventId = eventId;
        this.userId = userId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemPreRate = itemPreRate;
        this.itemTranRate = itemTranRate;
        this.itemDesc = itemDesc;
        this.itemImg1 = itemImg1;
        this.itemImg2 = itemImg2;
        this.itemImg3 = itemImg3;
        this.eventName = eventName;
    }

    public Item(int itemId, int eventId, String userId, String itemName, String itemPrice, String itemPreRate, String itemTranRate, String itemDesc, String itemImg1) {
        this.itemId = itemId;
        this.eventId = eventId;
        this.userId = userId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemPreRate = itemPreRate;
        this.itemTranRate = itemTranRate;
        this.itemDesc = itemDesc;
        this.itemImg1 = itemImg1;
    }

    public Item(int itemId, int eventId, String userId, String itemName, String itemPrice, String itemPreRate, String itemTranRate, String itemDesc, String itemImg1, String itemImg2, String itemImg3) {
        this.itemId = itemId;
        this.eventId = eventId;
        this.userId = userId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemPreRate = itemPreRate;
        this.itemTranRate = itemTranRate;
        this.itemDesc = itemDesc;
        this.itemImg1 = itemImg1;
        this.itemImg2 = itemImg2;
        this.itemImg3 = itemImg3;
    }

    public Item(int itemId, int eventId, String userId, String itemName, String itemPrice
            , String itemPreRate, String itemTranRate, String itemDesc, String itemImg1, String itemImg2, String itemImg3, int userRateScore, int userRateVote) {
        this.itemId = itemId;
        this.eventId = eventId;
        this.userId = userId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemPreRate = itemPreRate;
        this.itemTranRate = itemTranRate;
        this.itemDesc = itemDesc;
        this.itemImg1 = itemImg1;
        this.itemImg2 = itemImg2;
        this.itemImg3 = itemImg3;
        this.userRateScore = userRateScore;
        this.userRateVote = userRateVote;
    }

    public int getType() {
        return type;
    }

    public String getEventName() {
        return eventName;
    }

    public int getStatus() {
        return status;
    }

    public int getUserRateScore() {
        return userRateScore;
    }

    public int getUserRateVote() {
        return userRateVote;
    }

    public String getItemImg1() {
        return itemImg1;
    }

    public String getItemImg2() {
        return itemImg2;
    }

    public String getItemImg3() {
        return itemImg3;
    }

    public String getItemName() {
        return itemName;
    }

    public int getItemId() {
        return itemId;
    }

    public int getEventId() {
        return eventId;
    }

    public String getUserId() {
        return userId;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public String getItemPreRate() {
        return itemPreRate;
    }

    public String getItemTranRate() {
        return itemTranRate;
    }

    public String getItemDesc() {
        return itemDesc;
    }
}
