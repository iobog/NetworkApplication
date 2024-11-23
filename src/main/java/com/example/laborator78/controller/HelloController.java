package com.example.laborator78.controller;

import com.example.laborator78.service.Network;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {
    @FXML
    private Label welcomeText;

    private Network service;
    Stage dialogStage;

    public void setService(Network service,  Stage stage) {
        this.service = service;
        this.dialogStage=stage;
    }

    @FXML
    public void onSignupButtonClick(ActionEvent actionEvent) {
        try {
            // Load the Signup view using FXMLLoader
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/laborator78/view/signup-view.fxml"));
            Parent signupView = loader.load();

            // Get the SignupController instance
            SignupController signupController = loader.getController();

            // Pass the service and stage to the SignupController
            signupController.setService(service, dialogStage);

            // Set the new scene
            Scene signupScene = new Scene(signupView);
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(signupScene);
            window.show();
        } catch (IOException e) {
            // Print error and show alert
            e.printStackTrace();
            showError("Unable to load the signup view. Please try again.");
        }
    }


    @FXML
    public void onLoginButtonClick(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/laborator78/view/login-view.fxml"));
            Parent loginView = loader.load();

            // Get the LoginController instance
            LoginController loginController = loader.getController();

            // Pass the service and stage to the LoginController
            loginController.setService(service, dialogStage);

            // Set the new scene
            Scene loginScene = new Scene(loginView);
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(loginScene);
            window.show();
        } catch (IOException e) {
            // Print error and show alert
            e.printStackTrace();
            showError("Unable to load the login view. Please try again.");
        }
    }


    private void showError(String message) {
        // Optional: Use an alert dialog to show error messages
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}