package project.senior.holdit.verify;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import project.senior.holdit.MainActivity;
import project.senior.holdit.R;
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
            public void readingPassportFinished()
            {

                findViewById(teste.epassporttest.R.id.loadingPanel).setVisibility(View.GONE);
                Intent i = new Intent(ePassportInfoDisplay.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                bmp = Bitmap.createScaledBitmap(bmp, 150, 200, false);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                i.putExtra("bmp",byteArray);
                i.putExtra("result",result);
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
