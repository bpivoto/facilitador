package br.bruno.facilitador;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("Facilitador","onActivityResult");
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result != null){
            Log.i("Facilitador","SCAN result not null");
            if(result.getContents()==null){
                Log.i("Facilitador","SCAN cancelled");
                Toast.makeText(this,"Vc cancelou o scan",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this,result.getContents(),Toast.LENGTH_LONG).show();
                Log.i("Facilitador","SCAN ok");
            }
        }else{
            Log.i("Facilitador","SCAN result null");
            super.onActivityResult(requestCode, resultCode, data);
        }


    }
}
