package com.coursework.eshop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDate dateCreated;
    @ManyToOne
    private Customer customer;
    @ManyToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Puzzle> inOrderPuzzles;
    @ManyToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<BoardGame> inOrderBoardGames;
    @ManyToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Dice> inOrderDices;

    public void addProductToBoardGamesList(BoardGame boardGame) {
        if (boardGame != null) {
            if (inOrderBoardGames == null) {
                inOrderBoardGames = new ArrayList<>();
            }
            inOrderBoardGames.add(boardGame);
        }
    }

}