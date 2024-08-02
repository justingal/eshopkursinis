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
@Table(name = "customer_order")
public class CustomerOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDate dateCreated;
    private double totalPrice;

    @ManyToOne
    private Customer customer;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Embedded
    private ShoppingCart cart = new ShoppingCart();

    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<BoardGame> inOrderBoardGames;
    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Puzzle> inOrderPuzzles;
    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Dice> inOrderDices;

    public ShoppingCart getCart() {
        return cart;
    }

    public void setCart(ShoppingCart cart) {
        this.cart = cart;
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        if (inOrderPuzzles != null) products.addAll(inOrderPuzzles);
        if (inOrderBoardGames != null) products.addAll(inOrderBoardGames);
        if (inOrderDices != null) products.addAll(inOrderDices);
        return products;
    }
}
