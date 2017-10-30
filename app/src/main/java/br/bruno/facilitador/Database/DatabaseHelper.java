package br.bruno.facilitador.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Array;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import br.bruno.facilitador.VO.Order;
import br.bruno.facilitador.VO.Order_Product;

/**
 * Created by Bruno on 10/10/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "facilitador.db";

    public DatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
        Log.i("Facilitador", "DatabaseHelper constructor");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("Facilitador", "createtables");

        db.execSQL("CREATE TABLE IF NOT EXISTS ORDERS " +
                "(orderid text, " +
                "email text, " +
                "order_date text, " +
                "total real, " +
                "status integer," +
                "primary key(orderid, email))");

        db.execSQL("CREATE TABLE IF NOT EXISTS ORDER_PRODUCT " +
                "(product_code integer," +
                "orderid text," +
                "product_name text," +
                "product_description text," +
                "price real," +
                "qty integer," +
                "primary key(orderid, product_code))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.i("Facilitador", "ONUPGRADE");
        db.execSQL("DROP TABLE IF EXISTS ORDERS");
        db.execSQL("DROP TABLE IF EXISTS ORDER_PRODUCT");
        onCreate(db);
    }

    public boolean delete(int code) {
        Log.i("Facilitador", "delete()");
        SQLiteDatabase db = this.getWritableDatabase();
        //int result =  db.delete(TABLE_NAME,COL_1+" = ?",new String[]{Integer.toString(code)});
        // Log.i("Facilitador","delete() = "+result);
        //if(result == 1) return true;
        return false;

    }

    public ArrayList<Order_Product> getProductsOrder(String orderid) {
        ArrayList<Order_Product> products = new ArrayList<Order_Product>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM ORDER_PRODUCT WHERE orderid = '" + orderid + "';";
        Cursor cursor = db.rawQuery(query, null);

        Log.i("Facilitador", "Procura produtos: orderid = " + orderid);
        Log.i("Facilitador", "Procura produtos: "+query);
        if (cursor != null) {
            Log.i("Facilitador", "getProductsOrder Cursor null");
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    Order_Product product = new Order_Product();
                    product.setProduct_code(cursor.getInt(0));
                    product.setProduct_name(cursor.getString(2));
                    product.setProduct_description(cursor.getString(3));
                    product.setPrice(cursor.getDouble(4));
                    product.setQty(cursor.getInt(5));
                    products.add(product);
                    Log.i("Facilitador", "Adicionou o produto: code: " + product.getProduct_code() + " \nName: " + product.getProduct_name() + "\nDescription: " + product.getProduct_description() + "\nPrice: " + product.getPrice() + "\nQty:" + product.getQty());
                } while (cursor.moveToNext());
            } else {
                Log.i("Facilitador", "getProductsOrder count 0");
            }
        }
        return products;
    }

    public Order getOrder(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM ORDERS WHERE email = '" + email + "' AND status = -1;";
        Cursor cursor = db.rawQuery(query, null);
        Order order = new Order();
        Log.i("Facilitador", "cursor =" + cursor.getCount());

        if (cursor != null) {
            Log.i("Facilitador", "cursor not null");

            if (cursor.getCount() == 0) {
                Log.i("Facilitador", "cursor=0");
                newOrder(email);
            } else {
                cursor.moveToFirst();
                order.setEmail(email);
                order.setOrderid(cursor.getString(0));
                order.setOrder_date(cursor.getString(2));
                order.setTotal(cursor.getDouble(3));
                order.setStatus(cursor.getInt(4));
                order.setOrder_product(getProductsOrder(order.getOrderid()));
                Log.i("Facilitador", "Dados da ORDER: Email: " + order.getEmail() + " \nOrderID: " + order.getOrderid() + "\nTotal:" + order.getTotal());
            }

            if(order.getOrder_product()!=null){
                Log.i("Facilitador",order.getOrder_product().size()+ " produtos inseridos na orderid "+order.getOrderid());
                for(int i = 0; i< order.getOrder_product().size();i++){
                    Log.i("Facilitador",order.getOrder_product().get(i).getProduct_name()+" - code: "+order.getOrder_product().get(i).getProduct_code());
                }
            }
            return order;

        }
        Log.i("Facilitador", "cursor null");
        return null;
    }

    public boolean insertProduct(String orderid, int product_code, String product_name, String product_description, double price) {
        Log.i("Facilitador", "insertProduct()");
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM ORDER_PRODUCT WHERE orderid = '" + orderid + "' AND product_code = " + product_code + ";";
        Log.i("Facilitador",query);
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                Log.i("Facilitador", "insertProduct - produto ja cadastrado");
                cursor.moveToFirst();
                int qty = cursor.getInt(5);

                db = this.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("qty", qty+1);
                int update = db.update("ORDER_PRODUCT",values,"orderid = ? AND product_code = ?",new String[]{orderid,""+product_code});
                Log.i("Facilitador","update = "+update);
            } else {
                Log.i("Facilitador", "insertProduct - produto não cadastrado");
                db = this.getWritableDatabase();


                ContentValues contentValues = new ContentValues();
                contentValues.put("product_code", product_code);
                contentValues.put("orderid", orderid);
                contentValues.put("product_name", product_name);
                contentValues.put("product_description", product_description);
                contentValues.put("price", price);
                contentValues.put("qty", 1);
                long result = db.insert("ORDER_PRODUCT", null, contentValues);
                if (result == -1){
                    Log.i("Facilitador","produto não inserido");
                    return false;
                }
                else{
                    Log.i("Facilitador","produto inserido");
                    return true;
                }
            }
        }
    return false;
    }


       // SQLiteDatabase db = this.getWritableDatabase();
       // ContentValues contentValues = new ContentValues();
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

    public boolean newOrder(String email){
        String orderid;
        SimpleDateFormat formater = new SimpleDateFormat("yyMMddHH");
        orderid = formater.format(new Date());
        Log.i("Facilitador","novoorder"+orderid);
        int status;
        Double total;
        String order_date;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        order_date=sdf.format(new Date());

        Log.i("Facilitador","newOrder date="+order_date);

        insertOrder(orderid,email,order_date, 0.0,-1);

        return false;
    }
    public boolean insertOrder(String orderid, String email, String order_date, Double total, int status){
        Log.i("Facilitador","insertOrder()");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("orderid",orderid);
        contentValues.put("email",email);
        contentValues.put("order_date",order_date);
        contentValues.put("total",total);
        contentValues.put("status",status);
        long result = db.insert("ORDERS",null,contentValues);
        if (result == -1){
            Log.i("Facilitador","ORDER Não inserido");
            return false;
        }else{
            Log.i("Facilitador","ORDER Inserido");
            return true;
        }

    }

}
