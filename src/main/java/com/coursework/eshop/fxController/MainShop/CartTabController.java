package com.coursework.eshop.fxController.MainShop;

import com.coursework.eshop.HibernateControllers.CustomHib;
import com.coursework.eshop.StartGui;
import com.coursework.eshop.fxController.JavaFxCustomsUtils;
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

        if (user instanceof Customer) {
            Customer customer = (Customer) user;

            if (cart == null) {
                JavaFxCustomsUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Shopping Cart Error", "Shopping cart is not available.");
                return;
            }

            try {
                CustomerOrder customerOrder = new CustomerOrder();
                customerOrder.setCustomer(customer);
                customerOrder.setDateCreated(LocalDate.now());
                customerOrder.setOrderStatus(OrderStatus.NEW);

                customerOrder.setInOrderBoardGames(new ArrayList<>());
                customerOrder.setInOrderPuzzles(new ArrayList<>());
                customerOrder.setInOrderDices(new ArrayList<>());

                double totalPrice = 0.0;

                for (CartItem item : cart.getItems()) {
                    int productId = item.getProductId();

                    BoardGame boardGame = customHib.getEntityById(BoardGame.class, productId);
                    if (boardGame != null) {
                        customerOrder.getInOrderBoardGames().add(boardGame);
                        totalPrice += boardGame.getPrice();
                        continue;
                    }

                    Puzzle puzzle = customHib.getEntityById(Puzzle.class, productId);
                    if (puzzle != null) {
                        customerOrder.getInOrderPuzzles().add(puzzle);
                        totalPrice += puzzle.getPrice();
                        continue;
                    }

                    Dice dice = customHib.getEntityById(Dice.class, productId);
                    if (dice != null) {
                        customerOrder.getInOrderDices().add(dice);
                        totalPrice += dice.getPrice();
                    }
                }

                customerOrder.setTotalPrice(totalPrice);

                customHib.create(customerOrder);

                cart.getItems().clear();

                JavaFxCustomsUtils.generateAlert(Alert.AlertType.INFORMATION, "Success", "Order Created", "Your order has been successfully created.");

            } catch (Exception e) {
                JavaFxCustomsUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Order Creation Error", "There was an error creating the order: " + e.getMessage());
            }
        } else {
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.ERROR, "Error", "User Error", "Current user is not a customer.");
        }
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
            priceLabel.setText(String.format("%.2f €", 0.0));
        }


    public void setData(CustomHib customHib, ShoppingCart cart) {
        this.customHib = customHib;
        this.cart = cart;
        loadCartItemList();
    }

    private void loadCartItemList() {
        cartItemList.getItems().clear();
        cartItemList.getItems().addAll(cart.getItems());
        priceLabel.setText(String.format("%.2f €", 0.0));
    }
}
