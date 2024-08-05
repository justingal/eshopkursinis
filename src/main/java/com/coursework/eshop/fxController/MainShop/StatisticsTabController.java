package com.coursework.eshop.fxController.MainShop;

import com.coursework.eshop.HibernateControllers.CustomHib;
import com.coursework.eshop.fxController.tableviews.OrderTableParameters;
import com.coursework.eshop.fxController.tableviews.StatisticsTableParameters;
import com.coursework.eshop.model.Customer;
import com.coursework.eshop.model.CustomerOrder;
import com.coursework.eshop.model.Manager;
import com.coursework.eshop.model.OrderStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class StatisticsTabController implements Initializable {
    @FXML
    public TableView<StatisticsTableParameters> statisticsDataTableView;
    @FXML
    public TableColumn<StatisticsTableParameters, Integer> orderIdColumn;
    @FXML
    public TableColumn<StatisticsTableParameters, Manager> managerColumn;
    @FXML
    public TableColumn<StatisticsTableParameters, Customer> customerColumn;
    @FXML
    public TableColumn<StatisticsTableParameters, LocalDate> orderDateColumn;
    @FXML
    public TableColumn<StatisticsTableParameters, Double> orderValueColumn;
    @FXML
    public TableColumn<StatisticsTableParameters, OrderStatus> orderStatusColumn;
    @FXML
    public TextField minValueField;
    @FXML
    public TextField maxValueField;
    @FXML
    public TextField buyerIdField;
    @FXML
    public DatePicker startDateField;
    @FXML
    public DatePicker endDateField;
    @FXML
    public PieChart salesPieChart;
    @FXML
    public ComboBox<OrderStatus> orderStatusComboBox;
    @FXML
    public ComboBox<Customer> customerComboBox;
    @FXML
    public ComboBox<Manager> managerComboBox;
    @FXML
    public ComboBox<String> statisticsComboBox;


    private ObservableList<Manager> managersData;
    private ObservableList<Customer> customersData;

    private CustomHib customHib = new CustomHib();

    public void filterData(ActionEvent actionEvent) {
        double minValue = 0.0;
        double maxValue = Double.MAX_VALUE;
        Customer customer = null;
        Manager manager = null;
        OrderStatus orderStatus = null;
        LocalDate startDate = null;
        LocalDate endDate = null;

        try {
            if (!minValueField.getText().isEmpty()) {
                minValue = Double.parseDouble(minValueField.getText());
            }
            if (!maxValueField.getText().isEmpty()) {
                maxValue = Double.parseDouble(maxValueField.getText());
            }
            startDate = startDateField.getValue();
            endDate = endDateField.getValue();
            orderStatus = orderStatusComboBox.getValue();
            customer = customerComboBox.getValue();
            manager = managerComboBox.getValue();

            List<CustomerOrder> filteredData = customHib.filterData(minValue, maxValue, customer, manager, orderStatus, startDate, endDate);
            ObservableList<StatisticsTableParameters> filteredStatisticsData = FXCollections.observableArrayList();

            for (CustomerOrder order : filteredData) {
                filteredStatisticsData.add(new StatisticsTableParameters(
                        order.getId(),
                        order.getResponsibleManager(),
                        order.getCustomer(),
                        order.getDateCreated(),
                        order.getTotalPrice(),
                        order.getOrderStatus()
                ));
            }
            statisticsDataTableView.setItems(filteredStatisticsData);

        } catch (NumberFormatException e) {

        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        managerColumn.setCellValueFactory(new PropertyValueFactory<>("manager"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customer"));
        orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        orderStatusColumn.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));
        orderValueColumn.setCellValueFactory(new PropertyValueFactory<>("orderValue"));

        loadCustomers();
        loadManagers();


        loadStatisticsData();
        statisticsComboBox.setItems(FXCollections.observableArrayList( "Price", "Manager", "OrderStatus"));

    }
    private void loadManagers() {
        List<Manager> allManagers = customHib.getAllRecords(Manager.class);
        managersData = FXCollections.observableArrayList(allManagers);
    }
    private void loadCustomers() {
        List<Customer> allCustomers = customHib.getAllRecords(Customer.class);
        customersData = FXCollections.observableArrayList(allCustomers);
    }

    private void loadStatisticsData() {
        ObservableList<OrderStatus> orderStatusList = FXCollections.observableArrayList();
        orderStatusList.add(null);
        orderStatusList.addAll(OrderStatus.values());
        orderStatusComboBox.setItems(orderStatusList);

        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        customerList.add(null);
        customerList.addAll(customersData);
        customerComboBox.setItems(customerList);

        ObservableList<Manager> managerList = FXCollections.observableArrayList();
        managerList.add(null);
        managerList.addAll(managersData);
        managerComboBox.setItems(managerList);


        List<CustomerOrder> allOrders = customHib.getAllRecords(CustomerOrder.class);
        ObservableList<StatisticsTableParameters> statisticsData = FXCollections.observableArrayList();

        for (CustomerOrder order : allOrders) {
            statisticsData.add(new StatisticsTableParameters(
                    order.getId(),
                    order.getResponsibleManager(),
                    order.getCustomer(),
                    order.getDateCreated(),
                    order.getTotalPrice(),
                    order.getOrderStatus()

            ));
        }
        statisticsDataTableView.setItems(statisticsData);


    }

    @FXML
    private void updatePieChart(ActionEvent event) {
        String selectedStatistic = statisticsComboBox.getValue();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        List<CustomerOrder> allOrders = customHib.getAllRecords(CustomerOrder.class);

        switch (selectedStatistic) {
            case "Price":
                pieChartData = allOrders.stream()
                        .collect(Collectors.groupingBy(CustomerOrder::getTotalPrice, Collectors.counting()))
                        .entrySet().stream()
                        .map(entry -> new PieChart.Data(entry.getKey().toString() + "€", entry.getValue()))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
                break;

            case "Manager":
                pieChartData = allOrders.stream()
                        .collect(Collectors.groupingBy(order -> (order.getResponsibleManager() == null) ? "None" : order.getResponsibleManager().getName(), Collectors.counting()))
                        .entrySet().stream()
                        .map(entry -> new PieChart.Data(entry.getKey(), entry.getValue()))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
                break;

            case "OrderStatus":
                pieChartData = allOrders.stream()
                        .collect(Collectors.groupingBy(CustomerOrder::getOrderStatus, Collectors.counting()))
                        .entrySet().stream()
                        .map(entry -> new PieChart.Data(entry.getKey().getLabel(), entry.getValue())) // Use name() method for enum
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
                break;
        }

        salesPieChart.setData(pieChartData);
    }


}
