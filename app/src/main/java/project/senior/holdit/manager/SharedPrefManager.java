package project.senior.holdit.manager;

import android.content.Context;
import android.content.SharedPreferences;

import project.senior.holdit.model.User;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "prefs";

    private static SharedPrefManager mInstance;
    private Context mCtx;

    private SharedPrefManager(Context mCtx){
        this.mCtx = mCtx;
    }

    public static synchronized SharedPrefManager getInstance(Context mCtx){
        if (mInstance == null){
            mInstance = new SharedPrefManager(mCtx);
        }
        return mInstance;
    }

    public void saveUser(User user){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_id", user.getUserId());
        editor.putString("user_email", user.getUserEmail());
        editor.putString("user_firstname", user.getUserFirstname());
        editor.putString("user_lastname", user.getUserLastname());
        editor.putString("user_image", user.getUserImage());
        editor.putInt("user_status_verified", user.getUserStatusVerified());
        editor.putString("user_tel", user.getUserTel());
        editor.putString("user_citizen", user.getUserCitizen());
        editor.apply();

    }

    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString("user_id",null) != null;
    }

    public User getUser(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getString("user_id",null),
                sharedPreferences.getString("user_email",null),
                sharedPreferences.getString("user_firstname",null),
                sharedPreferences.getString("user_lastname",null),
                sharedPreferences.getString("user_image",null),
                sharedPreferences.getInt("user_status_verified",-1),
                sharedPreferences.getString("user_tel",null),
                sharedPreferences.getString("user_citizen",null)

        );
    }

    public void clear(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("user_id");
        editor.remove("user_email");
        editor.remove("user_firstname");
        editor.remove("user_lastname");
        editor.remove("user_image");
        editor.remove("user_status_verified");
        editor.remove("user_tel");
        editor.remove("user_citizen");
        editor.apply();
    }
}