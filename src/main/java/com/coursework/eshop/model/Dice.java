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

    public Dice(String title, String description, String author, Warehouse warehouse, int diceNumber) {
        super(title, description, author, warehouse);
        this.diceNumber = diceNumber;
    }

    public Dice(int id, String title, String description, String author, int diceNumber) {
        super(id, title, description, author);
        this.diceNumber = diceNumber;
    }
}