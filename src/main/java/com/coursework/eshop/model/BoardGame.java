package com.coursework.eshop.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BoardGame extends Product {

    private String playersQuantity;
    private String gameDuration;

    public BoardGame(int id, String title, String description, String author, String playersQuantity, String gameDuration) {
        super(id, title, description, author);
        this.playersQuantity = playersQuantity;
        this.gameDuration = gameDuration;
    }

    public BoardGame(String title, String description, String author, String playersQuantity, String gameDuration) {
        super(title, description, author);
        this.playersQuantity = playersQuantity;
        this.gameDuration = gameDuration;
    }

    public BoardGame(String title, String description, String author, Warehouse warehouse, String playersQuantity, String gameDuration) {
        super(title, description, author, warehouse);
        this.playersQuantity = playersQuantity;
        this.gameDuration = gameDuration;
    }
    @Override
    public String toString() {
        return title;
    }
}