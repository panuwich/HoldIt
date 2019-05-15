package project.senior.holdit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseModel {
    @SerializedName("status")
    @Expose
    boolean status ;

    @SerializedName("response")
    @Expose
    String response ;

    @SerializedName("userid")
    @Expose
    String userId;

    public String getResponse() {
        return response;
    }

    public boolean getStatus() {
        return status;
    }

    public ResponseModel(boolean status, String response) {
        this.status = status;
        this.response = response;
    }

    public ResponseModel(boolean status, String response, String userId) {
        this.status = status;
        this.response = response;
        this.userId = userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isStatus() {
        return status;
    }
}
