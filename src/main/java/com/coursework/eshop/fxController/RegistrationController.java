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
import java.util.ArrayList;
import java.util.List;


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

    /**
     * Use this class to validate fields.
     * @field applicableUsersBitField - 1st bit set -> applicable to Customer, 2nd bit set -> applicable to Manager
     */
    private class FieldValidation {
        private final boolean isValid;
        private final String errorMessage;
        private int applicableUsersBitField;

        public FieldValidation(boolean valid, String errorMessage, int applicableUsersBitField) {
            this.isValid = valid;
            this.errorMessage = errorMessage;
            this.applicableUsersBitField = applicableUsersBitField;
        }

        public boolean isValid() {
            return this.isValid;
        }

        public String getErrorMessage() {
            return this.errorMessage;
        }

        public int getApplicableUsersBitField() {
            return this.applicableUsersBitField;
        }
    }

    private boolean validateFields(List<FieldValidation> validations, int userBitField) {
        boolean validFields = true;
        for(FieldValidation fv : validations) {
            if((fv.getApplicableUsersBitField() & userBitField) != 0 && !fv.isValid()) {
                JavaFxCustomsUtils.generateAlert(Alert.AlertType.INFORMATION, "Registration INFO", "Wrong data", fv.getErrorMessage());
                validFields = false;
            }
        }
        return validFields;
    }

    private void generateSuccessAlert() {
        JavaFxCustomsUtils.generateAlert(Alert.AlertType.INFORMATION, "Registration INFO", "Success", "User created");
        try {
            if (currentUser == null) {
                returnToLogin();
            }
            if (currentUser != null) {
                Stage stage = (Stage) surnameField.getScene().getWindow();
                stage.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generatePasswordHash(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    public void createUser() {
        ArrayList<FieldValidation> validations = new ArrayList<>();
        validations.add(new FieldValidation(!loginField.getText().isEmpty(), "Please check credentials, no login", 3));
        validations.add(new FieldValidation(!passwordField.getText().isEmpty(), "Please check credentials, no password", 3));
        validations.add(new FieldValidation(!repeatPasswordField.getText().isEmpty(), "Please check credentials, no repeated password", 3));
        validations.add(new FieldValidation(repeatPasswordField.getText().equals(passwordField.getText()), "Passwords do not match", 3));
        validations.add(new FieldValidation(!nameField.getText().isEmpty(), "Please check credentials, no name", 3));
        validations.add(new FieldValidation(!surnameField.getText().isEmpty(), "Please check credentials, no surname", 1));
        validations.add(new FieldValidation(!addressField.getText().isEmpty() && customerCheckbox.isSelected(), "Please check credentials, no address", 1));
        validations.add(new FieldValidation(!cardNoField.getText().isEmpty() && customerCheckbox.isSelected(), "Please check credentials, no card number", 1));
        validations.add(new FieldValidation(birthDateField.getValue() != null, "Please check credentials, no birth date", 3));

        validations.add(new FieldValidation(!employeeIdField.getText().isEmpty(), "Please check credentials, no employee ID", 2));
        validations.add(new FieldValidation(employmentDateField.getValue() != null, "Please check credentials, no employment date", 2));
        validations.add(new FieldValidation(!medCertificateField.getText().isEmpty(), "Please check credentials, no medical certificate", 2));

        if (customerCheckbox.isSelected() && validateFields(validations, 1)) {
            userHib.create(new Customer(loginField.getText(), generatePasswordHash(passwordField.getText()), birthDateField.getValue(), nameField.getText(), surnameField.getText(), addressField.getText(), cardNoField.getText()));
            generateSuccessAlert();
        } else if (managerCheckbox.isSelected() && validateFields(validations, 2)) {
            userHib.create(new Manager(loginField.getText(), generatePasswordHash(passwordField.getText()), birthDateField.getValue(), nameField.getText(), surnameField.getText(), employeeIdField.getText(), medCertificateField.getText(), employmentDateField.getValue()));
            generateSuccessAlert();
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