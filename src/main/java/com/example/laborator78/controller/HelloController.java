package com.example.laborator78.controller;

import com.example.laborator78.HelloApplication;
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

    private HelloApplication app;

    private Network service;
    Stage dialogStage;

    public void setService(Network service,  Stage stage) {
        this.service = service;
        this.dialogStage=stage;
    }

    public void setApp(HelloApplication app) {
        this.app = app;
    }

    @FXML
    public void onSignupButtonClick(ActionEvent actionEvent) {
        try {
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            app.showSignupView(window);
        } catch (IOException e) {
            e.printStackTrace();
            app.showError("Unable to load the signup view. Please try again.");
        }
    }


    @FXML
    public void onLoginButtonClick(ActionEvent actionEvent) {
        try {
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            app.showLoginView(window);
        } catch (IOException e) {
            // Print error and show alert
            e.printStackTrace();
            app.showError("Unable to load the login view. Please try again.");
        }
    }
}