package com.example.laborator78.controller;

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
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javafx.scene.Scene;

public class UserController {

    private User currentUser;

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
    private Stage dialogStage;

    public void setService(Network service,  Stage stage,User user) {
        this.service = service;
        this.dialogStage=stage;
        this.currentUser=user;
        firstNameField.setText(currentUser.getFirstName());
        lastNameField.setText(currentUser.getLastName());
        loadFriendsList();
        loadNonFirendsList();

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
        dialogStage.close();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/laborator78/view/Hello-view.fxml"));
            Parent hellowView = loader.load();
            HelloController userController = loader.getController();
            userController.setService(service, dialogStage);
            Scene loginScene = new Scene(hellowView);
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(loginScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        if (userRequests.size() == 0) {
            showAlert("No Requests", "There are no contact requests.");
        } else {

        }

    }
    private void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }


    private void loadFriendsList() {
        List<Optional<User>> friends = service.getListFriends(currentUser);
        ObservableList<User> friendsList = FXCollections.observableArrayList();
        for (Optional<User> friend : friends) {
            friend.ifPresent(friendsList::add);
        }
        contactList.setItems(friendsList);
        contactList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setText(null);
                } else {
                    setText(user.getFirstName() + " " + user.getLastName());
                }
            }
        });


    }

    private void loadNonFirendsList(){
        List<Optional<User>> nonFriends = service.getListNonFriends(currentUser);
        ObservableList<User> nonFriendsList = FXCollections.observableArrayList();
        for (Optional<User> nonFriend : nonFriends) {
            nonFriend.ifPresent(nonFriendsList::add);
        }
        nonContactList.setItems(nonFriendsList);
        nonContactList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setText(null);
                } else {
                    setText(user.getFirstName() + " " + user.getLastName());
                }
            }
        });
    }

}
