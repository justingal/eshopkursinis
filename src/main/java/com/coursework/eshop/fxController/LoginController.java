package com.coursework.eshop.fxController;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.coursework.eshop.HibernateControllers.CustomHib;
import com.coursework.eshop.HibernateControllers.EntityManagerFactorySingleton;
import com.coursework.eshop.StartGui;
import com.coursework.eshop.fxController.MainShop.MainShopController;
import com.coursework.eshop.model.User;
import jakarta.persistence.EntityManagerFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class LoginController implements Initializable {

    @FXML
    public TextField loginField;
    @FXML
    public PasswordField passwordField;
    private EntityManagerFactory entityManagerFactory = EntityManagerFactorySingleton.getEntityManagerFactory();

    public void registerNewUser() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("registration.fxml"));
        Parent parent = fxmlLoader.load();
        RegistrationController registrationController = fxmlLoader.getController();
        Scene scene = new Scene(parent);
        Stage stage = (Stage) loginField.getScene().getWindow();
        stage.setTitle("Shop");
        stage.setScene(scene);
        stage.show();
    }

    public void validateAndConnect() throws IOException {
        CustomHib userHib = new CustomHib();
        User user = userHib.getUserByLogin(loginField.getText());

        if (user != null) {
            BCrypt.Result result = BCrypt.verifyer().verify(passwordField.getText().toCharArray(), user.getPassword());
            if (result.verified) {
                StartGui.currentUser = user;
                FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("main-shop.fxml"));
                Parent parent = fxmlLoader.load();
                MainShopController mainShopController = fxmlLoader.getController();
                mainShopController.setData();
                Scene scene = new Scene(parent);
                Stage stage = (Stage) loginField.getScene().getWindow();
                stage.setTitle("Shop");
                stage.setScene(scene);
                stage.show();

            } else {
                JavaFxCustomsUtils.generateAlert(Alert.AlertType.INFORMATION, "Login INFO", "Wrong data", "Please check credentials, incorrect password");
            }
        } else {
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.INFORMATION, "Login INFO", "Wrong data", "Please check credentials, no such user");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        entityManagerFactory = EntityManagerFactorySingleton.getEntityManagerFactory();
    }

    public void setData(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

}