package com.coursework.eshop.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDate dateCreated;
/*
    @ManyToOne
    private User customer;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();
    private double totalPrice;

    public Order(User customer, List<CartItem> items) {
        this.dateCreated = LocalDate.now();
        this.customer = customer;
        this.items = items;
        this.totalPrice = items.stream().mapToDouble(CartItem::getPrice).sum();
    }*/
}