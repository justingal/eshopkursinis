package com.coursework.eshop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@MappedSuperclass
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String title;
    String description;
    String author;
    double price;
    int quantity;
    @ManyToOne
    private Warehouse warehouse;
    @ManyToMany
    private List<Cart> cart;


    public Product(String title, String description, String author, double price, int quantity) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.price = price;
        this.quantity = quantity;
    }

    public Product(int id, String title, String description, String author, double price, int quantity) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.author = author;
        this.price = price;
        this.quantity = quantity;
    }

    public Product(String title, String description, String author, Warehouse warehouse, double price, int quantity) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.warehouse = warehouse;
        this.price = price;
        this.quantity = quantity;
    }



    @Override
    public String toString() {
        return title +" "+price+"€";
    }

}