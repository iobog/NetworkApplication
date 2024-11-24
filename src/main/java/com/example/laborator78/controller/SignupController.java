package com.example.laborator78.controller;

import com.example.laborator78.HelloApplication;
import com.example.laborator78.domain.User;
import com.example.laborator78.service.Network;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SignupController {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField confirmPasswordField;


    private HelloApplication app;

    public void setApp(HelloApplication app) {
        this.app = app;
    }

    public void onCancelButtonClick(ActionEvent actionEvent) {
        try {
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            app.showHelloView(window);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onSubmitButtonClick(ActionEvent actionEvent) {
        // Get data from fields
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();// Get data from fields

        // Check if the password and confirm password match else show an pop up error
        if (!password.equals(confirmPassword)) {
            // Show an error if the passwords don't match
            showAlert("Password mismatch", "The passwords you entered do not match.");
            return;
        }

        User user = new User(firstName, lastName, email, password);

        if(app.service.findUserByEmail(email)){
            showAlert("Error", "User already exists");
            return;
        }

        try{
            app.service.addUser(user);
            showAlert("Success", "User has been successfully created.");
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            app.showUserView(window);

        }
        catch (Exception e){
            showAlert("Error", "There was an error creating the user.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Add custom styling
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/com/example/laborator78/css/custom-alert.css").toExternalForm());
        dialogPane.getStyleClass().add("custom-alert");

        alert.showAndWait();
    }
}
