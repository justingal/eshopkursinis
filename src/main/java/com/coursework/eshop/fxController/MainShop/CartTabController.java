package com.coursework.eshop.fxController.MainShop;

import com.coursework.eshop.HibernateControllers.CustomHib;
import com.coursework.eshop.model.ShoppingCart;
import com.coursework.eshop.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class CartTabController {

    @FXML
    public ListView cartItemList;
    @FXML
    public Label priceLabel;
    private ShoppingCart cart;



    public void moveCartToOrder(ActionEvent actionEvent) {

    }

    public void removeFromCart(ActionEvent actionEvent) {
    }

    public void setData(CustomHib customHib, ShoppingCart cart) {
        this.cart = cart;
        loadCartItemList();
    }

    private void loadCartItemList() {
        cartItemList.getItems().clear();
        cartItemList.getItems().addAll(cart.getItems());
    }
}
