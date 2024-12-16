package com.coursework.eshop.fxController.MainShop;

import com.coursework.eshop.HibernateControllers.CustomHib;
import com.coursework.eshop.HibernateControllers.EntityManagerFactorySingleton;
import com.coursework.eshop.fxController.JavaFxCustomsUtils;
import com.coursework.eshop.fxController.RegistrationController;
import com.coursework.eshop.model.Customer;
import com.coursework.eshop.model.CustomerOrder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import javafx.application.Platform;
import javafx.scene.control.*;
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

    @Injectable
    private EntityTransaction entityTransaction;

    private RegistrationController controller;

    @BeforeAll
    static void initToolkit() {
        if (!Platform.isFxApplicationThread()) {
            Platform.startup(() -> {});
        }
    }

    @BeforeEach
    void setUp() {
        controller = new RegistrationController();
        controller.setData(customHib);

        new Expectations() {{
            Persistence.createEntityManagerFactory("coursework-eshop");
            result = entityManagerFactory;
//            entityManagerFactory.createEntityManager();
//            result = entityManager;

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

        }};

        new MockUp<JavaFxCustomsUtils>() {
            @Mock
            public void generateAlert(Alert.AlertType alertType, String title, String header, String content) {
            }
        };
    }


    @Test
    @DisplayName("Customer must be created when fields are valid")
    void testCreateUser_validCustomerFields() {
        givenCustomerFieldsValid();
        new Expectations() {{
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

//        controller.createUser();

        new Verifications() {

        };
        // assert success alert generated

    }


    @Test
    @DisplayName("Manager must not be created when a field is invalid")
    void testCreateUser_invalidManagerField() {

//        controller.createUser();

        // assert error alert generated
    }

    // todo: test the bitfields

    private void givenCustomerFieldsValid() {
        controller.loginField.setText("login");
        controller.passwordField.setText("login");
        controller.repeatPasswordField.setText("login");
        controller.nameField.setText("login");
        controller.surnameField.setText("login");
        controller.addressField.setText("login");
        controller.cardNoField.setText("login");
        controller.birthDateField.setValue(LocalDate.now());
        controller.customerCheckbox.setSelected(true);
    }

}
