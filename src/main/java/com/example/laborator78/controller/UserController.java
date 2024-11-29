package com.example.laborator78.controller;

import com.example.laborator78.HelloApplication;
import com.example.laborator78.domain.User;
import com.example.laborator78.service.Network;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class UserController {

    @FXML
    public VBox mainBox;

    //private User currentUser;

    @FXML
    private ListView<User> contactList;
    @FXML
    private ListView<User> nonContactList;
    @FXML
    private AnchorPane addContactPane;

    @FXML
    private TextField contactEmailField;
    @FXML
    private TextField contactNameField;
    @FXML
    private Label firstNameField;
    @FXML
    private Label lastNameField;



    private Network service;
    //private Stage dialogStage;

    private HelloApplication app;
    private User currentUser;

    public void setApp(HelloApplication app) {
        this.app = app;
        service = app.service;
    }

    public void setUser(User user) {
        this.currentUser = user;
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
    }

    public void onShowFrindsButtonClick(ActionEvent actionEvent) {
        try {
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            app.showFriendsView(window, currentUser);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void onLogoutButtonClick(ActionEvent actionEvent) {
        try {
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            app.showHelloView(window);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void onDeleteAccountButtonClick(ActionEvent actionEvent) throws IOException{
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Account");
            alert.setHeaderText("Are you sure you want to delete your account?");
            alert.setContentText("This action is irreversible.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                service.removeUser(currentUser.getId());
                showAlert("Account deleted", "Account deleted");
                Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                app.showHelloView(window);
            }
        }
        catch (IOException e) {
            showAlert("Account deletion failed", "Account deletion failed");
            e.printStackTrace();
        }
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
    }

    public void onShowRecommendedFriendsButtonClick(ActionEvent actionEvent) {

        try {
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            //Stage window = (Stage)mainBox.getScene().getWindow();
            app.showRecommendedFriendsView(window,currentUser);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onShowFriendsRequestButtonClick(ActionEvent actionEvent) {

        try {
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            app.showFriendRequestsView(window,currentUser);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onShowSentFriendsRequestButtonClick(ActionEvent actionEvent) {

        try {
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            app.showSentFriendRequestsView(window,currentUser);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
