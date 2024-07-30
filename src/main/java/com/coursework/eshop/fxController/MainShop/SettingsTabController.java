package com.coursework.eshop.fxController.MainShop;

import com.coursework.eshop.StartGui;
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
            // Correctly reference the FXML file relative to the classpath
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/coursework/eshop/registration.fxml"));
            if (loader.getLocation() == null) {
                throw new IllegalStateException("FXML file not found");
            }
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Manager Registration");  // Optionally set a title for the window
            stage.initModality(Modality.APPLICATION_MODAL); // Block other windows if necessary
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();  // Handle possible IOException more gracefully here
        } catch (IllegalStateException e) {
            e.printStackTrace();  // Handle if the FXML file wasn't found or other state issues
        }
    }
}
