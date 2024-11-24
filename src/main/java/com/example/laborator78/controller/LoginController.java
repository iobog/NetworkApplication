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


    public void setApp(HelloApplication app) {
        this.app = app;
    }

    public void onSubmitButtonClick(ActionEvent actionEvent) {
        // Get data from fields
        String email = emailField.getText();
        String password = passwordField.getText();

        // Check if the password and confirm password match else show an pop up error
        User user = app.service.findUserByEmailAndPassword(email, password);
        if (user!=null) {
            try {
                app.user = user;
                Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                app.showUserView(window);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                app.showHelloView(window);
            } catch (IOException e) {
                e.printStackTrace();
            }
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

