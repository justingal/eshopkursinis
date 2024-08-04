package com.coursework.eshop.fxController.MainShop;

import com.coursework.eshop.HibernateControllers.CustomHib;
import com.coursework.eshop.StartGui;
import com.coursework.eshop.fxController.RegistrationController;
import com.coursework.eshop.model.Admin;
import com.coursework.eshop.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingsTabController {
    @FXML
    public Button logOutButton;
    @FXML
    public Button exitButton;
    public Button managerRegistrationButton;

    private User currentUser = StartGui.currentUser;
    private CustomHib customHib;

    @FXML
    public void initialize() {
        updateUIBasedOnUserRole();
    }

    private void updateUIBasedOnUserRole() {
        if (!(currentUser instanceof Admin) && currentUser != null) {
            managerRegistrationButton.setVisible(false);
        } else {
            managerRegistrationButton.setVisible(true);
        }
    }

    public void logOut(ActionEvent actionEvent) {
        try {
            // Load the login screen
            StartGui.currentUser = null;
            FXMLLoader loader = new FXMLLoader(StartGui.class.getResource("login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) logOutButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exitProgram(ActionEvent actionEvent) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    public void openManagerRegistrationField(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/coursework/eshop/registration.fxml"));
            Parent root = loader.load();
            RegistrationController registrationController = loader.getController();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Registration");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

}
