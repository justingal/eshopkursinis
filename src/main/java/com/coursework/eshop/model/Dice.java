package com.coursework.eshop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Dice extends Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_gen")
    private int id;
    int diceNumber;

    @OneToMany(mappedBy = "dice", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Review> reviews;

    public Dice(String title, String description, String author, Warehouse warehouse,double price, int diceNumber) {
        super(title, description, author, warehouse, price);
        this.diceNumber = diceNumber;
    }

    public Dice(int id, String title, String description, String author, double price, int diceNumber) {
        super(id, title, description, author, price);
        this.diceNumber = diceNumber;
    }

    public Dice(String title, String description, String author, double price, int diceNumber) {
        super(title, description, author, price);
        this.diceNumber = diceNumber;
    }
}