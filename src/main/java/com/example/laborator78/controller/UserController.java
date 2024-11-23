package com.example.laborator78.controller;

import com.example.laborator78.service.Network;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class UserController {

    Network service;
    Stage dialogStage;

    public void setService(Network service,  Stage stage) {
        this.service = service;
        this.dialogStage=stage;
    }

    public void onAddContactButtonClick() {

    }

    public void onDeleteContactButtonClick() {

    }

    public void onLogoutButtonClick() {

    }

    public void onDeleteAccountButtonClick() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Account Deletion");
        alert.setHeaderText(null);
        alert.setContentText("Your account will be deleted in 30 seconds.");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/com/example/laborator78/css/custom-alert.css").toExternalForm());
        dialogPane.getStyleClass().add("custom-alert");

        alert.show();

        new Thread(() -> {
            for (int i = 30; i > 0; i--) {
                int finalI = i;
                javafx.application.Platform.runLater(() -> alert.setContentText("Your account will be deleted in " + finalI + " seconds."));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            javafx.application.Platform.runLater(() -> {
                alert.close();
                // Add account deletion logic here
                showAlert("Account Deleted", "Your account has been successfully deleted.");
            });
        }).start();
    }

    private void showAlert(String accountDeleted, String s) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(s);
        alert.setHeaderText(null);
        alert.setContentText(accountDeleted);

        // Add custom styling
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/com/example/laborator78/css/custom-alert.css").toExternalForm());
        dialogPane.getStyleClass().add("custom-alert");

        alert.showAndWait();
    }


    public void onContactRequestButtonClick(ActionEvent actionEvent) {
        List<UserRequestDTO> userRequests = service.getUserRequests();

    }
}
