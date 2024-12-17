package com.coursework.eshop.fxController.MainShop;

import com.coursework.eshop.HibernateControllers.CustomHib;
import com.coursework.eshop.StartGui;
import com.coursework.eshop.fxController.JavaFxCustomsUtils;
import com.coursework.eshop.fxController.MainShop.Util.OrderService;
import com.coursework.eshop.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.time.LocalDate;
import java.util.ArrayList;

public class CartTabController {

    @FXML
    public ListView<CartItem> cartItemList;
    @FXML
    public Label priceLabel;
    private ShoppingCart cart;
    private CustomHib customHib;


    @FXML
    public void moveCartToOrder(ActionEvent actionEvent) {
        User user = StartGui.currentUser;

        if (!OrderService.isValidUser(user)) {
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.ERROR, "Error", "User Error", "Current user is not a customer.");
            return;
        }

        if (cart == null) {
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Shopping Cart Error", "Shopping cart is not available.");
            return;
        }

        try {
            OrderService orderService = new OrderService(customHib, cart);
            CustomerOrder customerOrder = orderService.createOrder((Customer) user);

            clearCartAndRefreshUI();
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.INFORMATION, "Success", "Order Created", "Your order has been successfully created.");
        } catch (Exception e) {
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Order Creation Error", "There was an error creating the order: " + e.getMessage());
        }
    }

    private void clearCartAndRefreshUI() {
        cart.getItems().clear();
        updatePriceLabel();
    }

    public void removeFromCart(ActionEvent actionEvent) {
        CartItem selectedItem = cartItemList.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.ERROR, "No Selection", "No item selected", "Please select an item to remove.");
            return;
        }
        cart.removeItem(selectedItem.getProductId());
        loadCartItemList();
    }

    public void setData(CustomHib customHib, ShoppingCart cart) {
        this.customHib = customHib;
        this.cart = cart;
        if (cart != null) {
            loadCartItemList();
        }
    }

    private void loadCartItemList() {
        cartItemList.getItems().clear();
        cartItemList.getItems().addAll(cart.getItems());
        updatePriceLabel();
    }


    private void updatePriceLabel() {
        double totalPrice = cart.getItems().stream().mapToDouble(CartItem::getPrice).sum();
        priceLabel.setText(String.format("%.2f €", totalPrice));
    }
}
