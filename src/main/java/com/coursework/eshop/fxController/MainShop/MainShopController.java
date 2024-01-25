package com.coursework.eshop.fxController.MainShop;


import com.coursework.eshop.HibernateControllers.CustomHib;
import com.coursework.eshop.fxController.JavaFxCustomsUtils;
import com.coursework.eshop.fxController.tableviews.CustomerTableParameters;
import com.coursework.eshop.fxController.tableviews.ManagerTableParameters;
import com.coursework.eshop.model.*;
import jakarta.persistence.EntityManagerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainShopController implements Initializable {

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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("com/coursework/eshop/warehouseTab.fxml"));
        FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("com/coursework/eshop/commentTab.fxml"));
        FXMLLoader fxmlLoader3 = new FXMLLoader(getClass().getResource("com/coursework/eshop/productTab.fxml"));
        FXMLLoader fxmlLoader4 = new FXMLLoader(getClass().getResource("com/coursework/eshop/userTab.fxml"));
        Tab warehousesTab = null;
        Tab commentsTab = null;
        Tab productsTab = null;
        Tab usersTab = null;
        try {
            warehousesTab = fxmlLoader.load();
            commentsTab = fxmlLoader2.load();
            productsTab = fxmlLoader3.load();
            usersTab = fxmlLoader4.load();
            warehouseTabController = fxmlLoader.getController();
            commentTabController = fxmlLoader2.getController();
            productTabController = fxmlLoader3.getController();
            userTabController = fxmlLoader4.getController();
            warehouseTabController = fxmlLoader.getController();
            commentTabController = fxmlLoader2.getController();
            productTabController = fxmlLoader3.getController();
            userTabController = fxmlLoader4.getController();
            warehouseTabController.setData(customHib);
            commentTabController.setData(customHib, currentUser);
            productTabController.setData(customHib);
            userTabController.setData(customHib);
            warehousesTab.setContent(warehouseTabController.getWarehouseTabAnchor());
            commentsTab.setContent(commentTabController.getCommentTabAnchor());
            productsTab.setContent(productTabController.getProductTabAnchor());
            usersTab.setContent(userTabController.getUserTabAnchor());

        } catch (IOException e) {
            JavaFxCustomsUtils.generateAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Error",
                    "Error"
            );
        }

    }

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