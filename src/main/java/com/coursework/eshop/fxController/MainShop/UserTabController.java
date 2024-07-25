package com.coursework.eshop.fxController.MainShop;

import com.coursework.eshop.HibernateControllers.CustomHib;
import com.coursework.eshop.fxController.tableviews.CustomerTableParameters;
import com.coursework.eshop.fxController.tableviews.ManagerTableParameters;
import com.coursework.eshop.model.Customer;
import com.coursework.eshop.model.Manager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import lombok.Getter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UserTabController implements Initializable {

    @FXML
    public TableView customerTable;
    @FXML
    public TableView managerTable;

    @FXML
    public TableColumn<CustomerTableParameters, Integer> idTableCol;
    @FXML
    public TableColumn<CustomerTableParameters, String> loginTableCol;
    @FXML
    public TableColumn<CustomerTableParameters, String> passwordTableCol;
    @FXML
    public TableColumn<CustomerTableParameters, String> addressTableCol;
    @FXML
    public TableColumn< CustomerTableParameters, Void> dummyCol;
    @FXML
    public TableColumn<ManagerTableParameters, Integer> idManagerTableCol;
    @FXML
    public TableColumn<ManagerTableParameters,String> loginManagerTableCol;
    @FXML
    public TableColumn <ManagerTableParameters,String> passwordManagerTableCol;
    @FXML
    public TableColumn <ManagerTableParameters,String> employeeIdManagerTableCol;
    @FXML
    public TableColumn dummyManagerCol;
    @Getter
    @FXML
    public AnchorPane userTabAnchor;

    private ObservableList<ManagerTableParameters> dataManager = FXCollections.observableArrayList();
    private ObservableList<CustomerTableParameters> data = FXCollections.observableArrayList();
    private CustomHib customHib;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        customerTable.setEditable(true);
        idTableCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        loginTableCol.setCellValueFactory(new PropertyValueFactory<>("login"));
        loginTableCol.setCellFactory(TextFieldTableCell.forTableColumn());
        loginTableCol.setOnEditCommit( event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setLogin(event.getNewValue());
            Customer customer = customHib.getEntityById(Customer.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            customer.setLogin(event.getNewValue());
            customHib.update(customer);
        });
        passwordTableCol.setCellValueFactory(new PropertyValueFactory<>("password"));
        passwordTableCol.setCellFactory(TextFieldTableCell.forTableColumn());
        passwordTableCol.setOnEditCommit( event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setPassword(event.getNewValue());
            Customer customer = customHib.getEntityById(Customer.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            customer.setPassword(event.getNewValue());
            customHib.update(customer);
        });
        addressTableCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        addressTableCol.setCellFactory(TextFieldTableCell.forTableColumn());
        addressTableCol.setOnEditCommit( event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setAddress(event.getNewValue());
            Customer customer = customHib.getEntityById(Customer.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            customer.setAddress(event.getNewValue());
            customHib.update(customer);
        });

        Callback<TableColumn<CustomerTableParameters, Void>, TableCell<CustomerTableParameters, Void>> customerDeleteCallback = param -> {
            final TableCell<CustomerTableParameters, Void> cell = new TableCell<>() {
                private final Button deleteButton = new Button("Delete");

                {
                    deleteButton.setOnAction(event -> {
                        CustomerTableParameters row = getTableView().getItems().get(getIndex());
                        customHib.delete(Customer.class, row.getId());
                        customerTable.getItems().clear();
                        loadUserTables();
                    });
                }
                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(deleteButton);
                    }
                }
            };
            return cell;
        };

        dummyCol.setCellFactory(customerDeleteCallback);


        managerTable.setEditable(true);
        idManagerTableCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        loginManagerTableCol.setCellValueFactory(new PropertyValueFactory<>("login"));
        loginManagerTableCol.setCellFactory(TextFieldTableCell.forTableColumn());
        loginManagerTableCol.setOnEditCommit( event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setLogin(event.getNewValue());
            Manager manager = customHib.getEntityById(Manager.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            manager.setLogin(event.getNewValue());
            customHib.update(manager);
        });
        passwordManagerTableCol.setCellValueFactory(new PropertyValueFactory<>("password"));
        passwordManagerTableCol.setCellFactory(TextFieldTableCell.forTableColumn());
        passwordManagerTableCol.setOnEditCommit( event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setPassword(event.getNewValue());
            Manager manager = customHib.getEntityById(Manager.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            manager.setPassword(event.getNewValue());
            customHib.update(manager);
        });
        employeeIdManagerTableCol.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        employeeIdManagerTableCol.setCellFactory(TextFieldTableCell.forTableColumn());
        employeeIdManagerTableCol.setOnEditCommit( event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setEmployeeId(event.getNewValue());
            Manager manager = customHib.getEntityById(Manager.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            manager.setEmployeeId(event.getNewValue());
            customHib.update(manager);
        });

        Callback<TableColumn<ManagerTableParameters, Void>, TableCell<ManagerTableParameters, Void>> managerDeleteCallback = param -> {
            final TableCell<ManagerTableParameters, Void> cell = new TableCell<>() {
                private final Button deleteButton = new Button("Delete");

                {
                    deleteButton.setOnAction(event -> {
                        ManagerTableParameters row = getTableView().getItems().get(getIndex());
                        customHib.delete(Manager.class, row.getId());
                        managerTable.getItems().clear();
                        loadUserTables();
                    });
                }
                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(deleteButton);
                    }
                }
            };
            return cell;
        };

        dummyManagerCol.setCellFactory(managerDeleteCallback);
    }

    public void setData(CustomHib customHib) {
        this.customHib = customHib;
        loadUserTables();
    }

    private void loadUserTables() {
        customerTable.getItems().clear();
        List<Customer> customerList = customHib.getAllRecords(Customer.class);
        for (Customer c : customerList) {
            CustomerTableParameters customerTableParameters = new CustomerTableParameters();
            customerTableParameters.setId(c.getId());
            customerTableParameters.setLogin(c.getLogin());
            customerTableParameters.setPassword(c.getPassword());
            customerTableParameters.setAddress(c.getAddress());
            data.add(customerTableParameters);
        }
        customerTable.setItems(data);

        managerTable.getItems().clear();
        List <Manager> managerList = customHib.getAllRecords(Manager.class);
        for (Manager m : managerList) {
            ManagerTableParameters managerTableParameters = new ManagerTableParameters();
            managerTableParameters.setId(m.getId());
            managerTableParameters.setLogin(m.getLogin());
            managerTableParameters.setPassword(m.getPassword());
            managerTableParameters.setEmployeeId(m.getEmployeeId());
            dataManager.add(managerTableParameters);
        }
        managerTable.setItems(dataManager);
    }
}
