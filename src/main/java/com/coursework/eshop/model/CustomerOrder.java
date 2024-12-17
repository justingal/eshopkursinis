package com.coursework.eshop.model;

import jakarta.persistence.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer_order")
public class CustomerOrder implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDate dateCreated;
    private double totalPrice;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Manager responsibleManager;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Embedded
    private ShoppingCart cart = new ShoppingCart();

    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "order_id")
    private List<BoardGame> inOrderBoardGames;
    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "order_id")
    private List<Puzzle> inOrderPuzzles;
    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "order_id")
    private List<Dice> inOrderDices;

    @OneToMany(mappedBy = "customerOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> orderChat;

    public CustomerOrder() {
    }

    public CustomerOrder(int id, LocalDate dateCreated, double totalPrice, Customer customer, OrderStatus orderStatus, ShoppingCart cart, List<BoardGame> inOrderBoardGames, List<Puzzle> inOrderPuzzles, List<Dice> inOrderDices) {
        this.id = id;
        this.dateCreated = dateCreated;
        this.totalPrice = totalPrice;
        this.customer = customer;
        this.orderStatus = orderStatus;
        this.cart = cart;
        this.inOrderBoardGames = inOrderBoardGames;
        this.inOrderPuzzles = inOrderPuzzles;
        this.inOrderDices = inOrderDices;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<BoardGame> getInOrderBoardGames() {
        return inOrderBoardGames;
    }

    public void setInOrderBoardGames(List<BoardGame> inOrderBoardGames) {
        this.inOrderBoardGames = inOrderBoardGames;
    }

    public List<Puzzle> getInOrderPuzzles() {
        return inOrderPuzzles;
    }

    public void setInOrderPuzzles(List<Puzzle> inOrderPuzzles) {
        this.inOrderPuzzles = inOrderPuzzles;
    }

    public List<Dice> getInOrderDices() {
        return inOrderDices;
    }

    public void setInOrderDices(List<Dice> inOrderDices) {
        this.inOrderDices = inOrderDices;
    }

    public Manager getResponsibleManager() {
        return responsibleManager;
    }

    public void setResponsibleManager(Manager responsibleManager) {
        this.responsibleManager = responsibleManager;
    }

    public List<Comment> getOrderChat() {
        return orderChat;
    }

    public void setOrderChat(List<Comment> orderChat) {
        this.orderChat = orderChat;
    }

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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
