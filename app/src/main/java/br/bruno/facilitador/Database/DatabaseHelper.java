package br.bruno.facilitador.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import br.bruno.facilitador.VO.Order;
import br.bruno.facilitador.VO.Order_Product;

/**
 * Created by Bruno on 10/10/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "facilitador.db";

    public DatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null,1);
        SQLiteDatabase db = this.getWritableDatabase();
        Log.i("Facilitador","DatabaseHelper constructor");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("Facilitador","createtables");

        db.execSQL("CREATE TABLE IF NOT EXISTS ORDERS " +
                "(orderid integer, " +
                "email text, " +
                "order_date text, " +
                "total real, " +
                "status integer," +
                "primary key(orderid, email))");

        db.execSQL("CREATE TABLE IF NOT EXISTS ORDER_PRODUCT " +
                "(product_code integer," +
                "orderid integer," +
                "product_name text," +
                "product_description text," +
                "price real," +
                "qty integer," +
                "primary key(orderid, product_code))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.i("Facilitador","ONUPGRADE");
        db.execSQL("DROP TABLE IF EXISTS ORDERS");
        db.execSQL("DROP TABLE IF EXISTS ORDER_PRODUCT");
        onCreate(db);
    }
    public boolean delete(int code){
        Log.i("Facilitador","delete()");
        SQLiteDatabase db = this.getWritableDatabase();
        //int result =  db.delete(TABLE_NAME,COL_1+" = ?",new String[]{Integer.toString(code)});
       // Log.i("Facilitador","delete() = "+result);
        //if(result == 1) return true;
        return false;

    }
    public Order getOrder(String email){
        SQLiteDatabase db =this.getReadableDatabase();
        String query = "SELECT * FROM ORDERS WHERE email = '"+email+"' AND status = -1;";
        Cursor cursor = db.rawQuery(query, null);
        Order order = new Order();
        Log.i("Facilitador","cursor ="+cursor.getCount());

        if(cursor!=null){
            Log.i("Facilitador","cursor not null");
            if(cursor.getCount()==0) return null;
            cursor.moveToFirst();
            return order;

        }
        Log.i("Facilitador","cursor null");
        return null;
    }

    public boolean insertProduct(int product_code, String product_name, String product_description, double price){
        Log.i("Facilitador","insertProduct()");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
      //  contentValues.put(COL_1,product_code);
     //  contentValues.put(COL_2,product_name);
     //   contentValues.put(COL_3,product_description);
    //    contentValues.put(COL_4,price);
   //     long result = db.insert(TABLE_NAME,null,contentValues);
  //      if (result == -1){
  //          Log.i("Facilitador","Não inserido");
  //          return false;
   //     }
  //     else{
    //        Log.i("Facilitador","Inserido");
  //          return true;
    //    }
        return false;
    }

}
