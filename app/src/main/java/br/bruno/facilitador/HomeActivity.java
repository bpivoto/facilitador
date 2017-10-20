package br.bruno.facilitador;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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
import java.util.ArrayList;

import br.bruno.facilitador.Database.DatabaseHelper;
import br.bruno.facilitador.VO.Order;
import br.bruno.facilitador.VO.Order_Product;

public class HomeActivity extends AppCompatActivity {
    String searchproduct_url = "http://bpivoto.000webhostapp.com/search_product.php";
    ProgressDialog progressDialog;
    Order order;
    ArrayAdapter<Order_Product> adapter;
    DatabaseHelper db;
    BGtask backgroundtask;
    TextView welcome_txt;
    EditText campo;
    Button manual;
    UserSession userSession ;
    Button scan_btn;
    String read_id;

    ListView listView;

    BackgroundTask backgroundTask;
    class BGtask extends AsyncTask<String,Void,String>{

        Activity activity;
        Context ctx;
        public BGtask(Context ctx){
            this.ctx = ctx;
            activity = (Activity) ctx;
        }
        String searchproduct_url = "http://bpivoto.000webhostapp.com/search_product.php";


        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ctx);
            progressDialog.setTitle("Por favor, aguarde");
            progressDialog.setMessage("Comunicando com servidor...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            String method = params[0];

            try {
                URL url = new URL(searchproduct_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String product_code;
                product_code = params[1];

                String data = URLEncoder.encode("product_code","UTF-8")+"="+URLEncoder.encode(product_code,"UTF-8");

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
                Thread.sleep(1000);
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
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String json) {
            try {
                Log.i("Facilitador","onPostExecute-homeactivity");
                progressDialog.dismiss();
                JSONObject jsonObject = new JSONObject(json);
                JSONArray jsonArray = jsonObject.getJSONArray("server_response");
                JSONObject JO = jsonArray.getJSONObject(0);
                String code = JO.getString("code");
                Log.i("Facilitador",json);
                if(code.equals("product_found")){


                    Order_Product orderProduct = new Order_Product();
                    orderProduct.setProduct_code(JO.getInt("product_code"));
                    orderProduct.setProduct_name(JO.getString("product_name"));
                    orderProduct.setProduct_description(JO.getString("product_description"));
                    orderProduct.setPrice(JO.getDouble("price"));

                    int procura=checkIfExist(orderProduct.getProduct_code());
                    if(procura==-1){
                        db.insertProduct(orderProduct.getProduct_code(), orderProduct.getProduct_name(), orderProduct.getProduct_description(), orderProduct.getPrice());
                        Log.i("Facilitador","Não existe ainda");
                    }else{
                      //  Log.i("Facilitador","Já existe em "+procura + "= "+ orderProducts.get(procura).getProduct_code()+ ", "+ orderProducts.get(procura).getProduct_name());
                    }
                    //orderProducts.add(0, orderProduct);


                    adapter.notifyDataSetChanged();
                    Log.i("Facilitador","product_found");

                }else{
                    if(code.equals("product_notfound")){
                        Toast.makeText(getApplicationContext(),JO.getString("message"),Toast.LENGTH_LONG).show();
                    }
                }
            } catch (JSONException e) {

                e.printStackTrace();
            }
            catch(NullPointerException e){

                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = new DatabaseHelper(this);
        Log.i("Facilitador","onCreate HomeActivity");
        userSession = new UserSession(this);
        if(userSession.isLogged()){
            Log.i("Facilitador","isLogged!");
        }else{
            Log.i("Facilitador","notLogged");
        }

        String message = userSession.getMessage();
        welcome_txt = (TextView) findViewById(R.id.welcome_txt);
        welcome_txt.setText(message);



        order = db.getOrder(userSession.getEmail());
        if(order == null)
         Log.i("Facilitador","onCreate HomeActivity - ORDER == NULL ");
        else{
            Log.i("Facilitador,",order.getEmail()+" - "+order.getOrderid());
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        listView = (ListView) findViewById(R.id.list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = listView.getItemAtPosition(i).toString();
                //Toast.makeText(getApplicationContext(),"Item "+i+" - "+item,Toast.LENGTH_LONG).show();

            //    if(db.delete(orderProducts.get(i).getProduct_code())){
               //     orderProducts.remove(i);
              //      adapter.notifyDataSetChanged();
              //  }

            }
        });

       // adapter = new ArrayAdapter<Order_Product>(this,android.R.layout.simple_list_item_1, order.getOrder_product());
       // listView.setAdapter(adapter);




        scan_btn = (Button) findViewById(R.id.scan_btn);
        final Activity activity = this;
        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(activity);

                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                intentIntegrator.setPrompt("Scan!");
                intentIntegrator.setCameraId(0);

                intentIntegrator.setBeepEnabled(false);
                intentIntegrator.setBarcodeImageEnabled(false);
                intentIntegrator.initiateScan();
            }
        });






        manual = (Button) findViewById(R.id.manually_btn);


        campo = (EditText) findViewById(R.id.campo);
        manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BGtask backBGtask;
                backBGtask = new BGtask(HomeActivity.this);
                backBGtask.execute("searchproduct",campo.getText().toString());

                adapter.notifyDataSetChanged();

            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("Facilitador","onActivityResult");
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        read_id = result.getContents();

        if (result != null){
            Log.i("Facilitador","SCAN result not null");
            if(result.getContents()==null){
                Log.i("Facilitador","SCAN cancelled");
                Toast.makeText(this,"Vc cancelou o scan",Toast.LENGTH_LONG).show();
            }else{

                backgroundtask = new BGtask(HomeActivity.this);
                backgroundtask.execute("searchproduct",result.getContents());
                Toast.makeText(this,result.getContents(),Toast.LENGTH_LONG).show();


;
            }
        }else{
            Log.i("Facilitador","SCAN result null");
            super.onActivityResult(requestCode, resultCode, data);
        }


    }

    public int checkIfExist(int code_searched){

        for (int i = 0; i< order.getOrder_product().size(); i++){
            if (code_searched== order.getOrder_product().get(i).getProduct_code())
                return i;
        }


        return -1;

    }

}
