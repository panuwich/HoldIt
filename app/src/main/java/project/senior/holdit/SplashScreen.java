package project.senior.holdit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import project.senior.holdit.login_signup.Login;
import project.senior.holdit.manager.SharedPrefManager;

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);


            Thread timer = new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(1200);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        boolean booleanValue = SharedPrefManager.getInstance(SplashScreen.this).isLoggedIn();
                        if(booleanValue){
                            startActivity(new Intent(SplashScreen.this,MainActivity.class));

                        }else{
                            startActivity(new Intent(SplashScreen.this,Login.class));
                        }
                        finish();

                    }
                }
            };
            timer.start();
    }
}
