package com.coursework.eshop.fxController.MainShop;

import com.coursework.eshop.HibernateControllers.CustomHib;
import com.coursework.eshop.fxController.JavaFxCustomsUtils;
import com.coursework.eshop.model.CartItem;
import com.coursework.eshop.model.ShoppingCart;
import com.coursework.eshop.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class CartTabController {

    @FXML
    public ListView<CartItem> cartItemList;
    @FXML
    public Label priceLabel;
    private ShoppingCart cart;



    public void moveCartToOrder(ActionEvent actionEvent) {

    }

    public void removeFromCart(ActionEvent actionEvent) {
                    // Assuming 'cartItemList' is a ListView of CartItem and 'cart' is your ShoppingCart object
            CartItem selectedItem = cartItemList.getSelectionModel().getSelectedItem();

            if (selectedItem == null) {
                // Show an alert or some indication that no item is selected
                JavaFxCustomsUtils.generateAlert(Alert.AlertType.ERROR, "No Selection", "No item selected", "Please select an item to remove.");
                return;
            }

            // Remove the selected item from the cart
            cart.removeItem(selectedItem.getProductId());

            // Update the ListView display
            loadCartItemList();

            // Update the total price label
            priceLabel.setText(String.format("%.2f", cart.getTotalPrice()));
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
