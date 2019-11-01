package project.senior.holdit.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import project.senior.holdit.R;
import project.senior.holdit.SplashScreen;
import project.senior.holdit.manager.SharedPrefManager;
import project.senior.holdit.model.User;
import project.senior.holdit.retrofit.ApiInterface;
import project.senior.holdit.retrofit.ConnectServer;
import project.senior.holdit.verify.Verification;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationService extends Service {

    Timer timer;
    TimerTask timerTask;
    String TAG = "Timers";
    int Your_X_SECS = 10;
    String CHANNEL_ID = "notiChannel";
    NotificationManagerCompat notificationManager;
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);

        startTimer();

        return START_STICKY;
    }


    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");

        notificationManager = NotificationManagerCompat.from(this);
        createNotificationChannel();

    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        stoptimertask();
        super.onDestroy();


    }

    //we are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler();


    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 10000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask,   5000, Your_X_SECS * 1000); //
        //timer.schedule(timerTask, 5000,1000);
    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = CHANNEL_ID;
            String description = "example noti";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {

                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run() {

                        //TODO CALL NOTIFICATION FUNC
                        final User user  = SharedPrefManager.getInstance(NotificationService.this).getUser();
                        ApiInterface api = ConnectServer.getClient().create(ApiInterface.class);
                        Call<User> call = api.getverified(user.getUserId());
                        if (SharedPrefManager.getInstance(NotificationService.this).getUser().getUserStatusVerified() == 0) {
                        call.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (response.body().getUserStatusVerified() == 1 && user.getUserStatusVerified() == 0){

                                    Intent clickIntent = new Intent(NotificationService.this, SplashScreen.class);
                                    PendingIntent pendingIntent = PendingIntent.getActivity(NotificationService.this, 0, clickIntent, 0);

                                    Notification notification = new NotificationCompat.Builder(NotificationService.this, CHANNEL_ID)
                                            .setSmallIcon(R.drawable.logo)
                                            .setContentTitle("ยืนยันตัวตนเสร็จสมบุรณ์")
                                            .setContentText("ขณะนี้ท่านสามารถเริ่มใช้งานการรับซื้อสินค้าได้แล้วในขณะนี้")
                                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                            .setAutoCancel(true)
                                            .setContentIntent(pendingIntent)
                                            .build();

                                    notificationManager.notify(1, notification);
                                    user.setUserStatusVerified(1);
                                    SharedPrefManager.getInstance(NotificationService.this).saveUser(user);
                                    stoptimertask();
                                } else if (response.body().getUserStatusVerified() == 2 && user.getUserStatusVerified() == 0) {
                                    Intent clickIntent = new Intent(NotificationService.this, SplashScreen.class);
                                    PendingIntent pendingIntent = PendingIntent.getActivity(NotificationService.this, 0, clickIntent, 0);

                                    Notification notification = new NotificationCompat.Builder(NotificationService.this, CHANNEL_ID)
                                            .setSmallIcon(R.drawable.logo)
                                            .setContentTitle("ยืนยันตัวตนไม่สำเร็จ")
                                            .setContentText("กรุณาการทำยืนยันตัวตนอีกตรั้ง")
                                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                            .setAutoCancel(true)
                                            .setContentIntent(pendingIntent)
                                            .build();

                                    notificationManager.notify(1, notification);
                                    user.setUserStatusVerified(1);
                                    SharedPrefManager.getInstance(NotificationService.this).saveUser(user);
                                    stoptimertask();
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {

                            }
                        });

                    }}
                });
            }
        };
    }
}
