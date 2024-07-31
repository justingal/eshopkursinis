package com.coursework.eshop.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BoardGame extends Product {

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