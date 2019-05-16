package project.senior.holdit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Order implements Serializable {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("seller_id")
    @Expose
    private String sellerId;

    @SerializedName("buyer_id")
    @Expose
    private String buyerId;

    @SerializedName("item_id")
    @Expose
    private int itemId;

    @SerializedName("addr_id")
    @Expose
    private int addrId;

    @SerializedName("amount")
    @Expose
    private int amount;

    @SerializedName("total")
    @Expose
    private int total;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("track")
    @Expose
    private String track;

    public Order(int id, String sellerId, String buyerId, int itemId, int addrId, int amount, int total, String date, int status) {
        this.id = id;
        this.sellerId = sellerId;
        this.buyerId = buyerId;
        this.itemId = itemId;
        this.addrId = addrId;
        this.amount = amount;
        this.total = total;
        this.date = date;
        this.status = status;
    }

    public Order(int id, String sellerId, String buyerId, int itemId, int addrId, int amount, int total, String date, int status, String track) {
        this.id = id;
        this.sellerId = sellerId;
        this.buyerId = buyerId;
        this.itemId = itemId;
        this.addrId = addrId;
        this.amount = amount;
        this.total = total;
        this.date = date;
        this.status = status;
        this.track = track;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public int getId() {
        return id;
    }

    public String getSellerId() {
        return sellerId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public int getItemId() {
        return itemId;
    }

    public int getAddrId() {
        return addrId;
    }

    public int getAmount() {
        return amount;
    }

    public int getTotal() {
        return total;
    }

    public String getDate() {
        return date;
    }

    public int getStatus() {
        return status;
    }
}
