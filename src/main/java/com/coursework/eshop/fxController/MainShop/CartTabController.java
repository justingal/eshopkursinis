package com.coursework.eshop.fxController.MainShop;

import com.coursework.eshop.HibernateControllers.CustomHib;
import com.coursework.eshop.model.Cart;
import com.coursework.eshop.model.Product;
import com.coursework.eshop.model.Warehouse;
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
                cart.removeProduct(selectedProduct); // Pašalinti produktą iš krepšelio
                updateCartView(); // Atnaujinti krepšelio vaizdą
            }
    }
}

