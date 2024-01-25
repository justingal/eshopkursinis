package com.coursework.eshop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Warehouse implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String address;
    @ManyToMany(mappedBy = "warehouses", cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Manager> managers;
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
        this.inStockPuzzles = new ArrayList<>();
        this.managers = new ArrayList<>();
    }

    @Override
    public String toString() {
        return title;
    }
}