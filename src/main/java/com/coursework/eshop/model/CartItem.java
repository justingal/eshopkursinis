package com.coursework.eshop.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public
class CartItem {
    private int productId;
    private String name;
    private double price;

    @Override
    public String toString() {
        return name + " " + price;
    }

    public double getPrice() {
        return price;
    }

}
