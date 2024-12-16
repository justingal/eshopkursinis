package com.coursework.eshop.model;

import jakarta.persistence.*;

import java.util.List;


@Entity
public class BoardGame extends Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_gen")
    private int id;
    @ManyToOne
    private CustomerOrder customerOrder;

    @OneToMany(mappedBy = "boardGame", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Review> reviews;
    private String playersQuantity;
    private String gameDuration;

    public BoardGame(String title, String description, String author, Warehouse warehouse, double price) {
        super(title, description, author, warehouse, price);
    }

    public BoardGame() {

    }
    public BoardGame(String title, String description, String author, Warehouse warehouse, double price, String playersQuantity, int id, String gameDuration, List<Review> reviews) {
        super(title, description, author, warehouse, price);
        this.playersQuantity = playersQuantity;
        this.id = id;
        this.gameDuration = gameDuration;
        this.reviews = reviews;
    }


    public BoardGame(int id, String title, String description, String author, int price, String playersQuantity, String gameDuration) {
        super(id, title, description, author, price);
        this.playersQuantity = playersQuantity;
        this.gameDuration = gameDuration;
    }

    public BoardGame(String title, String description, String author, double price, String playersQuantity, String gameDuration) {
        super(title, description, author, price);
        this.playersQuantity = playersQuantity;
        this.gameDuration = gameDuration;
    }

    public BoardGame(String title, String description, String author, Warehouse warehouse, double price, String playersQuantity, String gameDuration) {
        super(title, description, author, warehouse, price);
        this.playersQuantity = playersQuantity;
        this.gameDuration = gameDuration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlayersQuantity() {
        return playersQuantity;
    }

    public void setPlayersQuantity(String playersQuantity) {
        this.playersQuantity = playersQuantity;
    }

    public String getGameDuration() {
        return gameDuration;
    }

    public void setGameDuration(String gameDuration) {
        this.gameDuration = gameDuration;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    @Override
    public void removeFromWarehouse() {
        if (getWarehouse() != null) {
            getWarehouse().getInStockBoardGames().remove(this);
        }
    }


}