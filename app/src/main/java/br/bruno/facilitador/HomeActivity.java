package br.bruno.facilitador;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;

public class HomeActivity extends AppCompatActivity {
    TextView textView;
    UserSession userSession ;
    Button scan_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        userSession = new UserSession(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        scan_btn = (Button) findViewById(R.id.scan_btn);
        final Activity activity = this;
        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(activity);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.PRODUCT_CODE_TYPES);
                intentIntegrator.setPrompt("Scan!");
                intentIntegrator.setCameraId(0);
                intentIntegrator.setBeepEnabled(false);
                intentIntegrator.setBarcodeImageEnabled(false);
                intentIntegrator.initiateScan();
            }
        });


        if(userSession.isLogged()){
            Log.i("Facilitador","isLogged!");
        }else{
            Log.i("Facilitador","notLogged");
        }
        textView = (TextView) findViewById(R.id.welcome_txt);
        String message = getIntent().getStringExtra("message");
        if(message==null){
            message = userSession.getMessage();
        }

        textView.setText(message);
    }
}
