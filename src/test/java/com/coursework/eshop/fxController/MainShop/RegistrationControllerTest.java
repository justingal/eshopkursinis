package com.coursework.eshop.fxController.MainShop;

import com.coursework.eshop.HibernateControllers.CustomHib;
import com.coursework.eshop.HibernateControllers.EntityManagerFactorySingleton;
import com.coursework.eshop.StartGui;
import com.coursework.eshop.fxController.JavaFxCustomsUtils;
import com.coursework.eshop.fxController.LoginController;
import com.coursework.eshop.fxController.RegistrationController;
import com.coursework.eshop.model.Admin;
import com.coursework.eshop.model.Customer;
import com.coursework.eshop.model.CustomerOrder;
import com.coursework.eshop.model.User;
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

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

class RegistrationControllerTest {

    @Mocked
    private CustomHib customHib;

    @Mocked
    private EntityManager entityManager;

    @Mocked
    private EntityManagerFactory entityManagerFactory;

    @Mocked
    private FXMLLoader fxmlLoader;

    @Mocked
    private LoginController loginController;

    @Injectable
    private EntityTransaction entityTransaction;


    private Scene scene;

    @Mocked
    private Stage stage;

    @Mocked
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
        controller = new RegistrationController();
        parent = new AnchorPane();
        scene = new Scene(parent);
        stage = new Stage();

        new Expectations() {{
            Persistence.createEntityManagerFactory("coursework-eshop");
            result = entityManagerFactory;
//            entityManagerFactory.createEntityManager();
//            result = entityManager;
            controller.setData(customHib);


//            entityManager.getTransaction(); result = entityTransaction;
//            entityTransaction.begin();
//            entityTransaction.commit();
//            entityManager.getTransaction().begin();
//            entityManager.persist(any);
//            entityManager.getTransaction().commit();
//            entityManager.close();

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

        }};

        new MockUp<JavaFxCustomsUtils>() {
            @Mock
            public void generateAlert(Alert.AlertType alertType, String title, String header, String content) {}
        };
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

//            fxmlLoader.getController();
//            result = loginController;
        }};

        controller.createUser();

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
    @DisplayName("Customer must not be created when a field is invalid")
    void testCreateUser_invalidCustomerField() {
        givenCustomerFieldsValid();
        controller.passwordField.setText("");

        controller.createUser();

        new Verifications() {{
            JavaFxCustomsUtils.generateAlert(
                    Alert.AlertType.INFORMATION,
                    "Registration INFO",
                    "Wrong data",
                    "Please check credentials, no login"
            );
            times = 1;
        }};
    }

    @Test
    @DisplayName("Manager must be created when fields are valid")
    void testCreateUser_validManagerFields() {
        givenManagerFieldsValid();
        new Expectations() {{
            controller.loginField.getScene();
            result = scene;

            customHib.create((Customer) any);
        }};

        controller.createUser();

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
    @DisplayName("Manager must not be created when a field is invalid")
    void testCreateUser_invalidManagerField() {
        givenManagerFieldsValid();
        controller.nameField.setText("");

        controller.createUser();

        new Verifications() {{
            JavaFxCustomsUtils.generateAlert(
                    Alert.AlertType.INFORMATION,
                    "Registration INFO",
                    "Wrong data",
                    "Please check credentials, no name"
            );
            times = 1;
        }};
    }

    // todo: test the bitfields

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
