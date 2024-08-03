package com.coursework.eshop.model;

import jakarta.persistence.*;


import java.util.List;


@Entity
public class BoardGame extends Product {
    public BoardGame(String title, String description, String author, Warehouse warehouse, double price) {
        super(title, description, author, warehouse, price);
    }

    public BoardGame() {

    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPlayersQuantity(String playersQuantity) {
        this.playersQuantity = playersQuantity;
    }

    public void setGameDuration(String gameDuration) {
        this.gameDuration = gameDuration;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public int getId() {
        return id;
    }

    public String getPlayersQuantity() {
        return playersQuantity;
    }

    public String getGameDuration() {
        return gameDuration;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public BoardGame(String title, String description, String author, Warehouse warehouse, double price, String playersQuantity, int id, String gameDuration, List<Review> reviews) {
        super(title, description, author, warehouse, price);
        this.playersQuantity = playersQuantity;
        this.id = id;
        this.gameDuration = gameDuration;
        this.reviews = reviews;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_gen")
    private int id;

    private String playersQuantity;
    private String gameDuration;

    @OneToMany(mappedBy = "boardGame", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Review> reviews;

    public BoardGame(int id, String title, String description, String author, int price,  String playersQuantity, String gameDuration) {
        super(id, title, description, author, price);
        this.playersQuantity = playersQuantity;
        this.gameDuration = gameDuration;
    }

    public BoardGame(String title, String description, String author, double price,String playersQuantity, String gameDuration) {
        super(title, description, author, price);
        this.playersQuantity = playersQuantity;
        this.gameDuration = gameDuration;
    }

    public BoardGame(String title, String description, String author, Warehouse warehouse, double price, String playersQuantity, String gameDuration) {
        super(title, description, author, warehouse, price);
        this.playersQuantity = playersQuantity;
        this.gameDuration = gameDuration;
    }

}