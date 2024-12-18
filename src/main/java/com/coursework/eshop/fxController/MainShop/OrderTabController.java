package com.coursework.eshop.fxController.MainShop;

import com.coursework.eshop.HibernateControllers.CustomHib;
import com.coursework.eshop.StartGui;
import com.coursework.eshop.fxController.JavaFxCustomsUtils;
import com.coursework.eshop.fxController.tableviews.OrderTableParameters;
import com.coursework.eshop.model.*;
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
import org.hibernate.criterion.Order;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;


public class OrderTabController implements Initializable {

    @FXML
    public TableColumn<OrderTableParameters, Void> commentColumn;
    @FXML
    public TableColumn<OrderTableParameters, Void> deleteColumn;
    @FXML
    public TableColumn<OrderTableParameters, OrderStatus> orderStatusColumn;
    @FXML
    public TableColumn<OrderTableParameters, String> customerNameColumn;
    @FXML
    public TableColumn<OrderTableParameters, Manager> managerNameColumn;
    @FXML
    public TableColumn<OrderTableParameters, LocalDate> dateCreatedColumn;
    @FXML
    public TableColumn<OrderTableParameters, Integer> orderIdColumn;
    @FXML
    public TableView<OrderTableParameters> ordersTableView;
    @FXML
    public ListView<String> myItemsListView;


    private ObservableList<Manager> managersData;

    private CustomHib customHib = new CustomHib();

    public void setData(CustomHib customHib) {
        this.customHib = customHib;

        loadOrderData();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ordersTableView.setEditable(true);
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateCreatedColumn.setCellValueFactory(new PropertyValueFactory<>("dateCreated"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        orderStatusColumn.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));
        managerNameColumn.setCellValueFactory(new PropertyValueFactory<>("manager"));

        loadManagers();
        managerNameColumn.setCellFactory(ComboBoxTableCell.forTableColumn(managersData));
        orderStatusColumn.setCellFactory(ComboBoxTableCell.forTableColumn(OrderStatus.values()));


        managerNameColumn.setOnEditCommit(event -> {
            OrderTableParameters orderParam = event.getRowValue();
            Manager newManager = event.getNewValue();

            orderParam.setManager(newManager);

            CustomerOrder customerOrder = customHib.getEntityById(CustomerOrder.class, orderParam.getId());
            customerOrder.setResponsibleManager(newManager);
            customerOrder.setOrderStatus(OrderStatus.PROCESSING);
            customHib.update(customerOrder);
            loadOrderData();
        });

        orderStatusColumn.setOnEditCommit(event -> {
            OrderTableParameters orderParam = event.getRowValue();
            OrderStatus newOrderStatus = event.getNewValue();

            orderParam.setOrderStatus(newOrderStatus);

            CustomerOrder customerOrder = customHib.getEntityById(CustomerOrder.class, orderParam.getId());
            customerOrder.setOrderStatus(newOrderStatus);
            customHib.update(customerOrder);
            loadOrderData();
        });

        commentColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<OrderTableParameters, Void> call(final TableColumn<OrderTableParameters, Void> param) {
                final TableCell<OrderTableParameters, Void> cell = new TableCell<>() {
                    private final Button btn = new Button("Chat");

                    {
                        btn.setOnAction(event -> {
                            OrderTableParameters row = getTableView().getItems().get(getIndex());
                            openOrderChatWindow(row.getId());
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

        deleteColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<OrderTableParameters, Void> call(final TableColumn<OrderTableParameters, Void> param) {
                final TableCell<OrderTableParameters, Void> cell = new TableCell<>() {
                    private final Button btn = new Button("Delete");

                    {
                        btn.setOnAction(event -> {
                            OrderTableParameters row = getTableView().getItems().get(getIndex());
                            customHib.deleteOrder(row.getId());
                            loadOrderData();
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
        loadOrderData();
    }

    private void loadManagers() {
        List<Manager> allManagers = customHib.getAllRecords(Manager.class);
        managersData = FXCollections.observableArrayList(allManagers);
    }

    private boolean isOrderRelevantForUser(CustomerOrder order, User user) {
        return order.getResponsibleManager() == null || order.getResponsibleManager().getId() == user.getId() || user instanceof Admin;
    }

    private boolean isOrderUrgent(CustomerOrder order) {
        long hours = ChronoUnit.HOURS.between(order.getDateCreated().atStartOfDay(), LocalDate.now().atStartOfDay());
        return hours >= 24 && order.getOrderStatus() != OrderStatus.URGENT && order.getResponsibleManager() == null;
    }

    private ObservableList<OrderTableParameters> getOrderData(List<CustomerOrder> orders) {
        ObservableList<OrderTableParameters> ordersData = FXCollections.observableArrayList();
        for (CustomerOrder order : orders) {
            if(!isOrderRelevantForUser(order, StartGui.currentUser)) {
                continue;
            }

            if(isOrderUrgent(order)) {
                order.setOrderStatus(OrderStatus.URGENT);
                customHib.update(order);
            }

            ordersData.add(new OrderTableParameters(
                    order.getId(),
                    order.getDateCreated(),
                    order.getCustomer().getName(),
                    order.getOrderStatus(),
                    order.getResponsibleManager()
            ));
        }

        return ordersData;
    }

    private TableRow<OrderTableParameters> createTableRow() {
        return new TableRow<>() {
            @Override
            protected void updateItem(OrderTableParameters item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle("");
                } else {
                    long hours = ChronoUnit.HOURS.between(item.getDateCreated().atStartOfDay(), LocalDate.now().atStartOfDay());
                    if (hours >= 24 && item.getManager() == null) {
                        setStyle("-fx-background-color: yellow;");
                    } else {
                        setStyle("");
                    }
                }
            }
        };
    }

    public ObservableList<OrderTableParameters> sortOrderDataByUrgency(ObservableList<OrderTableParameters> ordersData) {
        ordersData.sort(Comparator.comparing((OrderTableParameters o) -> o.getOrderStatus() == OrderStatus.URGENT)
                .reversed()
                .thenComparing(OrderTableParameters::getDateCreated));
        return ordersData;
    }

    public void loadOrderData() {
        List<CustomerOrder> allOrders = customHib.getAllRecords(CustomerOrder.class);
        ObservableList<OrderTableParameters> ordersData = getOrderData(allOrders);

        ordersTableView.setItems(sortOrderDataByUrgency(ordersData));
        ordersTableView.setRowFactory(tv -> createTableRow());
        ordersTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadItems(newValue);
            }
        });
    }

    private void loadItems(OrderTableParameters order) {
        ObservableList<String> items = FXCollections.observableArrayList();
        CustomerOrder customerOrder = customHib.getEntityById(CustomerOrder.class, order.getId());
        customerOrder.getAllProducts().forEach(product -> items.add("ID:" + product.getId() + "  " + product.getTitle() + "  " + product.getAuthor() + "  " + product.getWarehouse() + "   " + product.getPrice() + "€"));
        myItemsListView.setItems(items);
    }

    private void openOrderChatWindow(int orderId) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("commentTab.fxml"));
            Parent parent = fxmlLoader.load();
            CommentTabController commentTab = fxmlLoader.getController();
            commentTab.setData(customHib, orderId);
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.setTitle("Order chat window");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Failed to open chat window", "An error occurred while opening the chat window: " + e.getMessage());
        }
    }
}