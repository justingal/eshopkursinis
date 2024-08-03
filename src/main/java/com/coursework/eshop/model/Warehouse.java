package com.coursework.eshop.model;

import jakarta.persistence.*;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Warehouse implements Serializable {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Puzzle> getInStockPuzzles() {
        return inStockPuzzles;
    }

    public void setInStockPuzzles(List<Puzzle> inStockPuzzles) {
        this.inStockPuzzles = inStockPuzzles;
    }

    public List<BoardGame> getInStockBoardGames() {
        return inStockBoardGames;
    }

    public void setInStockBoardGames(List<BoardGame> inStockBoardGames) {
        this.inStockBoardGames = inStockBoardGames;
    }

    public List<Dice> getInStockDices() {
        return inStockDices;
    }

    public void setInStockDices(List<Dice> inStockDices) {
        this.inStockDices = inStockDices;
    }

    public Warehouse() {
    }

    public Warehouse(int id, String title, String address, List<Puzzle> inStockPuzzles, List<BoardGame> inStockBoardGames, List<Dice> inStockDices) {
        this.id = id;
        this.title = title;
        this.address = address;
        this.inStockPuzzles = inStockPuzzles;
        this.inStockBoardGames = inStockBoardGames;
        this.inStockDices = inStockDices;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String address;
    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Puzzle> inStockPuzzles;
    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<BoardGame> inStockBoardGames;
    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Dice> inStockDices;

    public Warehouse(String title, String address) {
        this.title = title;
        this.address = address;
    }

    @Override
    public String toString() {
        return title;
    }
}