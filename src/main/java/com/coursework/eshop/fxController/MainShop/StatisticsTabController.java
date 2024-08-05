package com.coursework.eshop.fxController.MainShop;

import com.coursework.eshop.HibernateControllers.CustomHib;
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

            //List filteredData = CustomHib.filterData(minValue, maxValue, userId, managerId, startDate, endDate);

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

        managerColumn.setCellFactory(ComboBoxTableCell.forTableColumn(managersData));
        customerColumn.setCellFactory(ComboBoxTableCell.forTableColumn(customersData));
        orderStatusColumn.setCellFactory(ComboBoxTableCell.forTableColumn(OrderStatus.values()));


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

        List<CustomerOrder> allOrders = customHib.getAllRecords(CustomerOrder.class);

    }

}
