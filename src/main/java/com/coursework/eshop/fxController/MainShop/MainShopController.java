package com.coursework.eshop.fxController.MainShop;


import com.coursework.eshop.HibernateControllers.CustomHib;
import com.coursework.eshop.HibernateControllers.EntityManagerFactorySingleton;
import com.coursework.eshop.StartGui;
import com.coursework.eshop.fxController.JavaFxCustomsUtils;
import com.coursework.eshop.fxController.RegistrationController;
import com.coursework.eshop.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

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
    @FXML
    private RegistrationController RegistrationTab;

    private EntityManagerFactory entityManagerFactory = EntityManagerFactorySingleton.getEntityManagerFactory();
    private User currentUser = StartGui.currentUser;
    private CustomHib customHib;

    private ShoppingCart cart = new ShoppingCart();

    private Parent myOrdersTabContent;
    private boolean myOrdersTabLoaded = false;
    private boolean ordersTabLoaded = false;

    public void setData() {
        limitAccess();
        loadData();
    }

    private void loadData() {
        customHib = new CustomHib();
        loadMainShopProductList();
    }


    private void loadMainShopProductList() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            String boardGameQuery = "SELECT bg FROM BoardGame bg WHERE bg.id NOT IN " +
                    "(SELECT bg.id FROM CustomerOrder co JOIN co.inOrderBoardGames bg)";

            String puzzleQuery = "SELECT p FROM Puzzle p WHERE p.id NOT IN " +
                    "(SELECT p.id FROM CustomerOrder co JOIN co.inOrderPuzzles p)";


            String diceQuery = "SELECT d FROM Dice d WHERE d.id NOT IN " +
                    "(SELECT d.id FROM CustomerOrder co JOIN co.inOrderDices d)";

            List<BoardGame> availableBoardGames = entityManager.createQuery(boardGameQuery, BoardGame.class).getResultList();
            List<Puzzle> availablePuzzles = entityManager.createQuery(puzzleQuery, Puzzle.class).getResultList();
            List<Dice> availableDices = entityManager.createQuery(diceQuery, Dice.class).getResultList();

            productList.getItems().clear();

            productList.getItems().addAll(availableBoardGames);
            productList.getItems().addAll(availablePuzzles);
            productList.getItems().addAll(availableDices);

        } catch (Exception e) {
            JavaFxCustomsUtils.generateAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Failed to load products",
                    "An error occurred while loading the product list: " + e.getMessage()
            );
        } finally {
            entityManager.close();
        }
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
        if (currentUser.getClass() == Admin.class) {
        } else if (currentUser.getClass() == Manager.class) {
            tabPane.getTabs().remove(usersTab);
            tabPane.getTabs().remove(myOrdersTab);
        } else if (currentUser.getClass() == Customer.class) {
            tabPane.getTabs().remove(usersTab);
            tabPane.getTabs().remove(warehousesTab);
            tabPane.getTabs().remove(productsTab);
            tabPane.getTabs().remove(ordersTab);
        }
    }

    public void loadTabValues() throws IOException {
        if (primaryTab.isSelected()) {
            loadMainShopProductList();
        } else if (productsTab.isSelected()) {
            productTabController.setData(customHib);
        } else if (warehousesTab.isSelected()) {
            warehouseTabController.setData(customHib);
        } else if (commentsTab.isSelected()) {
            commentTabController.setData(customHib);
        } else if (usersTab.isSelected()) {
            userTabController.setData(customHib);
        } else if (cartsTab.isSelected()) {
            cartTabController.setData(customHib, cart);
        } else if (myOrdersTab.isSelected()) {
            if (!myOrdersTabLoaded) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("myOrderTab.fxml"));
                    myOrdersTabContent = fxmlLoader.load();
                    myOrdersTabController = fxmlLoader.getController();
                    myOrdersTab.setContent(myOrdersTabContent);
                    myOrdersTabLoaded = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            myOrdersTabController.setData(customHib);
        } else if (ordersTab.isSelected()) {
            if (!ordersTabLoaded) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("orderTab.fxml"));
                    Parent parent = fxmlLoader.load();
                    orderTabController = fxmlLoader.getController();
                    ordersTab.setContent(parent);
                    ordersTabLoaded = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            orderTabController.setData(customHib);
            }
        }


    public void leaveReview() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("commentTree.fxml"));
        Parent parent = fxmlLoader.load();
        CommentTreeController commentTree = fxmlLoader.getController();
        commentTree.setData(customHib);
        var stage = new Stage();
        if (primaryTab.isSelected()) {
            stage = (Stage) productList.getScene().getWindow();
        }
        Scene scene = new Scene(parent);
        stage.setTitle("Shop");
        stage.setScene(scene);
        stage.show();
    }

    public void setUser(User user) {
        this.currentUser = user;
    }

    public void addProductToCart(ActionEvent actionEvent) {
        Product selectedProduct = productList.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.WARNING, "No Selection", "No product selected", "Please select a product to add to the cart.");
            return;
        }
        CartItem item = new CartItem(selectedProduct.getId(), selectedProduct.getTitle(), selectedProduct.getPrice());
        cart.addItem(item);

    }

    public void initialize() {
        setUser(StartGui.currentUser);
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                loadTabValues();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}