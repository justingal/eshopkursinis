package com.coursework.eshop.fxController;

//import com.coursework.eshop.HibernateControllers.UserHib;
import com.coursework.eshop.HibernateControllers.UserHib;
import com.coursework.eshop.StartGui;
import com.coursework.eshop.model.Customer;
import com.coursework.eshop.model.ProductType;
import jakarta.persistence.EntityManagerFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

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
    @FXML
    public CheckBox isAdminCheck;

    private EntityManagerFactory entityManagerFactory;

    private UserHib userHib;
    public void setData(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
        userHib = new UserHib(entityManagerFactory);
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

            userHib.createUser(new Customer(loginField.getText(), passwordField.getText(), birthDateField.getValue(), nameField.getText(), surnameField.getText(), addressField.getText(), cardNoField.getText()));
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.INFORMATION, "Registration INFO", "Success", "User created");
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
    public void initialize() {
        managerCheckbox.setVisible(false);
        employeeIdField.setVisible(false);
        medCertificateField.setVisible(false);
        employmentDateField.setVisible(false);
        isAdminCheck.setVisible(false);

    }
}