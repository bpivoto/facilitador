package br.bruno.facilitador;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {
    EditText name,lastname,email,password,password_confirmation;
    Button reg_button;

    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name = (EditText) findViewById(R.id.reg_name);
        lastname = (EditText) findViewById(R.id.reg_lastname);
        email = (EditText) findViewById(R.id.reg_email);
        password = (EditText) findViewById(R.id.reg_password);
        password_confirmation = (EditText) findViewById(R.id.reg_password_confirmation);
        reg_button = (Button) findViewById(R.id.reg_button);
        reg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Facilitador","Botão Registrar");
                if(name.getText().toString().equals("")
                        || lastname.getText().toString().equals("")
                        || email.getText().toString().equals("")
                        || password.getText().toString().equals("")
                        || password_confirmation.getText().toString().equals(""))
                {
                    Log.i("Facilitador","Registro-Campos Vazios");
                    builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setTitle("Falha ao registrar");
                    builder.setMessage("Preencha todos os campos");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.i("Facilitador","Exibe alerta-Campos Vazios");
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                else if(password.getText().toString().equals(password_confirmation.getText().toString())==false){
                    Log.i("Facilitador","Registro-Senhas não coincidem");
                    builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setTitle("Falha ao registrar");
                    builder.setMessage("As senhas não coincidem");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.i("Facilitador","Exibe alerta-SEnhas nao coincidem");
                            dialog.dismiss();
                            password.setText("");
                            password_confirmation.setText("");
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }else{
                    BackgroundTask backgroundTask = new BackgroundTask(RegisterActivity.this);
                    backgroundTask.execute("registeruser",name.getText().toString(),lastname.getText().toString(),email.getText().toString(),password.getText().toString());
                }
            }
        });
    }
}
