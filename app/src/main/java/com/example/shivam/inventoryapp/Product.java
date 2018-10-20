package com.example.shivam.inventoryapp;

public class Product {
    private String productName="";
    private Double productPrice=0.0;
    private Integer productQuantity=0, sale=0;

    public Product(String productName, Double productPrice, Integer productQuantity, Integer sale) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.sale = sale;
    }

    public String getProductName() {
        return productName;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public Integer getProductQuantity() {
        return productQuantity;
    }

    public Integer getSale() {
        return sale;
    }
}
