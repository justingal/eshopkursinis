package com.coursework.eshop.fxController;


import com.coursework.eshop.HibernateControllers.GenericHib;
import com.coursework.eshop.model.*;
import jakarta.persistence.EntityManagerFactory;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainShopController  {


    public ListView<Product> productList;
@FXML
    public ListView<Cart> currentOrder;
    @FXML
    public Tab primaryTab;
    @FXML
    public Tab usersTab;
    @FXML
    public Tab warehouseTab;
    @FXML
    public Tab ordersTab;
    @FXML
    public ListView<Warehouse>warehouseList;
    @FXML
    public TextField addressWarehouseField;
    @FXML
    public TextField titleWarehouseField;
    @FXML
    public TableView customerTable;
    @FXML
    public TableView managerTable;
    @FXML
    public TabPane tabPane;
    @FXML
    public Tab productsTab;
    public ListView<Product> productListManager;
    @FXML
    public TextField productTitleField;
    @FXML
    public TextArea descriptionField;
    @FXML
    public TextField authorField;
    @FXML
    public ComboBox <ProductType> productType;
    @FXML
    public ComboBox <Warehouse>  warehouseComboBox;
    @FXML
    public TextField gameDurationFIeld;
    @FXML
    public TextField playersQuantityField;
    @FXML
    public TextField piecesQuantityField;
    @FXML
    public TextField puzzleMaterialField;
    @FXML
    public TextField puzzleSizeField;


    private EntityManagerFactory entityManagerFactory;
    User currentUser;
    private GenericHib genericHib;

    public void initialize() {
        productType.getItems().addAll(ProductType.values());
    }

    public void setData(EntityManagerFactory entityManagerFactory, User user) {
        this.entityManagerFactory = entityManagerFactory;
        this.currentUser = user;
        limitAccess();
        loadData();
    }

    private void loadData() {
        genericHib = new GenericHib(entityManagerFactory);
        productList.getItems().addAll(genericHib.getAllRecords(Product.class));
    }

    private void limitAccess() {
        if(currentUser.getClass()== Manager.class){
            Manager manager = (Manager) currentUser;
            if(!manager.isAdmin()) {
                managerTable.setDisable(true);
            }
        }else if(currentUser.getClass()== Customer.class){
                tabPane.getTabs().remove(usersTab);
                tabPane.getTabs().remove(warehouseTab);
                tabPane.getTabs().remove(productsTab);
        }
    }

    public void leaveComment(ActionEvent actionEvent) {
    }

    public void addToCart(ActionEvent actionEvent) {
    }

    public void loadTabValues() {
        if (productsTab.isSelected()) {

            warehouseComboBox.getItems().clear();
            warehouseComboBox.getItems().addAll(genericHib.getAllRecords(Warehouse.class));
        }else if( warehouseTab.isSelected()){
            loadWarehouseList();
            warehouseList.getSelectionModel().select(0);
            Warehouse selectedWarehouse = warehouseList.getSelectionModel().getSelectedItem();
            if (selectedWarehouse != null) {
                titleWarehouseField.setText(selectedWarehouse.getTitle());
                addressWarehouseField.setText(selectedWarehouse.getAddress());
            }
        }
    }

    public void enableProductField(ActionEvent actionEvent) {
        if(productType.getSelectionModel().getSelectedItem() == ProductType.BOARD_GAME){
            gameDurationFIeld.setDisable(false);
            playersQuantityField.setDisable(false);
            piecesQuantityField.setDisable(true);
            puzzleMaterialField.setDisable(true);
            puzzleSizeField.setDisable(true);
        }
        else if(productType.getSelectionModel().getSelectedItem() == ProductType.PUZZLE){
            gameDurationFIeld.setDisable(true);
            playersQuantityField.setDisable(true);
            piecesQuantityField.setDisable(false);
            puzzleMaterialField.setDisable(false);
            puzzleSizeField.setDisable(false);
        }
        else if(productType.getSelectionModel().getSelectedItem() == ProductType.OTHER){
            gameDurationFIeld.setDisable(true);
            playersQuantityField.setDisable(true);
            piecesQuantityField.setDisable(true);
            puzzleMaterialField.setDisable(true);
            puzzleSizeField.setDisable(true);
        }
    }
    //---------------------------------Product-----------------------------------

    private void loadProductListManager() {
        productListManager.getItems().clear();
        List<Product> products = genericHib.getAllRecords(Product.class);
        productListManager.getItems().addAll(products);
    }

    public void addNewProduct() {
        if(productType.getSelectionModel().getSelectedItem() == ProductType.BOARD_GAME){
            genericHib.create(new BoardGame(productTitleField.getText(),descriptionField.getText(), authorField.getText(), genericHib.getEntityById(Warehouse.class, warehouseComboBox.getSelectionModel().getSelectedItem().getId()), playersQuantityField.getText(), gameDurationFIeld.getText()));
        }
        else if( productType.getSelectionModel().getSelectedItem() == ProductType.PUZZLE){
            genericHib.create(new Puzzle(productTitleField.getText(), descriptionField.getText(), authorField.getText(), genericHib.getEntityById(Warehouse.class, warehouseComboBox.getSelectionModel().getSelectedItem().getId()), Integer.parseInt(piecesQuantityField.getText()) , puzzleMaterialField.getText(), puzzleSizeField.getText()));
        }
        else if( productType.getSelectionModel().getSelectedItem() == ProductType.OTHER){
            genericHib.create(new Product(productTitleField.getText(), descriptionField.getText(), authorField.getText(), genericHib.getEntityById(Warehouse.class, warehouseComboBox.getSelectionModel().getSelectedItem().getId())));
        }
        loadProductListManager();
    }

    public void updateProduct(ActionEvent actionEvent) {
        loadProductListManager();
    }

    public void deleteProduct(ActionEvent actionEvent) {
        loadProductListManager();
    }

    //---------------------------------Warehouse---------------------------------
    private void loadWarehouseList() {
        warehouseList.getItems().clear();
        warehouseList.getItems().addAll(genericHib.getAllRecords(Warehouse.class));
    }

    public void addNewWarehouse(ActionEvent actionEvent) {
        genericHib.create(new Warehouse(titleWarehouseField.getText(), addressWarehouseField.getText()));
        loadWarehouseList();
    }

    public void updateWarehouse(ActionEvent actionEvent) {
        Warehouse selectedWarehouse = warehouseList.getSelectionModel().getSelectedItem();
        Warehouse warehouse = genericHib.getEntityById(Warehouse.class, selectedWarehouse.getId());
        warehouse.setTitle(titleWarehouseField.getText());
        warehouse.setAddress(addressWarehouseField.getText());
        genericHib.update(warehouse);
        loadWarehouseList();
    }

    public void removeWarehouse(ActionEvent actionEvent) {
        Warehouse selectedWarehouse = warehouseList.getSelectionModel().getSelectedItem();
        Warehouse warehouse = genericHib.getEntityById(Warehouse.class, selectedWarehouse.getId());
        genericHib.delete(selectedWarehouse.getClass(), selectedWarehouse.getId());
    loadWarehouseList();
    }

    public void loadWarehouseData() {
        Warehouse selectedWarehouse = warehouseList.getSelectionModel().getSelectedItem();
        titleWarehouseField.setText(selectedWarehouse.getTitle());
        addressWarehouseField.setText(selectedWarehouse.getAddress());
    }

}