package com.coursework.eshop.model;

import jakarta.persistence.*;

import java.io.Serializable;


@MappedSuperclass
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_gen")
    int id;
    String title;
    String description;
    String author;
    double price;
    @ManyToOne
    private Warehouse warehouse;

    public Product() {
    }

    public Product(int id, String title, String description, String author, double price, Warehouse warehouse) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.author = author;
        this.price = price;
        this.warehouse = warehouse;
    }

    public Product(String title, String description, String author, double price) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.price = price;
    }

    public Product(int id, String title, String description, String author, double price) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.author = author;
        this.price = price;
    }

    public Product(String title, String description, String author, Warehouse warehouse, double price) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.warehouse = warehouse;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }


    @Override
    public String toString() {
        return title + " " + price + "€";
    }

}