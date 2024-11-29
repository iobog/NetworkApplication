package com.example.laborator78.controller;

import com.example.laborator78.HelloApplication;
import com.example.laborator78.domain.User;
import com.example.laborator78.domain.pagining.Page;
import com.example.laborator78.domain.pagining.Pageable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class FriendsController {

    public Button buttonPrevious;
    public Label labelPage;
    public Button buttonNext;
    private HelloApplication app;

    @FXML
    private ListView<User> contactList;

    private final int pageSize = 5;
    private int currentPage = 0;
    private int totalNumberOfElements = 0;

    private User currentUser;

    public void setApp(HelloApplication app) {
        this.app = app;
    }

    private void loadFriendsList() {
        Pageable pageable = new Pageable(currentPage, pageSize);
        Page<User> userPage = app.service.getFriendshipPaged(currentUser.getId(), pageable);
        totalNumberOfElements = userPage.getTotalNumberOfElements();

        ObservableList<User> friends = FXCollections.observableArrayList(userPage.getContent());

        contactList.setItems(friends);
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




        labelPage.setText("Page: " + (currentPage + 1) + " / " + ((totalNumberOfElements + pageSize - 1) / pageSize));

        buttonPrevious.setDisable(currentPage == 0);
        buttonNext.setDisable((currentPage + 1) * pageSize >= totalNumberOfElements);


        buttonPrevious.setDisable(currentPage == 0);
        buttonNext.setDisable((currentPage + 1) * pageSize >= totalNumberOfElements);

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

    private void updateList() {

    }

    public void setUser(User user) {
        this.currentUser = user;
        loadFriendsList();
    }

    @FXML
    private void handleNextPage() {
        if ((currentPage + 1) * pageSize < totalNumberOfElements) {
            currentPage++;
            loadFriendsList();
        }
    }

    @FXML
    private void handlePreviousPage() {
        if (currentPage > 0) {
            currentPage--;
            loadFriendsList();
        }

    }
}
