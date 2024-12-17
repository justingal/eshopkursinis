package com.coursework.eshop.fxController;


import at.favre.lib.crypto.bcrypt.BCrypt;
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

import java.io.IOException;


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
    public Button returnButton;
    @FXML
    public Button createUserButton;

    private EntityManagerFactory entityManagerFactory = EntityManagerFactorySingleton.getEntityManagerFactory();

    private User currentUser = StartGui.currentUser;

    private CustomHib userHib = new CustomHib();

    @FXML
    public void initialize() {
        updateUIBasedOnUserRole();
        updateVisibility();
        userType.selectedToggleProperty().addListener((observable, oldVal, newVal) -> {
            updateVisibility();
        });
    }

    public void setData(CustomHib customHib) {
        this.userHib = customHib;
    }

    private void updateUIBasedOnUserRole() {
        if (currentUser instanceof Admin) {
            managerCheckbox.setVisible(true);
        } else {
            managerCheckbox.setVisible(false);
        }
        if (currentUser != null) {
            returnButton.setVisible(false);
        } else {
            returnButton.setVisible(true);
        }

    }

    @FXML
    private void updateVisibility() {
        boolean isManager = managerCheckbox.isSelected();
        employeeIdField.setVisible(isManager);
        medCertificateField.setVisible(isManager);
        employmentDateField.setVisible(isManager);

        boolean isCustomer = customerCheckbox.isSelected();
        addressField.setVisible(isCustomer);
        cardNoField.setVisible(isCustomer);
    }

    public void createUser() {
        // Validate fields
        if (loginField.getText().isEmpty()) {
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.INFORMATION, "Registration INFO", "Wrong data", "Please check credentials, no login");
        }
        if (passwordField.getText().isEmpty()) {
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.INFORMATION, "Registration INFO", "Wrong data", "Please check credentials, no password");
        }
        if (repeatPasswordField.getText().isEmpty()) {
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.INFORMATION, "Registration INFO", "Wrong data", "Please check credentials, no repeated password");
        }
        if (!repeatPasswordField.getText().equals(passwordField.getText())) {
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.INFORMATION, "Registration INFO", "Wrong data", "Passwords do not match");
        }
        if (nameField.getText().isEmpty()) {
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.INFORMATION, "Registration INFO", "Wrong data", "Please check credentials, no name");
        }
        if (surnameField.getText().isEmpty()) {
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.INFORMATION, "Registration INFO", "Wrong data", "Please check credentials, no surname");
        }
        if (addressField.getText().isEmpty() && customerCheckbox.isSelected()) {
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.INFORMATION, "Registration INFO", "Wrong data", "Please check credentials, no address");
        }
        if (cardNoField.getText().isEmpty() && customerCheckbox.isSelected()) {
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.INFORMATION, "Registration INFO", "Wrong data", "Please check credentials, no card number");
        }
        if (birthDateField.getValue() == null) {
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.INFORMATION, "Registration INFO", "Wrong data", "Please check credentials, no birth date");
        }

        boolean validCustomerData = customerCheckbox.isSelected() &&
                !loginField.getText().isEmpty() &&
                !passwordField.getText().isEmpty() &&
                repeatPasswordField.getText().equals(passwordField.getText()) &&
                !repeatPasswordField.getText().isEmpty() &&
                !nameField.getText().isEmpty() &&
                !surnameField.getText().isEmpty() &&
                !addressField.getText().isEmpty() &&
                !cardNoField.getText().isEmpty() &&
                birthDateField.getValue() != null;

        boolean validManagerData = managerCheckbox.isSelected() &&
                !loginField.getText().isEmpty() &&
                !passwordField.getText().isEmpty() &&
                repeatPasswordField.getText().equals(passwordField.getText()) &&
                !repeatPasswordField.getText().isEmpty() &&
                !nameField.getText().isEmpty() &&
                !employeeIdField.getText().isEmpty() &&
                employmentDateField.getValue() != null &&
                !medCertificateField.getText().isEmpty() &&
                birthDateField.getValue() != null;

        if (validCustomerData) {
            String bcryptHashString = BCrypt.withDefaults().hashToString(12, passwordField.getText().toCharArray());
            userHib.create(new Customer(loginField.getText(), bcryptHashString, birthDateField.getValue(),
                    nameField.getText(), surnameField.getText(), addressField.getText(), cardNoField.getText()));

            JavaFxCustomsUtils.generateAlert(Alert.AlertType.INFORMATION, "Registration INFO", "Success", "User created");
            try {
                if (currentUser == null) {
                    returnToLogin();
                } else {
                    // Use the UI hierarchy to get the stage
                    Stage currentStage = (Stage) nameField.getScene().getWindow();
                    currentStage.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (validManagerData) {
            String bcryptHashString = BCrypt.withDefaults().hashToString(12, passwordField.getText().toCharArray());
            userHib.create(new Manager(loginField.getText(), bcryptHashString, birthDateField.getValue(),
                    nameField.getText(), surnameField.getText(), employeeIdField.getText(),
                    medCertificateField.getText(), employmentDateField.getValue()));

            JavaFxCustomsUtils.generateAlert(Alert.AlertType.INFORMATION, "Registration INFO", "Success", "User created");
            try {
                if (currentUser == null) {
                    returnToLogin();
                } else {
                    Stage currentStage = (Stage) nameField.getScene().getWindow();
                    currentStage.close();
                }
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