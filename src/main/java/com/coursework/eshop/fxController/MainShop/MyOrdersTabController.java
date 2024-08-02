package com.coursework.eshop.fxController.MainShop;

import com.coursework.eshop.HibernateControllers.CustomHib;
import com.coursework.eshop.fxController.tableviews.MyOrderTableParameters;
import com.coursework.eshop.model.CustomerOrder;
import com.coursework.eshop.model.OrderStatus;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MyOrdersTabController implements Initializable {
    @FXML
    private TableView<MyOrderTableParameters> myOrdersTableView;
    @FXML
    private TableColumn<MyOrderTableParameters, Integer> orderIdColumn;
    @FXML
    private TableColumn dateCreatedColumn;
    @FXML
    private TableColumn customerNameColumn;
    @FXML
    private TableColumn orderStatusColumn;
    @FXML
    private ListView myItemsListView;

    private ObservableList<MyOrderTableParameters> ordersData;

    private CustomHib customHib = new CustomHib();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize the TableView columns
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateCreatedColumn.setCellValueFactory(new PropertyValueFactory<>("dateCreated"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        orderStatusColumn.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));

        orderStatusColumn.setCellFactory(ComboBoxTableCell.forTableColumn(OrderStatus.values()));
/*        orderStatusColumn.setOnEditCommit(event -> {
            MyOrderTableParameters order = event.getRowValue();
            order.setOrderStatus(event.getNewValue());

            // Update the order status in the database
            CustomerOrder customerOrder = customHib.getEntityById(CustomerOrder.class, order.getId());
            customerOrder.setOrderStatus(event.getNewValue());
            customHib.update(customerOrder);
        });*/

        // Enable editing in the TableView
        myOrdersTableView.setEditable(true);


        loadOrderData();
    }

    private void loadOrderData() {
        // Fetch data from database
        List<CustomerOrder> allOrders = customHib.getAllRecords(CustomerOrder.class);
        ordersData = FXCollections.observableArrayList();

        for (CustomerOrder order : allOrders) {
            ordersData.add(new MyOrderTableParameters(
                    order.getId(),
                    order.getDateCreated(),
                    order.getCustomer().getName(),
                    order.getOrderStatus()
            ));
        }

        myOrdersTableView.setItems(ordersData);

        // Handle row selection to display items in ListView
        myOrdersTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadItems(newValue);
            }
        });
    }
    private void loadItems(MyOrderTableParameters order) {
        ObservableList<String> items = FXCollections.observableArrayList();
        CustomerOrder customerOrder = customHib.getEntityById(CustomerOrder.class, order.getId());
        customerOrder.getAllProducts().forEach(product -> items.add(product.getTitle()));
        myItemsListView.setItems(items);
    }
}

