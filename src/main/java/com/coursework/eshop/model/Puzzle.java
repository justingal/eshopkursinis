package com.coursework.eshop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Puzzle extends Product {

    private int piecesQuantity;
    private String puzzleSize;
    private String puzzleMaterial;

    public Puzzle(int id, String title, String description, String author, double price, int quantity, int piecesQuantity, String puzzleSize, String puzzleMaterial) {
        super(id, title, description, author, price, quantity);
        this.piecesQuantity = piecesQuantity;
        this.puzzleSize = puzzleSize;
        this.puzzleMaterial = puzzleMaterial;
    }

    public Puzzle(String title, String description, String author, double price, int quantity, int piecesQuantity, String puzzleSize, String puzzleMaterial) {
        super(title, description, author, price, quantity);
        this.piecesQuantity = piecesQuantity;
        this.puzzleSize = puzzleSize;
        this.puzzleMaterial = puzzleMaterial;
    }

    public Puzzle(String title, String description, String author, Warehouse warehouse, double price, int quantity, int piecesQuantity, String puzzleSize, String puzzleMaterial) {
        super(title, description, author, warehouse, price, quantity);
        this.piecesQuantity = piecesQuantity;
        this.puzzleSize = puzzleSize;
        this.puzzleMaterial = puzzleMaterial;
    }
}