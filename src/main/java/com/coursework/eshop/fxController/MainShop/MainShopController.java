package com.coursework.eshop.fxController.MainShop;


import com.coursework.eshop.HibernateControllers.CustomHib;
import com.coursework.eshop.StartGui;
import com.coursework.eshop.fxController.JavaFxCustomsUtils;
import com.coursework.eshop.model.*;
import jakarta.persistence.EntityManagerFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class MainShopController {

    public ListView<Product> productList;

    @FXML
    public Tab usersTab;
    @FXML
    public Tab warehousesTab;

    @FXML
    public Tab primaryTab;

    @FXML
    public TabPane tabPane;
    @FXML
    public Tab productsTab;
    @FXML
    public Tab myOrdersTab;
    @FXML
    public Tab ordersTab;
    @FXML
    public Tab commentsTab;
    @FXML
    public Tab cartsTab;
    @FXML
    public Tab settingsTab;
    public TextField titleField;
    public TextField authorField;
    public TextArea descriptionField;
    public TextField typeField;
    public TextField priceField;
    public TextField quantityField;



    @FXML
    private WarehouseTabController warehouseTabController;

    @FXML
    private CommentTabController commentTabController;

    @FXML
    private ProductTabController productTabController;

    @FXML
    private UserTabController userTabController;

    @FXML
    private CartTabController cartTabController;

    @FXML
    private OrderTabController orderTabController;

    @FXML
    private SettingsTabController settingsTabController;

    @FXML
    private MyOrdersTabController myOrdersTabController;

    private EntityManagerFactory entityManagerFactory;
    private User currentUser;
    private CustomHib customHib;

    private ShoppingCart cart = new ShoppingCart();

    public void setData(EntityManagerFactory entityManagerFactory, User user) {
        this.entityManagerFactory = entityManagerFactory;
        this.currentUser = user;
        limitAccess();
        loadData();
    }

    private void loadData() {
        customHib = new CustomHib(entityManagerFactory);
        loadProductList();
    }

    private void loadProductList() {
        productList.getItems().clear();
        productList.getItems().addAll(customHib.getAllRecords(BoardGame.class));
        productList.getItems().addAll(customHib.getAllRecords(Puzzle.class));
        productList.getItems().addAll(customHib.getAllRecords(Dice.class));
    }


    public void loadProductFields() {
        Product selectedProduct = productList.getSelectionModel().getSelectedItem();

        if (selectedProduct != null) {
            titleField.setText(selectedProduct.getTitle());
            priceField.setText(String.valueOf(selectedProduct.getPrice()));
            authorField.setText(selectedProduct.getAuthor());
            if (selectedProduct instanceof BoardGame) {
                BoardGame boardGame = (BoardGame) selectedProduct;
                descriptionField.setText("Type: Board game " + "\r\n" + "Description: " + (selectedProduct.getDescription()) + "\r\n" + "Players number: " + (boardGame.getPlayersQuantity()) + "\r\n" + "Game duration: " + (boardGame.getGameDuration()));
            } else if (selectedProduct instanceof Puzzle) {
                Puzzle puzzle = (Puzzle) selectedProduct;
                descriptionField.setText("Type: Puzzle " + "\r\n" + "Description: " + (selectedProduct.getDescription()) + "\r\n" + "Puzzle pieces quantity: " + (String.valueOf(puzzle.getPiecesQuantity()) + "\r\n" + "Puzzle material: " + (puzzle.getPuzzleMaterial()) + "\r\n" + "PuzzleSize: " + (puzzle.getPuzzleSize())));
            } else {
                Dice dice = (Dice) selectedProduct;
                descriptionField.setText("Type: Dice " + "\r\n" + "Description: " + (selectedProduct.getDescription()) + "\r\n" + "Dice number: " + (String.valueOf(dice.getDiceNumber())));
            }
        }
    }

    private void limitAccess() {
        if (currentUser.getClass() == Manager.class) {
            tabPane.getTabs().remove(usersTab);
            tabPane.getTabs().remove(myOrdersTab);
            tabPane.getTabs().remove(cartsTab);
        } else if (currentUser.getClass() == Customer.class) {
            tabPane.getTabs().remove(usersTab);
            tabPane.getTabs().remove(warehousesTab);
            tabPane.getTabs().remove(productsTab);
        }
    }

    public void loadTabValues() {
        if (primaryTab.isSelected()) {
            loadProductList();
        } else if (productsTab.isSelected()) {
            productTabController.setData(customHib);
        } else if (warehousesTab.isSelected()) {
            warehouseTabController.setData(customHib);
        } else if (commentsTab.isSelected()) {
            commentTabController.setData(customHib, currentUser);
        } else if (usersTab.isSelected()) {
            userTabController.setData(customHib);
        } else if (cartsTab.isSelected()) {
            cartTabController.setData(customHib, cart);
        }
    }

    public void leaveReview() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("commentTree.fxml"));
        Parent parent = fxmlLoader.load();
        CommentTreeController commentTree = fxmlLoader.getController();
        commentTree.setData(customHib, currentUser);
        var stage = new Stage();
        if (primaryTab.isSelected()) {
            stage = (Stage) productList.getScene().getWindow();
        }
        Scene scene = new Scene(parent);
        stage.setTitle("Shop");
        stage.setScene(scene);
        stage.show();
    }

    public void addProductToCart(ActionEvent actionEvent) {
        Product selectedProduct = productList.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.WARNING, "No Selection", "No product selected", "Please select a product to add to the cart.");
            return;
        }
        CartItem item = new CartItem(selectedProduct.getId(), selectedProduct.getTitle(), selectedProduct.getPrice());
        // Might need to make ShoppingCart static or else the products will keep getting wiped
        cart.addItem(item);

    }
}