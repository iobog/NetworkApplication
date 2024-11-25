package com.example.laborator78.controller;

import com.example.laborator78.HelloApplication;
import com.example.laborator78.domain.User;
import com.example.laborator78.domain.UserRequestDTO;
import com.example.laborator78.service.Network;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javafx.scene.Scene;

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

    public void setApp(HelloApplication app) {
        this.app = app;
        service = app.service;
        firstNameField.setText(app.user.getFirstName());
        lastNameField.setText(app.user.getLastName());
    }

    public void onShowFrindsButtonClick(ActionEvent actionEvent) {
        try {
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            app.showFriendsView(window);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onAddContactButtonClick() {
//        // Fetch non-friend users
//        List<User> nonFriendUsers = service.getNonFriendUsers(currentUser);
//
//        // Display in the nonFriendListView
//        if (nonFriendUsers.isEmpty()) {
//            showAlert("No Users", "There are no users to add.");
//            return;
//        }
//
//        nonFriendListView.getItems().setAll(nonFriendUsers);
//
//        // Custom cell factory to show "Request" button for each user
//        nonFriendListView.setCellFactory(listView -> new ListCell<User>() {
//            @Override
//            protected void updateItem(User user, boolean empty) {
//                super.updateItem(user, empty);
//                if (empty || user == null) {
//                    setGraphic(null);
//                    return;
//                }
//
//                HBox container = new HBox(10);
//                Label userName = new Label(user.getFirstName() + " " + user.getLastName());
//                Button requestButton = new Button("Request");
//
//                // Action for the request button
//                requestButton.setOnAction(event -> sendContactRequest(user));
//
//                container.getChildren().addAll(userName, requestButton);
//                setGraphic(container);
//            }
//        });
//
//        // Show the pane containing the list
//        addContactPane.setVisible(true);
    }


    //    // Method to handle sending a contact request
//    private void sendContactRequest(User user) {
//        boolean success = service.sendContactRequest(currentUser, user);
//        if (success) {
//            showAlert("Request Sent", "Contact request sent to " + user.getFirstName() + ".");
//            nonFriendListView.getItems().remove(user); // Remove user from list on success
//        } else {
//            showAlert("Request Failed", "Failed to send request to " + user.getFirstName() + ".");
//        }
//    }
//
//    // Hide the Add Contact Pane
    @FXML
    private void onCloseAddContactPane() {
        addContactPane.setVisible(false);
    }

    public void onDeleteContactButtonClick() {

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
                service.removeUser(app.user.getId());
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
            app.showRecommendedFreindsView(window);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onShowFriendsRequestButtonClick(ActionEvent actionEvent) {

        try {
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            app.showFriendRequestsView(window);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onShowSentFriendsRequestButtonClick(ActionEvent actionEvent) {

        try {
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            app.showSentFriendRequestsView(window);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
