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

    public boolean isStatus() {
        return status;
    }
}
