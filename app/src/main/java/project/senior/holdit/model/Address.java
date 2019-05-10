package project.senior.holdit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Address implements Serializable{
    @SerializedName("id")
    @Expose
    int id;
    @SerializedName("name")
    @Expose
    String name;
    @SerializedName("tel")
    @Expose
    String tel;
    @SerializedName("address")
    @Expose
    String address;
    @SerializedName("province")
    @Expose
    String province;
    @SerializedName("district")
    @Expose
    String district;
    @SerializedName("postcode")
    @Expose
    String postcode;
    @SerializedName("addr_default")
    @Expose
    int addrDefault;

    public Address(int id, String name, String tel, String address, String province, String district, String postcode, int addrDefault) {
        this.id = id;
        this.name = name;
        this.tel = tel;
        this.address = address;
        this.province = province;
        this.district = district;
        this.postcode = postcode;
        this.addrDefault = addrDefault;
    }

    public Address(int id, String name, String tel, String address, String province, String district, String postcode) {
        this.id = id;
        this.name = name;
        this.tel = tel;
        this.address = address;
        this.province = province;
        this.district = district;
        this.postcode = postcode;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTel() {
        return tel;
    }

    public String getAddress() {
        return address;
    }

    public String getProvince() {
        return province;
    }

    public String getDistrict() {
        return district;
    }

    public String getPostcode() {
        return postcode;
    }

    public int getAddrDefault() {
        return addrDefault;
    }
}
