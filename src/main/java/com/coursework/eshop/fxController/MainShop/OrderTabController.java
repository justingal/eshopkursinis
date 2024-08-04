package com.coursework.eshop.fxController.MainShop;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class OrderTabController {

    @FXML
    public TableColumn commentColumn;
    @FXML
    public TableColumn orderStatusColumn;
    @FXML
    public TableColumn customerNameColumn;
    @FXML
    public TableColumn managerNameColumn;
    @FXML
    public TableColumn dateCreatedColumn;
    @FXML
    public TableColumn orderIdColumn;
    @FXML
    public TableView myOrdersTableView;
    @FXML
    public ListView myItemsListView;
}