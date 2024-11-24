package com.example.laborator78.controller;

import com.example.laborator78.HelloApplication;
import com.example.laborator78.domain.User;
import com.example.laborator78.domain.UserRequestDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class RecommendedFriendsController {

    @FXML
    public TableView<User>tableView;
    @FXML
    public TableColumn<User,String> tableColumnFirstName;
    @FXML
    public TableColumn<User,String> tableColumnLastName;
    @FXML
    public Button buttonSendRequest;



    ObservableList<User> model = FXCollections.observableArrayList();

    private HelloApplication app;

    @FXML
    private ListView<User> recommendedFriendsListView;


    public void setApp(HelloApplication helloApplication) {
        this.app = helloApplication;
        initModel();
    }


    @FXML
    public void initialize() {
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        tableView.setItems(model);

        buttonSendRequest.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull());
    }

    private void initModel() {
        Iterable<User> requests = app.service.getRecommendedFriends(app.user);
        List<User> users = StreamSupport.stream(requests.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(users);
    }




    public void onBackButtonClick(ActionEvent actionEvent) {

        try{
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            app.showUserView(window);
        }
        catch (Exception e){
            e.printStackTrace();
            app.showError("Unable to load the user view. Please try again.");
        }
    }


    public void onSendRequestButtonClick(ActionEvent actionEvent) {

        try{
            User user = tableView.getSelectionModel().getSelectedItem();
            if(user != null){
                app.service.sendFriendshipRequest(app.user, user);
                app.showSucces("Friendship request sent!");
                initModel();
            }
        }
        catch (Exception e){
            e.printStackTrace();
            app.showError("Unable to send the friendship request. Please try again.");
        }
    }
}
