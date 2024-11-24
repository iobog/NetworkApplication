package com.example.laborator78.controller;


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

    private Network service;
    Stage dialogStage;

    public void setService(Network service,  Stage stage) {
        this.service = service;
        this.dialogStage=stage;
    }

    public void onSubmitButtonClick(ActionEvent actionEvent) {
        // Get data from fields
        String email = emailField.getText();
        String password = passwordField.getText();

        // Check if the password and confirm password match else show an pop up error
        User user = service.findUserByEmailAndPassword(email, password);
        if (user!=null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/laborator78/view/user-view.fxml"));
                Parent loginView = loader.load();
                UserController userController = loader.getController();
                userController.setService(service, dialogStage,user);
                Scene loginScene = new Scene(loginView);
                Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                window.setScene(loginScene);
                window.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/laborator78/view/hello-view.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onCancelButtonClick(ActionEvent actionEvent) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/laborator78/view/hello-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Add methods to handle login logic here
}

