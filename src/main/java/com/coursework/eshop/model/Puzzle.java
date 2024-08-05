package com.coursework.eshop.model;

import jakarta.persistence.*;

import java.util.List;


@Entity
public class Puzzle extends Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_gen")
    private int id;

    private int piecesQuantity;
    private String puzzleSize;
    private String puzzleMaterial;

    @OneToMany(mappedBy = "puzzle", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Review> reviews;
    @ManyToOne
    private CustomerOrder customerOrder;

    public Puzzle() {

    }

    public Puzzle(String title, String description, String author, Warehouse warehouse, double price) {
        super(title, description, author, warehouse, price);
    }

    public Puzzle(String title, String description, String author, Warehouse warehouse, double price, int id, int piecesQuantity, String puzzleSize, String puzzleMaterial, List<Review> reviews) {
        super(title, description, author, warehouse, price);
        this.id = id;
        this.piecesQuantity = piecesQuantity;
        this.puzzleSize = puzzleSize;
        this.puzzleMaterial = puzzleMaterial;
        this.reviews = reviews;
    }


    public Puzzle(int id, String title, String description, String author, double price, int piecesQuantity, String puzzleSize, String puzzleMaterial) {
        super(id, title, description, author, price);
        this.piecesQuantity = piecesQuantity;
        this.puzzleSize = puzzleSize;
        this.puzzleMaterial = puzzleMaterial;
    }

    public Puzzle(String title, String description, String author, double price, int piecesQuantity, String puzzleSize, String puzzleMaterial) {
        super(title, description, author, price);
        this.piecesQuantity = piecesQuantity;
        this.puzzleSize = puzzleSize;
        this.puzzleMaterial = puzzleMaterial;
    }

    public Puzzle(String title, String description, String author, Warehouse warehouse, double price, int piecesQuantity, String puzzleSize, String puzzleMaterial) {
        super(title, description, author, warehouse, price);
        this.piecesQuantity = piecesQuantity;
        this.puzzleSize = puzzleSize;
        this.puzzleMaterial = puzzleMaterial;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public int getPiecesQuantity() {
        return piecesQuantity;
    }

    public void setPiecesQuantity(int piecesQuantity) {
        this.piecesQuantity = piecesQuantity;
    }

    public String getPuzzleSize() {
        return puzzleSize;
    }

    public void setPuzzleSize(String puzzleSize) {
        this.puzzleSize = puzzleSize;
    }

    public String getPuzzleMaterial() {
        return puzzleMaterial;
    }

    public void setPuzzleMaterial(String puzzleMaterial) {
        this.puzzleMaterial = puzzleMaterial;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }


}