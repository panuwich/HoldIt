package project.senior.holdit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("user_email")
    @Expose
    private String userEmail;
    @SerializedName("user_firstname")
    @Expose
    private String userFirstname;
    @SerializedName("user_lastname")
    @Expose
    private String userLastname;
    @SerializedName("user_image")
    @Expose
    private String userImage;
    @SerializedName("user_status_login")
    @Expose
    private int userStatusLogin;
    @SerializedName("user_status_verified")
    @Expose
    private int userStatusVerified;
    @SerializedName("user_tel")
    @Expose
    private String userTel;
    @SerializedName("user_citizen")
    @Expose
    private String userCitizen;

    public User(String userId, String userEmail, String userFirstname, String userLastname
            , String userImage, int userStatus, int userStatusVerified
            , String userTel, String userCitizen) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.userFirstname = userFirstname;
        this.userLastname = userLastname;
        this.userImage = userImage;
        this.userStatusLogin = userStatus;
        this.userStatusVerified = userStatusVerified;
        this.userTel = userTel;
        this.userCitizen = userCitizen;
    }

    public User(String userId, String userEmail, String userFirstname, String userLastname
            , String userImage, int userStatusVerified, String userTel, String userCitizen) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.userFirstname = userFirstname;
        this.userLastname = userLastname;
        this.userImage = userImage;
        this.userStatusVerified = userStatusVerified;
        this.userTel = userTel;
        this.userCitizen = userCitizen;
    }

    public User() {
    }

    public String getUserId() {
        return userId;
    }
    public int getUserStatusVerified() {
        return userStatusVerified;
    }

    public String getUserCitizen() {
        return userCitizen;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserFirstname() {
        return userFirstname;
    }

    public void setUserFirstname(String userFirstname) {
        this.userFirstname = userFirstname;
    }

    public String getUserLastname() {
        return userLastname;
    }

    public void setUserLastname(String userLastname) {
        this.userLastname = userLastname;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public int getUserStatusLogin() {
        return userStatusLogin;
    }

    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }
}
