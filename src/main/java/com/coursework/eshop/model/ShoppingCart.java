package com.coursework.eshop.model;

import jakarta.persistence.*;



import java.util.ArrayList;
import java.util.List;

@Embeddable

public class ShoppingCart {
    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public ShoppingCart() {
    }

    public ShoppingCart(List<CartItem> items) {
        this.items = items;
    }

    @ElementCollection
    private List<CartItem> items = new ArrayList<>();

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