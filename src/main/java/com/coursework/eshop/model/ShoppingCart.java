package com.coursework.eshop.model;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ShoppingCart {
    @ElementCollection
    private List<CartItem> items = new ArrayList<>();
    private double totalPrice;

    public void addItem(CartItem item) {
        items.add(item);
        recalculateTotal();
    }

    public void removeItem(int productId) {
        items.removeIf(item -> item.getProductId() == productId);
        recalculateTotal();
    }

    private void recalculateTotal() {
        totalPrice = items.stream()
                .mapToDouble(CartItem::getPrice)
                .sum();
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}