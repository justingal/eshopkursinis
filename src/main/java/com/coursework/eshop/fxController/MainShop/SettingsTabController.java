package com.coursework.eshop.fxController.MainShop;

import com.coursework.eshop.HibernateControllers.CustomHib;
import com.coursework.eshop.HibernateControllers.EntityManagerFactorySingleton;
import com.coursework.eshop.StartGui;
import com.coursework.eshop.fxController.RegistrationController;
import com.coursework.eshop.model.ShoppingCart;
import com.coursework.eshop.model.User;
import jakarta.persistence.EntityManagerFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class SettingsTabController {
    @FXML
    public Button logOutButton;
    @FXML
    public Button exitButton;
    public Button managerRegistrationButton;

    public void logOut(ActionEvent actionEvent) {
        try {
            // Load the login screen
            FXMLLoader loader = new FXMLLoader(StartGui.class.getResource("login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) logOutButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

            // Optional: Perform any additional cleanup here if needed
            // For example, clearing any static user data or settings
        } catch (IOException e) {
            e.printStackTrace();  // Handle possible IO exceptions like a missing FXML file
        }
    }

    public void exitProgram(ActionEvent actionEvent) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close(); // Closes the application window
    }

    public void openManagerRegistrationField(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/coursework/eshop/registration.fxml"));
            Parent root = loader.load();
            RegistrationController registrationController = loader.getController();
            registrationController.setData(currentUser, customHib); // Set data right after loading
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Registration");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();  // Handle possible IOException more gracefully here
        } catch (IllegalStateException e) {
            e.printStackTrace();  // Handle if the FXML file wasn't found or other state issues
        }
    }
}
