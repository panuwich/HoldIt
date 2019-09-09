package project.senior.holdit.verify;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import project.senior.holdit.MainActivity;
import project.senior.holdit.R;

public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private SurfaceView cameraView, transparentView;
    private SurfaceHolder holder, holderTransparent;
    private Camera camera;
    public static final String TESS_DATA = "/tessdata";
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/Tess";
    private TessBaseAPI tessBaseAPI;
    private Bitmap bitmapCrop;
    private int widthPic, heightPic;
    private int RectLeft, RectTop, RectRight, RectBottom;
    int deviceHeight, deviceWidth;
    Timer timer = new Timer();
    String result = "";
    String checkAll="",nID = "", nFirstname = "", nSurname = "", nSex = ""
            , nDOB = "", nPassType = "", nExpDate = "", nNation = "", nCitizenNo = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        Window window = this.getWindow();

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorBlack));
      //  getSupportActionBar().hide();

        System.out.println("SCREEN : " + getScreenWidth() + " " + getScreenHeight());


        // Create first surface with his holder(holder)
        cameraView = (SurfaceView) findViewById(R.id.CameraView);
        holder = cameraView.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        cameraView.setSecure(true);

        // Create second surface with another holder (holderTransparent)

        transparentView = (SurfaceView) findViewById(R.id.TransparentView);

        holderTransparent = transparentView.getHolder();
        holderTransparent.addCallback((SurfaceHolder.Callback) this);
        holderTransparent.setFormat(PixelFormat.TRANSLUCENT);
        transparentView.setZOrderMediaOverlay(true);

        //getting the device heigth and width

        deviceWidth = getScreenWidth();
        deviceHeight = getScreenHeight();


    }

    private void startTakePic() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    camera.takePicture(null, null, mPicture);
                }catch (Exception e){

                }

            }

        }, 0, 3000);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
            System.out.println("TOUCH " + x+" "+y);
            Rect rect = calculateFocusArea(event.getX(), event.getY());
            doTouchFocus(rect);

        }
        return false;
    }

    private Rect calculateFocusArea(float x, float y) {
        int FOCUS_AREA_SIZE = 300;
        int left = clamp(Float.valueOf((x / widthPic * 2000 - 1000)).intValue(), FOCUS_AREA_SIZE);
        int top = clamp(Float.valueOf((y / heightPic * 2000 - 1000)).intValue(), FOCUS_AREA_SIZE);
        return new Rect(left, top, left + FOCUS_AREA_SIZE, top + FOCUS_AREA_SIZE);
    }

    private int clamp(int touchCoordinateInCameraReper, int focusAreaSize) {
        int result;
        if (Math.abs(touchCoordinateInCameraReper)+focusAreaSize/2>1000){
            if (touchCoordinateInCameraReper>0){
                result = 1000 - focusAreaSize/2;
            } else {
                result = -1000 + focusAreaSize/2;
            }
        } else{
            result = touchCoordinateInCameraReper - focusAreaSize/2;
        }
        return result;
    }

    private void doTouchFocus(final Rect tfocusRect) {
        try {
            final List<Camera.Area> focusList = new ArrayList<Camera.Area>();
            Camera.Area focusArea = new Camera.Area(tfocusRect, 1000);
            focusList.add(focusArea);

            Camera.Parameters para = camera.getParameters();
            para.setFocusAreas(focusList);
            para.setMeteringAreas(focusList);
            camera.setParameters(para);
            camera.autoFocus(myAutoFocusCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    Camera.AutoFocusCallback myAutoFocusCallback = new Camera.AutoFocusCallback() {

        @Override
        public void onAutoFocus(boolean arg0, Camera arg1) {
            // if (arg0) {
            //camera.takePicture(null,null,mPicture);
            camera.cancelAutoFocus();
            //  }
        }
    };
    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            bitmapCrop = bitmap;
            System.out.println("Screen : " + getScreenWidth() + " " + getScreenHeight());
            System.out.println("Bitmap : " + bitmapCrop.getWidth() + " " + bitmapCrop.getHeight());
            prepareTessData();
            startOCR();
        }
    };

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    private void Draw(){
        Canvas canvas = holderTransparent.lockCanvas(null);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(5);
        RectLeft = widthPic - (widthPic - 100);
        RectTop = (int)(heightPic*36/100);
        RectRight = widthPic - 100;
        RectBottom = RectTop + (int)(heightPic*20/100);

        //Border
        Rect rec = new Rect((int) RectLeft, (int) RectTop, (int) deviceWidth-100, (int) RectBottom);
        canvas.drawRect(rec, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#66000000"));
        //Left
        rec = new Rect(0, 0, 100, heightPic+100);
        canvas.drawRect(rec, paint);
        //Right
        rec = new Rect(deviceWidth-100, 0, deviceWidth, heightPic+100);
        canvas.drawRect(rec, paint);
        //Top
        rec = new Rect(RectLeft, 0, deviceWidth-100, RectTop);
        canvas.drawRect(rec, paint);
        //Bottom
        rec = new Rect(RectLeft, RectBottom, deviceWidth-100, heightPic+100);
        canvas.drawRect(rec, paint);

        paint.setColor(Color.GREEN);
        paint.setTextSize(50);
        canvas.drawText("Put the MRZ in here",RectLeft,RectTop-paint.getTextSize()/2,paint);
        holderTransparent.unlockCanvasAndPost(canvas);
    }
    private void releaseCameraAndPreview() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }
    @Override

    public void surfaceCreated(SurfaceHolder holder) {
        releaseCameraAndPreview();
        camera = Camera.open(); //open a camera

        try {
            //  Toast.makeText(this, "surfaceCreated", Toast.LENGTH_SHORT).show();
            synchronized (holder) {
                setPicSize();
                Draw();
            }   //call a draw method
        } catch (Exception e) {
            Log.i("Exception", e.toString());
            return;
        }
        setParam();
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (Exception e) {
            return;
        }
    }

    @Override

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        refreshCamera(); //call method for refress camera

         if(holder == cameraView.getHolder())  {
             startTakePic();
         }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.release();
    }

    void setParam() {
        Camera.Parameters param;
        param = camera.getParameters();
        param.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        param.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        if (display.getRotation() == Surface.ROTATION_0) {
            camera.setDisplayOrientation(90);
        }
        param.setPictureSize(widthPic, heightPic);
        param.setPreviewSize(widthPic, heightPic);
        camera.setParameters(param);
    }

    public void refreshCamera() {
        if (holder.getSurface() == null) {
            return;
        }
        try {
            camera.stopPreview();
        } catch (Exception e) {
        }

        try {
            camera.setPreviewDisplay(holder);
            setParam();
            camera.startPreview();
        } catch (Exception e) {

        }
    }

    public void setPicSize() {
        int maxWidth= -1,maxHeight=-1;
        Camera.Parameters param = camera.getParameters();
        for (Camera.Size size : param.getSupportedPictureSizes()) {

            if (size.width <= deviceWidth && size.height <= deviceHeight
                    && size.width > maxWidth && size.height > maxHeight) {
                maxWidth = size.width;
                maxHeight = size.height;
            }
        }
        if(maxWidth < 1920 && maxHeight < 1280){
            widthPic = 1280;
            heightPic = 720;
        }else{
            widthPic = maxWidth;
            heightPic = maxHeight;
        }


    }


    private void prepareTessData() {
        try {
            File dir = getExternalFilesDir(TESS_DATA);
            if (!dir.exists()) {
                if (!dir.mkdir()) {
                    Toast.makeText(getApplicationContext(), "The folder " + dir.getPath() + "was not created", Toast.LENGTH_SHORT).show();
                }
            }
            String fileList[] = {"mrz.traineddata"};
            for (String fileName : fileList) {
                String pathToDataFile = dir + "/" + fileName;

                if (!(new File(pathToDataFile)).exists()) {
                    InputStream in = getAssets().open(fileName);
                    OutputStream out = new FileOutputStream(pathToDataFile);

                    byte[] buff = new byte[1024];
                    int len;
                    while ((len = in.read(buff)) > 0) {
                        out.write(buff, 0, len);
                    }
                    in.close();
                    out.close();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void startOCR() {

        try {
            refreshCamera();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inSampleSize = 6;
            bitmapCrop = Bitmap.createBitmap(bitmapCrop, RectLeft, RectTop+30, RectRight - RectLeft , RectBottom - RectTop);

            result = getTextResult();
            extractString(result);
            System.out.println(result);

             if (nCitizenNo.isEmpty() || nDOB.isEmpty() || nExpDate.isEmpty() || nPassType.isEmpty()) {
                Toast.makeText(this, "Please try again ", Toast.LENGTH_SHORT).show();

            } else {
                ImageView img = (ImageView)findViewById(R.id.img);
                FrameLayout frameLayout = (FrameLayout)findViewById(R.id.frameLayout);
                Animation resize = AnimationUtils.loadAnimation(CameraActivity.this, R.anim.resize);
                Animation blink = AnimationUtils.loadAnimation(CameraActivity.this, R.anim.blink);

                img.setImageBitmap(bitmapCrop);
                img.startAnimation(resize);
                frameLayout.startAnimation(blink);
                timer.cancel();
                new CountDownTimer(500, 500) {
                    public void onTick(long millisUntilFinished) {
                        // Tick
                    }

                    public void onFinish() {
                        // Finish
                        String[] arResult  = new String[]{nID,nCitizenNo,nDOB,nPassType,nExpDate};
                        Intent intent = new Intent(CameraActivity.this, NfcAcitivity.class);
                        intent.putExtra("result", arResult);
                        startActivity(intent);
                        finish();
                    }
                }.start();

            }

        } catch (Exception e) {
            Log.e("startOCR", e.getMessage());
        }
    }

    private String getTextResult() {

        try {
            tessBaseAPI = new TessBaseAPI();
        } catch (Exception e) {
            Log.e("new obj ", e.getMessage());
        }
        String dataPath = getExternalFilesDir("/").getPath() + "/";
        bitmapCrop = bitmapCrop.copy(Bitmap.Config.ARGB_8888, true);
        tessBaseAPI.init(dataPath, "mrz");
        tessBaseAPI.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890<");
        tessBaseAPI.setImage(bitmapCrop);
        String result = "no result";
        try {
            result = tessBaseAPI.getUTF8Text();
        } catch (Exception e) {

            Log.e("getText", e.getMessage());
        }
        tessBaseAPI.end();
        return result.replace(" ", "");
    }

    private void extractString(String mrzResult) {
        String line1 = mrzResult.split("\n")[0];
        String line2 = mrzResult.split("\n")[1];

        checkAll = line2.substring(0,10)+line2.substring(13,20)+ line2.substring(21,43);
        try {
            if(!checkLastDigit(checkAll,Integer.parseInt(line2.charAt(line2.length()-1)+"")).isEmpty()){

                line2 = line2.replace(" ", "");
                String arrLine2[] = line2.split("<");
                //  Log.i("1",line1);
                // Log.i("1", line2);
                String removeEmp1 = "";
                String removeEmp2 = "";
                for (int i = 0; i < arrLine2.length; i++) {
                    if (!arrLine2[i].isEmpty()) {
                        removeEmp2 += arrLine2[i] + ",";
                        // Log.i("1", removeEmp2);
                    }
                }
                arrLine2 = removeEmp2.split(",");
                //  Log.i("1",arrLine1.length+"");
                //  Log.i("1",arrLine2.length+"");

                if(line1.charAt(0)== 'P' || line1.charAt(0) == 'O' || line1.charAt(0) == 'D'){
                    nPassType = line1.charAt(0)+"";
                }

                int indexArr1 = 0;
                int indexArr2 = 0;
                int startID = 0;
                int stopID = 9;
                int startDOBY = 13;
                int startDOBM = 15;
                int startDOBD = 17;
                int startExpY = 21;
                int startExpM = 23;
                int startExpD = 25;
                int startCitizenID = 28;

                if (arrLine2.length == 3) {
                    indexArr1 = 0;
                    indexArr2 = 1;
                    startID = 0;
                    stopID = arrLine2[0].length();
                    startDOBY = 4;
                    startDOBM = 6;
                    startDOBD = 8;
                    startExpY = 12;
                    startExpM = 14;
                    startExpD = 16;
                    startCitizenID = 18;
                }

                nID = checkLastDigit(arrLine2[indexArr1].substring(startID, stopID),Integer.parseInt(arrLine2[indexArr1].charAt(stopID)+""));

                try {
                    int y = Integer.parseInt(arrLine2[indexArr2].substring(startDOBY, startDOBY + 2));
                    int m = Integer.parseInt(arrLine2[indexArr2].substring(startDOBM, startDOBM + 2));
                    int d = Integer.parseInt(arrLine2[indexArr2].substring(startDOBD, startDOBD + 2));
                    String strDate = arrLine2[indexArr2].substring(startDOBY, startDOBD + 2);
                    if(!checkLastDigit(strDate,Integer.parseInt(arrLine2[indexArr2].charAt(startDOBD+2)+"")).isEmpty())
                        nDOB = new SimpleDateFormat("yyMMdd").format(new Date(y, m - 1, d));
                } catch (Exception e) {

                }


                try {
                    int y = Integer.parseInt(arrLine2[indexArr2].substring(startExpY, startExpY + 2));
                    int m = Integer.parseInt(arrLine2[indexArr2].substring(startExpM, startExpM + 2));
                    int d = Integer.parseInt(arrLine2[indexArr2].substring(startExpD, startExpD + 2));
                    String strDate = arrLine2[indexArr2].substring(startExpY, startExpD + 2);
                    if(!checkLastDigit(strDate,Integer.parseInt(arrLine2[indexArr2].charAt(startExpD+2)+"")).isEmpty())
                        nExpDate = new SimpleDateFormat("yyMMdd").format(new Date(y, m - 1, d));

                } catch (Exception e) {

                }

                nCitizenNo = arrLine2[indexArr2].substring(startCitizenID, arrLine2[indexArr2].length());
            }
        }catch (Exception e){

        }



    }


    private String checkLastDigit(String str,int lastDigit){
        int count = 7 ;
        int sum = 0 ;
        for(int i = 0 ; i < str.length() ; i++){
            int score = 0;
            if(str.charAt(i)=='<'){
                score = 0;
            }
            else if((int)str.charAt(i) >= 65 && (int)str.charAt(i) <= 90){
                score = (int)str.charAt(i) - 65 ;
            }else {
                score = Integer.parseInt(String.valueOf(str.charAt(i)));
            }
            sum += score*count;
            if(count == 7) count = 3;
            else if(count == 3) count = 1;
            else count = 7;
        }
        if (sum%10 == lastDigit) return str;
        else return "";
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        timer.cancel();
    }


}