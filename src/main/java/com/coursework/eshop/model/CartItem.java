package com.coursework.eshop.model;

import jakarta.persistence.Embeddable;

@Embeddable
public
class CartItem {
    private int productId;
    private String name;
    private double price;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public CartItem() {
    }

    public CartItem(int productId, String name, double price) {
        this.productId = productId;
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return name + " " + price;
    }

    public double getPrice() {
        return price;
    }

}
