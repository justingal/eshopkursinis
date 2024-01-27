package com.coursework.eshop.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Dice extends Product {
    int diceNumber;

    public Dice(String title, String description, String author, Warehouse warehouse,double price, int quantity, int diceNumber) {
        super(title, description, author, warehouse, price, quantity);
        this.diceNumber = diceNumber;
    }

    public Dice(int id, String title, String description, String author, double price, int quantity, int diceNumber) {
        super(id, title, description, author, price, quantity);
        this.diceNumber = diceNumber;
    }
}