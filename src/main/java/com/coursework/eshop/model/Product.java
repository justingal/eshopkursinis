package com.coursework.eshop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

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
    @ManyToOne
    private Warehouse warehouse;
    @ManyToOne
    private Cart cart;

    public Product(String title, String description, String author) {
        this.title = title;
        this.description = description;
        this.author = author;
    }

    public Product(int id, String title, String description, String author) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.author = author;
    }

    public Product(String title, String description, String author, Warehouse warehouse) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.warehouse = warehouse;
    }

    @Override
    public String toString() {
        return title;
    }
}