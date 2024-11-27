package com.example.laborator78.controller;

import com.example.laborator78.HelloApplication;
import com.example.laborator78.domain.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class FriendsController {

    private HelloApplication app;

    @FXML
    private ListView<User> contactList;
    private User currentUser;

    public void setApp(HelloApplication app) {
        this.app = app;
    }

    private void loadFriendsList() {
        List<Optional<User>> friends = app.service.getListFriends(currentUser);
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

    public void onBackButtonClick(ActionEvent actionEvent) {
        try{
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            app.showUserView(window,currentUser);
        }
        catch (Exception e){
            e.printStackTrace();
            app.showError("Unable to load the user view. Please try again.");
        }

    }

    public void onConversationButtonClick(ActionEvent actionEvent) {

        User user = contactList.getSelectionModel().getSelectedItem();
        try{
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            app.showConversationView(window, user,currentUser);
        }
        catch (Exception e){
            e.printStackTrace();
            app.showError("Unable to load the Conversation. Please try again.");
        }

    }

    public void setUser(User user) {
        this.currentUser = user;
        loadFriendsList();
    }
}
