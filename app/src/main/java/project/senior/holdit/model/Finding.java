package project.senior.holdit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Finding implements Serializable {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("descript")
    @Expose
    private String descript;

    @SerializedName("location")
    @Expose
    private String location;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("amount")
    @Expose
    private int amount;

    @SerializedName("addr_id")
    @Expose
    private int addrId;

    @SerializedName("user_id")
    @Expose
    private String userId;

    public Finding() {
    }

    public Finding(int id, String name, String descript, String location, String image, int amount, int addrId) {
        this.id = id;
        this.name = name;
        this.descript = descript;
        this.location = location;
        this.image = image;
        this.amount = amount;
        this.addrId = addrId;
    }

    public Finding(int id, String name, String descript, String location, String image, int amount, int addrId, String userId) {
        this.id = id;
        this.name = name;
        this.descript = descript;
        this.location = location;
        this.image = image;
        this.amount = amount;
        this.addrId = addrId;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public int getAddrId() {
        return addrId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescript() {
        return descript;
    }

    public String getLocation() {
        return location;
    }

    public String getImage() {
        return image;
    }

    public int getAmount() {
        return amount;
    }
}
