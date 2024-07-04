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

    public void addProductToBoardGamesList(BoardGame boardGame, Dice dice, Puzzle puzzle) {
        if (boardGame != null) {
            if (inOrderBoardGames == null) {
                inOrderBoardGames = new ArrayList<>();
            }
            inOrderBoardGames.add(boardGame);
            if (puzzle != null) {
                if (inOrderPuzzles == null) {
                    inOrderPuzzles = new ArrayList<>();
                }
                inOrderPuzzles.add(puzzle);
            }
            if (dice != null) {
                if (inOrderDices == null) {
                    inOrderDices = new ArrayList<>();
                }
                inOrderDices.add(dice);
            }
        }
    }

    public boolean hasProduct(Product product) {
        if (product instanceof BoardGame && inOrderBoardGames != null) {
            return inOrderBoardGames.stream().anyMatch(p -> p.getId() == product.getId());
        } else if (product instanceof Puzzle && inOrderPuzzles != null) {
            return inOrderPuzzles.stream().anyMatch(p -> p.getId() == product.getId());
        } else if (product instanceof Dice && inOrderDices != null) {
            return inOrderDices.stream().anyMatch(p -> p.getId() == product.getId());
        }
        return false;
    } //chatgpt kodas

    public void addProduct(Product product) {
        if (product instanceof BoardGame) {
            if (inOrderBoardGames == null) {
                inOrderBoardGames = new ArrayList<>();
            }
            inOrderBoardGames.add((BoardGame) product);
        } else if (product instanceof Puzzle) {
            if (inOrderPuzzles == null) {
                inOrderPuzzles = new ArrayList<>();
            }
            inOrderPuzzles.add((Puzzle) product);
        } else if (product instanceof Dice) {
            if (inOrderDices == null) {
                inOrderDices = new ArrayList<>();
            }
            inOrderDices.add((Dice) product);
        }
        // Čia galite pridėti logiką atnaujinti bendrą krepšelio būseną
    }
    public List<Product> getProducts() {
        List<Product> allProducts = new ArrayList<>();
        if (inOrderBoardGames != null) {
            allProducts.addAll(inOrderBoardGames);
        }
        if (inOrderPuzzles != null) {
            allProducts.addAll(inOrderPuzzles);
        }
        if (inOrderDices != null) {
            allProducts.addAll(inOrderDices);
        }
        return allProducts;
    }
    public void clearCart() {
        inOrderPuzzles.clear();
        inOrderBoardGames.clear();
        inOrderDices.clear();
    }
    public void removeProduct(Product product) {
        if (product instanceof BoardGame && inOrderBoardGames != null) {
            inOrderBoardGames.remove(product);
        } else if (product instanceof Puzzle && inOrderPuzzles != null) {
            inOrderPuzzles.remove(product);
        } else if (product instanceof Dice && inOrderDices != null) {
            inOrderDices.remove(product);
        }

    }

    public double getTotalPrice() {
        double totalPrice = 0.0;
        for (Product product : getProducts()) {
            totalPrice += product.getPrice();
        }
        return totalPrice;
    }

}