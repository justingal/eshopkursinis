package com.coursework.eshop.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Embeddable

public class ShoppingCart implements Serializable {

    @ElementCollection
    private List<CartItem> items = new ArrayList<>();


    public ShoppingCart() {
    }

    public ShoppingCart(List<CartItem> items) {
        this.items = items;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public void addItem(CartItem newItem) {
        for (CartItem item : items) {
            if (item.getProductId() == newItem.getProductId()) {
                return;
            }
        }
        items.add(newItem);
    }

    public void removeItem(int productId) {
        items.removeIf(item -> item.getProductId() == productId);
    }


}