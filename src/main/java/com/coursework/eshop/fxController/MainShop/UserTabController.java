package com.coursework.eshop.fxController;

import com.coursework.eshop.HibernateControllers.GenericHib;
import com.coursework.eshop.fxController.tableviews.CustomerTableParameters;
import com.coursework.eshop.fxController.tableviews.ManagerTableParameters;
import com.coursework.eshop.model.Customer;
import com.coursework.eshop.model.Manager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class UserTabController {

    FXML
    private TableView<CustomerTableParameters> userTable;
    @FXML
    private TableColumn<CustomerTableParameters, Integer> idColumn;
    @FXML
    private TableColumn<CustomerTableParameters, String> loginColumn;
    @FXML
    private TableColumn<CustomerTableParameters, String> passwordColumn;
    @FXML
    private TableColumn<CustomerTableParameters, String> addressColumn;

    // Čia yra priklausomybė, kuri gali būti įtraukta per konstruktorių arba setter metodą
    private GenericHib genericHib;

    private ObservableList<CustomerTableParameters> userData = FXCollections.observableArrayList();

    // Jei reikia, galite pridėti konstruktorių, kad perduotumėte GenericHib priklausomybę
    public UserTabController(GenericHib genericHib) {
        this.genericHib = genericHib;
    }

    @FXML
    private void initialize() {
        // Čia konfigūruojame stulpelius atitinkamai su duomenų bazės laukais
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        loginColumn.setCellValueFactory(new PropertyValueFactory<>("login"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        // Užpildome vartotojų duomenis į TableView
        loadUserTable();
    }

    private void loadUserTable() {
        userData.clear();
        customerTable.getItems().clear();
        List<Customer> customerList = genericHib.getAllRecords(Customer.class);
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
        List <Manager> managerList = genericHib.getAllRecords(Manager.class);
        for (Manager m : managerList) {
            ManagerTableParameters managerTableParameters = new ManagerTableParameters();
            managerTableParameters.setId(m.getId());
            managerTableParameters.setLogin(m.getLogin());
            managerTableParameters.setPassword(m.getPassword());
            managerTableParameters.setEmployeeId(m.getEmployeeId());
            dataManager.add(managerTableParameters);
        }
        managerTable.setItems(dataManager);
        userTable.setItems(userData);
    }

    // Čia galite pridėti papildomus metodai, pavyzdžiui, trinti vartotoją, redaguoti vartotojo informaciją ir pan.
}