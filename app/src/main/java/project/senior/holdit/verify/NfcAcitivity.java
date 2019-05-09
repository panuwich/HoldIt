package project.senior.holdit.verify;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import project.senior.holdit.R;

public class NfcAcitivity extends AppCompatActivity {
    private NfcAdapter NFCadapter;
    String[] result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_acitivity);
        Context context = this.getApplicationContext();
        NFCadapter = NfcAdapter.getDefaultAdapter(this); // get default nfc adapter

        if (NFCadapter == null || !NFCadapter.isEnabled()) {
            Toast.makeText(context, "Please Turn on the NFC", Toast.LENGTH_SHORT).show();
            startActivity(
                    new Intent(Settings.ACTION_NFC_SETTINGS));
        }
        Intent intent = getIntent();
        result = intent.getStringArrayExtra("result");

    }

    @Override
    protected void onResume() {
        super.onResume();
        enableForegroundDispatch();
    }

    @Override
    protected void onPause() {
        super.onPause();
        disableForegroundDispatch();
    }

    public void enableForegroundDispatch() {

            //prepare the intent to the reader activity
            Intent i = new Intent(this, ePassportInfoDisplay.class);
            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            i.putExtra("result", result);

            PendingIntent pending = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

            String[][] techs = new String[][]{new String[]{"android.nfc.tech.IsoDep"}};

            //enable the foregroundDispatch,
            //this will give this P
            // assportIfoDisplay priority over another
            //to manage this intent

            NFCadapter.enableForegroundDispatch(this, pending, null, techs);

    }

    public void disableForegroundDispatch() {
        try {
            NFCadapter.disableForegroundDispatch(this);
            System.out.println("DIS");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
