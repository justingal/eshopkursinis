package com.coursework.eshop.fxController.MainShop;


import com.coursework.eshop.HibernateControllers.CustomHib;
import com.coursework.eshop.fxController.JavaFxCustomsUtils;
import com.coursework.eshop.model.*;
import jakarta.persistence.EntityManagerFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainShopController {

    public ListView<Product> productList;
    @FXML
    public ListView<Cart> currentOrder;
    @FXML
    public Tab primaryTab;
    @FXML
    public Tab usersTab;
    @FXML
    public Tab warehousesTab;

    @FXML
    public TabPane tabPane;
    @FXML
    public Tab productsTab;

    @FXML
    public Tab commentsTab;

    @FXML
    private WarehouseTabController warehouseTabController;

    @FXML
    private CommentTabController commentTabController;

    @FXML
    private ProductTabController productTabController;

    @FXML
    private UserTabController userTabController;

    private EntityManagerFactory entityManagerFactory;
    private User currentUser;
    private CustomHib customHib;


    public void setData(EntityManagerFactory entityManagerFactory, User user) {
        this.entityManagerFactory = entityManagerFactory;
        this.currentUser = user;
        limitAccess();
        loadData();

    }

    private void loadData() {
        customHib = new CustomHib(entityManagerFactory);
        productList.getItems().addAll(customHib.getAllRecords(Product.class));

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
        if (productsTab.isSelected()) {

            productTabController.setData(customHib);
        }else if( warehousesTab.isSelected()){
            warehouseTabController.setData(customHib);
        }else if( usersTab.isSelected()){
            userTabController.setData(customHib);
        }else if( commentsTab.isSelected()){
            commentTabController.setData(customHib, currentUser);
        }
    }


}