package com.coursework.eshop.fxController.MainShop;

import com.coursework.eshop.HibernateControllers.CustomHib;
import com.coursework.eshop.StartGui;
import com.coursework.eshop.fxController.JavaFxCustomsUtils;
import com.coursework.eshop.fxController.tableviews.MyOrderTableParameters;
import com.coursework.eshop.model.CustomerOrder;
import com.coursework.eshop.model.OrderStatus;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
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
    public TableColumn< MyOrderTableParameters, Void> commentColumn;

    @FXML
    private ListView myItemsListView;

    private ObservableList<MyOrderTableParameters> ordersData;

    @FXML
    public TableColumn dummyManagerCol;

    public void setData(CustomHib customHib) {
        this.customHib = customHib;
        loadOrderData();
    }

    private CustomHib customHib = new CustomHib();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize the TableView columns
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateCreatedColumn.setCellValueFactory(new PropertyValueFactory<>("dateCreated"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        orderStatusColumn.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));

        orderStatusColumn.setCellFactory(ComboBoxTableCell.forTableColumn(OrderStatus.values()));


        commentColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<MyOrderTableParameters, Void> call(final TableColumn<MyOrderTableParameters, Void> param) {
                final TableCell<MyOrderTableParameters, Void> cell = new TableCell<>() {
                    private final Button btn = new Button("Chat");

                    {
                        btn.setOnAction(event -> {
                            openOrderChatWindow();
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        });


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

    private void openOrderChatWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("commentTab.fxml"));
            Parent parent = fxmlLoader.load();
            CommentTabController commentTab = fxmlLoader.getController();
            commentTab.setData(customHib); //! Overloading set data funkcija gaidy
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.setTitle("Order chat window"); //
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Failed to open chat window", "An error occurred while opening the chat window: " + e.getMessage());
        }
    }
}

