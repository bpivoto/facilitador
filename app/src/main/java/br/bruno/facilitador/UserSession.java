package br.bruno.facilitador;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

/**
 * Created by Bruno on 04/10/2017.
 */

public class UserSession {

    SharedPreferences sharedPreferences;
    Context ctx;
    String message,name,email;
    public UserSession(Context ctx) {
        this.ctx = ctx;
        sharedPreferences = ctx.getSharedPreferences("usersession",Context.MODE_PRIVATE);
    }

    public void saveSession(String name, String email,String message){
        Log.i("Facilitador","saveSession-"+name+" - "+email);
        this.message=message;
        this.email=email;
        this.name=name;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("logged",true);
        editor.putString("name",name);
        editor.putString("email",email);
        editor.putString("message",message);
        editor.commit();

    }
    public String getMessage(){
        return sharedPreferences.getString("message","");
    }
    public String getName(){
        return sharedPreferences.getString("name","");
    }
    public String getEmail(){
        return sharedPreferences.getString("email","");
    }
    public boolean isLogged(){
        return sharedPreferences.getBoolean("logged",false);
    }
}
