package br.bruno.facilitador;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Bruno on 03/10/2017.
 */

public class BackgroundTask extends AsyncTask<String,Void,String>{
    String registeruser_url = "http://bpivoto.000webhostapp.com/register.php";
    String login_url = "http://bpivoto.000webhostapp.com/login.php";
    String name_sp, email_sp;
    Context ctx;
    Activity activity;
    ProgressDialog progressDialog;
    AlertDialog.Builder builder;
    public BackgroundTask(Context ctx){
        this.ctx = ctx;
        activity = (Activity) ctx;
    }
    @Override
    protected void onPreExecute() {
        builder = new AlertDialog.Builder(activity);
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setTitle("Por favor, aguarde");
        progressDialog.setMessage("Comunicando com servidor...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String json) {
        try {
            progressDialog.dismiss();
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("server_response");
            JSONObject JO = jsonArray.getJSONObject(0);
            String code = JO.getString("code");
            String message = JO.getString("message");
            if(code.equals("reg_true")){
                Log.i("Facilitador","reg_true");
                showDialog("Registrado",message,code);
            }else if(code.equals("reg_false")){
                Log.i("Facilitador","reg_false");
                showDialog("Falha",message,code);
            }
            else if(code.equals("login_true")){
                UserSession userSession = new UserSession(this.ctx);
                name_sp = JO.getString("name");
                email_sp = JO.getString("email");
                userSession.saveSession(name_sp,email_sp,message);

                Intent intent = new Intent(activity,HomeActivity.class);
                intent.putExtra("message",message);
                activity.startActivity(intent);
            }else if(code.equals("login_false")){
                name_sp = "";
                email_sp = "";
                Log.i("Facilitador","login_false");
                showDialog("Falha de login",message,code);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void showDialog(String title, String message, String code){
        builder.setTitle(title);
        Log.i("Facilitador","ShowDialog");
        if(code.equals("reg_true") || code.equals("reg_false")){
            builder.setMessage(message);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.dismiss();
                    activity.finish();
                }
            });


        }else if(code.equals("login_false")){
            builder.setMessage(message);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    EditText email,password;
                    email = (EditText)activity.findViewById(R.id.email);
                    password = (EditText) activity.findViewById(R.id.password);

                    email.setText("");
                    password.setText("");
                    dialog.dismiss();
                }
            });


        }
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    @Override
    protected String doInBackground(String... params) {
        String method = params[0];
        if(method.equals("registeruser"))
        {
            try{
                URL url = new URL(registeruser_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String name = params[1];
                String lastname = params[2];
                String email = params[3];
                String password = params[4];

                String data = URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"+
                        URLEncoder.encode("lastname","UTF-8")+"="+URLEncoder.encode(lastname,"UTF-8")+"&"+
                        URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"+
                        URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line = "";
                while((line=bufferedReader.readLine())!=null)
                {
                    stringBuilder.append(line+"\n");
                }
                Thread.sleep(5000);
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();


            }catch (MalformedURLException e){
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }else if(method.equals("login")){
            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String email,password;
                email = params[1];
                password = params[2];
                String data = URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"+
                        URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line = "";
                while((line=bufferedReader.readLine())!=null)
                {
                    stringBuilder.append(line+"\n");
                }
                Thread.sleep(5000);
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}