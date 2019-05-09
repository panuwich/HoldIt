package project.senior.holdit.verify;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;
import project.senior.holdit.R;

public class VerificationPassport extends AppCompatActivity {
    private CircleImageView btnPassport;
    NfcAdapter nfcAdapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_passport);
        btnPassport = (CircleImageView)findViewById(R.id.imageView_veri_passport_camera);

        btnPassport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nfcAdapter = nfcAdapter.getDefaultAdapter(VerificationPassport.this); // get default nfc adapter
                if (nfcAdapter == null || !nfcAdapter.isEnabled()) {
                    Toast.makeText(VerificationPassport.this, "Please Turn on the NFC", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(VerificationPassport.this, CameraActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
