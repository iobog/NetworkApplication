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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    private HelloApplication app;
    private User currentUser;


    public void setApp(HelloApplication app) {
        this.app = app;
    }

    public void onSubmitButtonClick(ActionEvent actionEvent) {
        // Example: Authenticate user based on input fields
        String username = emailField.getText();
        String password = passwordField.getText();

        var userOptional = app.service.findUserByEmailAndPassword(username, password);

        if (userOptional!=null) {
            currentUser = userOptional;
            try {
                Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                app.openUserView(window,currentUser);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Invalid credentials");
            app.showError("Invalid credentials. Please try again.");
        }
    }

    public void onCancelButtonClick(ActionEvent actionEvent) {
        try {
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            app.showHelloView(window);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Add methods to handle login logic here
}

