package br.bruno.facilitador.VO;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Bruno on 16/10/2017.
 */

public class Order {
    private String orderid;
    private String email;
    private String order_date;
    private double total;
    private int status;
    private ArrayList<Order_Product> order_product;

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public void setTotal(double total) {
        this.total = getTotal();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<Order_Product> getOrder_product() {

        return order_product;
    }

    public void setOrder_product(ArrayList<Order_Product> order_product) {
        this.order_product = order_product;
    }



    public double getTotal() {
        total = 0;

        if(this.getOrder_product()==null) return 0;

        for(int i = 0; i<this.getOrder_product().size();i++){
            total = total+(this.getOrder_product().get(i).getPrice()*this.getOrder_product().get(i).getQty());
        }
        return total;
    }






}
