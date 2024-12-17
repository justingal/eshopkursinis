package com.coursework.eshop.fxController.MainShop;

import com.coursework.eshop.HibernateControllers.CustomHib;
import com.coursework.eshop.StartGui;
import com.coursework.eshop.fxController.JavaFxCustomsUtils;
import com.coursework.eshop.fxController.LoginController;
import com.coursework.eshop.fxController.RegistrationController;
import com.coursework.eshop.model.Admin;
import com.coursework.eshop.model.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import mockit.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testfx.util.WaitForAsyncUtils;

import java.io.IOException;
import java.time.LocalDate;

class RegistrationControllerTest {

    @Mocked
    private CustomHib customHib;

    @Mocked
    private EntityManager entityManager;

    @Mocked
    private static EntityManagerFactory entityManagerFactory;

    @Mocked
    private FXMLLoader fxmlLoader;

    @Mocked
    private LoginController loginController;

    @Injectable
    private EntityTransaction entityTransaction;


    private Scene scene;

    private Stage stage;

    private Parent parent;

    private RegistrationController controller;

    @BeforeAll
    static void initToolkit() {
        StartGui.currentUser = new Admin();
        if (!Platform.isFxApplicationThread()) {
            Platform.startup(() -> {});
        }
    }

    @BeforeEach
    void setUp() {
        Platform.runLater(() -> {
            new MockUp<Persistence>() {
                @Mock
                public static EntityManagerFactory createEntityManagerFactory(String persistenceUnitName) {
                    return entityManagerFactory;
                }
            };

            controller = new RegistrationController();
            controller.setData(customHib);

            controller.loginField = new TextField();
            controller.passwordField = new PasswordField();
            controller.repeatPasswordField = new PasswordField();
            controller.nameField = new TextField();
            controller.surnameField = new TextField();
            controller.addressField = new TextField();
            controller.cardNoField = new TextField();
            controller.birthDateField = new DatePicker();
            controller.customerCheckbox = new RadioButton();
            controller.managerCheckbox = new RadioButton();
            controller.employeeIdField = new TextField();
            controller.employmentDateField = new DatePicker();
            controller.medCertificateField = new TextField();

            parent = new AnchorPane();
            ((AnchorPane) parent).getChildren().addAll(
                    controller.loginField, controller.passwordField, controller.repeatPasswordField,
                    controller.nameField, controller.surnameField, controller.addressField,
                    controller.cardNoField, controller.birthDateField, controller.customerCheckbox,
                    controller.managerCheckbox, controller.employeeIdField, controller.employmentDateField,
                    controller.medCertificateField
            );

            scene = new Scene(parent);
            stage = new Stage();
            stage.setScene(scene);

            new MockUp<JavaFxCustomsUtils>() {
                @Mock
                public void generateAlert(Alert.AlertType alertType, String title, String header, String content) {
                }
            };
        });

        WaitForAsyncUtils.waitForFxEvents();
    }



    @Test
    @DisplayName("Customer must be created when fields are valid")
    void testCreateUser_validCustomerFields() {
        givenCustomerFieldsValid();

        new Expectations() {{
            controller.nameField.getScene();
            result = scene;
            scene.getWindow();
            result = stage;
            customHib.create((Customer) any);
        }};

        Platform.runLater(controller::createUser);
        WaitForAsyncUtils.waitForFxEvents();

        new Verifications() {{
            JavaFxCustomsUtils.generateAlert(
                    Alert.AlertType.INFORMATION,
                    "Registration INFO",
                    "Success",
                    "User created"
            );
            times = 1;
        }};
    }

    @Test
    @DisplayName("Customer must not be created when password field is invalid")
    void testCreateUser_invalidCustomerFields1() {
        givenCustomerFieldsValid();
        controller.passwordField.setText("");

        new Expectations() {{
            controller.nameField.getScene();
            result = scene;
            scene.getWindow();
            result = stage;

        }};

        Platform.runLater(controller::createUser);
        WaitForAsyncUtils.waitForFxEvents();

        new Verifications() {{
            JavaFxCustomsUtils.generateAlert(
                    Alert.AlertType.INFORMATION,
                    "Registration INFO",
                    "Wrong data",
                    "Please check credentials, no password"
            );
            times = 1;

            customHib.create((Customer) any);
            times = 0;
        }};
    }

    @Test
    @DisplayName("Customer must not be created when passwords do not match")
    void testCreateUser_invalidCustomerFields2() {
        givenCustomerFieldsValid();
        controller.passwordField.setText("password1");
        controller.repeatPasswordField.setText("1pass");

        new Expectations() {{
            controller.nameField.getScene();
            result = scene;
            scene.getWindow();
            result = stage;

        }};

        Platform.runLater(controller::createUser);
        WaitForAsyncUtils.waitForFxEvents();

        new Verifications() {{
            JavaFxCustomsUtils.generateAlert(
                    Alert.AlertType.INFORMATION,
                    "Registration INFO",
                    "Wrong data",
                    "Passwords do not match"
            );
            times = 1;

            customHib.create((Customer) any);
            times = 0;
        }};
    }

    @Test
    @DisplayName("Manager must be created when fields are valid")
    void testCreateUser_validManagerFields() {
        givenManagerFieldsValid();

        new Expectations() {{
            controller.nameField.getScene();
            result = scene;
            scene.getWindow();
            result = stage;
            customHib.create((Customer) any);
        }};

        Platform.runLater(controller::createUser);
        WaitForAsyncUtils.waitForFxEvents();

        new Verifications() {{
            JavaFxCustomsUtils.generateAlert(
                    Alert.AlertType.INFORMATION,
                    "Registration INFO",
                    "Success",
                    "User created"
            );
            times = 1;
        }};
    }

    @Test
    @DisplayName("Exception is printed when returning to login screen fails")
    void testCreateUser_loginScreenError() throws IOException {
        givenManagerFieldsValid();

        new Expectations() {{
            controller.nameField.getScene();
            result = scene;
            scene.getWindow();
            result = stage;
            customHib.create((Customer) any);
            fxmlLoader.load();
            controller.currentUser = null;
            result = new IOException();
        }};

        Platform.runLater(controller::createUser);
        WaitForAsyncUtils.waitForFxEvents();

        new Verifications() {{
            JavaFxCustomsUtils.generateAlert(
                    Alert.AlertType.INFORMATION,
                    "Registration INFO",
                    "Success",
                    "User created"
            );
            times = 1;
        }};
    }

    private void givenCustomerFieldsValid() {
        controller.customerCheckbox.setSelected(true);
        controller.loginField.setText("login");
        controller.passwordField.setText("password");
        controller.repeatPasswordField.setText("password");
        controller.nameField.setText("login");
        controller.surnameField.setText("login");
        controller.addressField.setText("login");
        controller.cardNoField.setText("login");
        controller.birthDateField.setValue(LocalDate.now());
    }

    private void givenManagerFieldsValid() {
        controller.managerCheckbox.setSelected(true);
        controller.loginField.setText("login");
        controller.passwordField.setText("password");
        controller.repeatPasswordField.setText("password");
        controller.nameField.setText("name");
        controller.employeeIdField.setText("employee id");
        controller.employmentDateField.setValue(LocalDate.now());
        controller.medCertificateField.setText("certificate");
        controller.birthDateField.setValue(LocalDate.now());
    }
}
