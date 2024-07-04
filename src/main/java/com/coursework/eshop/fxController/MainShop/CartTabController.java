package com.coursework.eshop.fxController.MainShop;

import com.coursework.eshop.HibernateControllers.CustomHib;
import com.coursework.eshop.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;

import java.util.List;

public class CartTabController {


    @FXML
    public ListView <Product> cartList;
    @FXML
    public Label priceLabel;
    private CustomHib customHib;

    private Cart cart;
    public List<Product> productCart;
    public void setData(CustomHib customHib) {
        this.customHib = customHib;
    }
    public void updateCartView() {
        if (cart != null) {
            cartList.getItems().clear();
            cartList.getItems().addAll(cart.getProducts()); // Atnaujinti produktų sąrašą
            priceLabel.setText(String.valueOf(cart.getTotalPrice())); // Atnaujinti bendrą kainą
        }
    }


    public void setCart(Cart cart) {
            this.cart = cart;

    }

    public void removeFromCart() {
            Product selectedProduct = cartList.getSelectionModel().getSelectedItem();
            if (selectedProduct != null && cart != null) {
                cart.removeProduct(selectedProduct);
                updateCartView();
            }
    }

    public void updateBoardGameQuantity(BoardGame boardGame, int quantity) {
        BoardGame boardGameInDb = customHib.getEntityById(BoardGame.class, boardGame.getId());
        if (boardGameInDb != null) {
            int newQuantity = boardGameInDb.getQuantity() - quantity;
            boardGameInDb.setQuantity(newQuantity);
            customHib.update(boardGameInDb);
        }
    }

    public void updatePuzzleQuantity(Puzzle puzzle, int quantity) {
        Puzzle puzzleInDb = customHib.getEntityById(Puzzle.class, puzzle.getId());
        if (puzzleInDb != null) {
            int newQuantity = puzzleInDb.getQuantity() - quantity;
            puzzleInDb.setQuantity(newQuantity);
            customHib.update(puzzleInDb);
        }
    }
    public void updateDiceQuantity(Dice dice, int quantity) {
        Dice diceInDb = customHib.getEntityById(Dice.class, dice.getId());
        if (diceInDb != null) {
            int newQuantity = diceInDb.getQuantity() - quantity;
            diceInDb.setQuantity(newQuantity);
            customHib.update(diceInDb);
        }
    }

    public void moveCartToOrder() {
        if (cart != null) {
            List<Product> productsInCart = cart.getProducts();
            for (Product product : productsInCart) {
                updateBoardGameQuantity((BoardGame) product, 1);
                updatePuzzleQuantity((Puzzle) product, 1);
                updateDiceQuantity((Dice) product, 1);
            }
            cart.clearCart();
            updateCartView();
        }
    }
}

