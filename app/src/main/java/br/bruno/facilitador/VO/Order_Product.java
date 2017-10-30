package br.bruno.facilitador.VO;

/**
 * Created by Bruno on 09/10/2017.
 */

public class Order_Product {
    private int product_code;
    private String product_name;

    @Override
    public String toString() {
        return product_name+" - "+"R$ "+price+" - "+qty+" unidades";
    }

    private String product_description;
    private double price;
    private int qty;

    public int getProduct_code() {
        return product_code;
    }

    public void setProduct_code(int product_code) {
        this.product_code = product_code;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }




}
