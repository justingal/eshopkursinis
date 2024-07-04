package com.coursework.eshop.fxController.MainShop;


import com.coursework.eshop.HibernateControllers.CustomHib;
import com.coursework.eshop.fxController.JavaFxCustomsUtils;
import com.coursework.eshop.model.*;
import jakarta.persistence.EntityManagerFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

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
    public Tab commentsTab;
    @FXML
    public Tab cartsTab;
    public TextField titleField;
    public TextField authorField;
    public TextArea descriptionField;
    public TextField typeField;
    public TextField priceField;
    public TextField quantityField;


    @FXML
    private WarehouseTabController warehouseTabController;

    @FXML
    private CartTabController cartTabController;

    @FXML
    private CommentTabController commentTabController;

    @FXML
    private ProductTabController productTabController;

    @FXML
    private UserTabController userTabController;

    private EntityManagerFactory entityManagerFactory;
    private User currentUser;
    private CustomHib customHib;

    private List<Product> productCart;

    public void setData(EntityManagerFactory entityManagerFactory, User user) {
        this.entityManagerFactory = entityManagerFactory;
        this.currentUser = user;
        this.cart = new Cart();
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
                descriptionField.setText("Type: Board game "+ "\r\n"+ "Description: "+(selectedProduct.getDescription())+ "\r\n"+"Players number: "+(boardGame.getPlayersQuantity())+ "\r\n"+"Game duration: "+(boardGame.getGameDuration()));
            } else if (selectedProduct instanceof Puzzle) {
                Puzzle puzzle = (Puzzle) selectedProduct;
                descriptionField.setText("Type: Puzzle "+ "\r\n"+ "Description: "+(selectedProduct.getDescription())+ "\r\n"+"Puzzle pieces quantity: "+(String.valueOf(puzzle.getPiecesQuantity())+ "\r\n"+"Puzzle material: "+(puzzle.getPuzzleMaterial())+ "\r\n"+"PuzzleSize: "+(puzzle.getPuzzleSize())));
            } else {
                Dice dice = (Dice) selectedProduct;
                descriptionField.setText("Type: Board game "+ "\r\n"+ "Description: "+(selectedProduct.getDescription())+ "\r\n"+"Dice number: "+(String.valueOf(dice.getDiceNumber())));
            }
        }
    }

    private void limitAccess() {
        if(currentUser.getClass()== Manager.class){
            Manager manager = (Manager) currentUser;
        }else if(currentUser.getClass()== Customer.class){
                tabPane.getTabs().remove(usersTab);
                tabPane.getTabs().remove(warehousesTab);
                tabPane.getTabs().remove(productsTab);
        }
    }

    public void loadTabValues() {
        if (primaryTab.isSelected()) {
            loadProductList();
        }
        else if (productsTab.isSelected()) {
            productTabController.setData(customHib);
        }else if( warehousesTab.isSelected()){
            warehouseTabController.setData(customHib);
        }else if( usersTab.isSelected()){
            userTabController.setData(customHib);
        }else if( commentsTab.isSelected()){
            commentTabController.setData(customHib, currentUser);
        }
        else if(cartsTab.isSelected()) {
            cartTabController.setData(customHib);
        }
    }

    private Cart cart = new Cart();
    public void addProductToCart() {
        Product selectedProduct = productList.getSelectionModel().getSelectedItem();
        if (selectedProduct != null && !cart.hasProduct(selectedProduct)) {
            cart.addProduct(selectedProduct); // Pridėti produktą į krepšelį, jei jo dar nėra
            cartTabController.setCart(cart); // Perduoti Cart objektą į CartTabController
            cartTabController.updateCartView(); // Atnaujinti krepšelio vaizdą
        }
    }

}