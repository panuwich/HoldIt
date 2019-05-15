package project.senior.holdit.verify;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import project.senior.holdit.R;
import project.senior.holdit.manager.SharedPrefManager;
import project.senior.holdit.model.ResponseModel;
import project.senior.holdit.model.User;
import project.senior.holdit.retrofit.ApiInterface;
import project.senior.holdit.retrofit.ConnectServer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import teste.epassporttest.Facade.Facade;
import teste.epassporttest.data.Credentials;
import teste.epassporttest.data.Passenger;

public class ePassportInfoDisplay extends Activity
{

    Facade fachada;
    String[] result;
    Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_passport_info_display);
        Intent intent = getIntent();
        result = intent.getStringArrayExtra("result");

        startReading();
    }

    private void startReading()
    {
        fachada = new Facade(getData(result), new Facade.ReadingPassport()
        {
            @Override
            public void readingPassportDataSucess(Passenger passenger)
            {

                String DOB = "";
                String EXP = "";
                DateFormat df =  new SimpleDateFormat("yyMMdd");
                DateFormat df2 =  new SimpleDateFormat("yy/MM/dd");
                try {
                    Date dateDOB = df.parse(result[2]);
                    Date dateEXP = df.parse(result[4]);
                    DOB = df2.format(dateDOB);
                    EXP = df2.format(dateEXP);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                result = new String[]{passenger.getPassportNumber(),passenger.getDocumentID()
                ,passenger.getName(),passenger.getSurname(),passenger.getGender()
                ,DOB,result[3],EXP,passenger.getNationality()};



            }

            @Override
            public void readingPassportImageSucess(Bitmap passengerPhoto)
            {
                bmp = passengerPhoto;
            }

            @Override
            public void readingPassportDataFail(String error) {}

            @Override
            public void readingPassportImageFail(String error) {}

            @Override
            public void readingPassportFinished() {
                String citizenId = SharedPrefManager.getInstance(ePassportInfoDisplay.this).getUser().getUserCitizen();
                System.out.println("citizen id = "  + result[1] + " " + citizenId);

                if(citizenId.equals(result[1])){
                    setVerifyStatus();
                }else{
                    Toast.makeText(ePassportInfoDisplay.this, "เลขบัตรประชาชนไม่ตรงกัน", Toast.LENGTH_SHORT).show();
                }
                findViewById(teste.epassporttest.R.id.loadingPanel).setVisibility(View.GONE);
                Intent i = new Intent(ePassportInfoDisplay.this, Verification.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                /*bmp = Bitmap.createScaledBitmap(bmp, 150, 200, false);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                i.putExtra("bmp",byteArray);
                i.putExtra("result",result);*/
                startActivity(i);
                finish();

            }
            @Override
            public void readingPassportFail(String error) {
                Toast.makeText(ePassportInfoDisplay.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        fachada.handleIntent(this.getIntent());
    }

    public void setVerifyStatus(){
        final ApiInterface apiService = ConnectServer.getClient().create(ApiInterface.class);
        Call<ResponseModel> call = apiService.updateverify(SharedPrefManager.getInstance(ePassportInfoDisplay.this).getUser().getUserId());
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                User user =  SharedPrefManager.getInstance(ePassportInfoDisplay.this).getUser();
                user.setUserStatusVerified(1);
                SharedPrefManager.getInstance(ePassportInfoDisplay.this).saveUser(user);
                Toast.makeText(ePassportInfoDisplay.this, response.body().getResponse(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {

            }
        });
    }
    private ArrayList<Credentials> getData(String [] result)
    {
        ArrayList<Credentials> allCredentials = new ArrayList<Credentials>();

        try
        {

            Credentials credentialGabriel = new Credentials(result[0],result[2], result[4]);

            allCredentials.add(credentialGabriel);

        }catch (Exception ex){
            ex.printStackTrace();
        }

        return allCredentials;
    }


}
