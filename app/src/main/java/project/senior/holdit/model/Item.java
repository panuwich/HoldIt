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

    @SerializedName("user_email")
    @Expose
    private String userEmail;

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

    public Item(int itemId, int eventId, String userEmail, String itemName, String itemPrice, String itemPreRate, String itemTranRate, String itemDesc, String itemImg1, String itemImg2, String itemImg3) {
        this.itemId = itemId;
        this.eventId = eventId;
        this.userEmail = userEmail;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemPreRate = itemPreRate;
        this.itemTranRate = itemTranRate;
        this.itemDesc = itemDesc;
        this.itemImg1 = itemImg1;
        this.itemImg2 = itemImg2;
        this.itemImg3 = itemImg3;
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

    public String getUserEmail() {
        return userEmail;
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
