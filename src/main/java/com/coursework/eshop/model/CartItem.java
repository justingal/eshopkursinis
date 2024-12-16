package com.coursework.eshop.model;

import jakarta.persistence.Embeddable;

@Embeddable
public
class CartItem {
    private int productId;
    private String name;
    private double price;
    private ProductType productType;

    public int getProductId() {
        return productId;
    }
    public CartItem(int productId, String name, double price, ProductType productType) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.productType = productType;
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

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public CartItem() {
    }


    @Override
    public String toString() {
        return name + " " + price;
    }

    public double getPrice() {
        return price;
    }

}
