package br.bruno.facilitador;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    TextView signup_text;
    UserSession userSession;
    Button login_bn;
    EditText email,password;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userSession = new UserSession(this);
        if(userSession.isLogged()){
            Intent intent = new Intent(this,HomeActivity.class);
            intent.putExtra("message",userSession.getMessage());
            this.startActivity(intent);
            Log.i("Facilitador","isLogged! - LoginActivity");
        }else{
            Log.i("Facilitador","notLogged! - LoginActivity");
        }

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        signup_text = (TextView) findViewById(R.id.sign_up);
        login_bn = (Button) findViewById(R.id.login_button);
        login_bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString().equals("") || password.getText().toString().equals("")){
                    builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("Campos vazios");
                    builder.setMessage("Preencha todos os campos!");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }else{
                    BackgroundTask backgroundTask = new BackgroundTask(LoginActivity.this);
                    backgroundTask.execute("login",email.getText().toString(),password.getText().toString());
                }
            }
        });
        signup_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));

            }
        });

    }
}
