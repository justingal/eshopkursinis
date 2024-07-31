package com.coursework.eshop.fxController;

//import com.coursework.eshop.HibernateControllers.UserHib;
import com.coursework.eshop.HibernateControllers.CustomHib;
import com.coursework.eshop.HibernateControllers.EntityManagerFactorySingleton;
import com.coursework.eshop.StartGui;
import com.coursework.eshop.model.Admin;
import com.coursework.eshop.model.Customer;
import com.coursework.eshop.model.Manager;
import com.coursework.eshop.model.User;
import jakarta.persistence.EntityManagerFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import at.favre.lib.crypto.bcrypt.BCrypt;

import java.io.IOException;
//import utils.DatabaseUtils;
//
//import java.sql.Connection;
//import java.sql.Date;
//import java.sql.PreparedStatement;

public class RegistrationController {

    @FXML
    public TextField loginField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public PasswordField repeatPasswordField;
    @FXML
    public TextField nameField;
    @FXML
    public TextField surnameField;
    @FXML
    public RadioButton customerCheckbox;
    @FXML
    public ToggleGroup userType;
    @FXML
    public RadioButton managerCheckbox;
    @FXML
    public TextField addressField;
    @FXML
    public TextField cardNoField;
    @FXML
    public DatePicker birthDateField;
    @FXML
    public TextField employeeIdField;
    @FXML
    public TextField medCertificateField;
    @FXML
    public DatePicker employmentDateField;

    private EntityManagerFactory entityManagerFactory;

    private User currentUser;

    private CustomHib userHib;
    public void setData(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
        userHib = new CustomHib(entityManagerFactory);
    }

    public void createUser() {
        if (loginField.getText().isEmpty()){
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.INFORMATION, "Registration INFO", "Wrong data", "Please check credentials, no login");
        }
        if (passwordField.getText().isEmpty()){
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.INFORMATION, "Registration INFO", "Wrong data", "Please check credentials, no password");
        }
        if (repeatPasswordField.getText().isEmpty()){
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.INFORMATION, "Registration INFO", "Wrong data", "Please check credentials, no repeated password");
        }
        if (!repeatPasswordField.getText().equals(passwordField.getText())){
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.INFORMATION, "Registration INFO", "Wrong data", "Passwords do not match");
        }
        if (nameField.getText().isEmpty()){
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.INFORMATION, "Registration INFO", "Wrong data", "Please check credentials, no name");
        }
        if (surnameField.getText().isEmpty()){
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.INFORMATION, "Registration INFO", "Wrong data", "Please check credentials, no surname");
        }
        if (addressField.getText().isEmpty()){
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.INFORMATION, "Registration INFO", "Wrong data", "Please check credentials, no address");
        }
        if (cardNoField.getText().isEmpty()){
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.INFORMATION, "Registration INFO", "Wrong data", "Please check credentials, no card number");
        }
        if (birthDateField.getValue() == null){
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.INFORMATION, "Registration INFO", "Wrong data", "Please check credentials, no birth date");
        }
        if (!loginField.getText().isEmpty() &&
                !passwordField.getText().isEmpty() &&
                repeatPasswordField.getText().equals(passwordField.getText()) &&
                !repeatPasswordField.getText().isEmpty() &&
                !nameField.getText().isEmpty() &&
                !surnameField.getText().isEmpty() &&
                !addressField.getText().isEmpty() &&
                !cardNoField.getText().isEmpty() &&
                birthDateField.getValue() != null) {

            String bcryptHashString = BCrypt.withDefaults().hashToString(12, passwordField.getText().toCharArray());
            if (customerCheckbox.isSelected()) {
                userHib.create(new Customer(loginField.getText(), bcryptHashString, birthDateField.getValue(), nameField.getText(), surnameField.getText(), addressField.getText(), cardNoField.getText()));
            } else if (managerCheckbox.isSelected()) {
                userHib.create( new Manager(loginField.getText(), bcryptHashString, birthDateField.getValue(), nameField.getText(), surnameField.getText(),employeeIdField.getText(), medCertificateField.getText(), employmentDateField.getValue()));
                // Papildomi manager specifiniai laukai gali būti pridėti čia
            }         JavaFxCustomsUtils.generateAlert(Alert.AlertType.INFORMATION, "Registration INFO", "Success", "User created");
            try {
                returnToLogin();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void returnToLogin() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("login.fxml"));
        Parent parent = fxmlLoader.load();
        LoginController loginController = fxmlLoader.getController();
        loginController.setData(entityManagerFactory);
        Scene scene = new Scene(parent);
        Stage stage = (Stage) loginField.getScene().getWindow();
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

}