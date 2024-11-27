package com.example.laborator78.controller;

import com.example.laborator78.HelloApplication;
import com.example.laborator78.domain.User;
import com.example.laborator78.domain.UserRequestDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SentFriendshipRequestsController {

    @FXML
    public TableView <UserRequestDTO>tableView;
    @FXML
    public TableColumn <UserRequestDTO,String>tableColumnFirstName;
    @FXML
    public TableColumn <UserRequestDTO,String>tableColumnLastName;
    @FXML
    public TableColumn <UserRequestDTO,String> tableColumnStatus;
    @FXML
    public Button buttonDeleteRequest;


    private HelloApplication app;
    private User currentUser;
    private ObservableList<UserRequestDTO> model = FXCollections.observableArrayList();

    @FXML
    private ListView<User> recommendedFriendsListView;


    public void setApp(HelloApplication helloApplication) {
        this.app = helloApplication;

    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        initModel();
    }

    @FXML
    public void initialize() {
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<UserRequestDTO, String>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<UserRequestDTO, String>("lastName"));
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<UserRequestDTO, String>("status"));
        tableView.setItems(model);

        buttonDeleteRequest.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull());
    }

    private void initModel() {
        Iterable<UserRequestDTO> requests = app.service.getSentFriendshipRequests(currentUser);
        List<UserRequestDTO> users = StreamSupport.stream(requests.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(users);
    }




    public void onDeleteButtonClick(ActionEvent actionEvent) {
        UserRequestDTO userRequestDTO = tableView.getSelectionModel().getSelectedItem();
        app.service.deleteFriendshipRequest(userRequestDTO);
        initModel();
    }

    public void onBackButtonClick(ActionEvent actionEvent) {
        try{

            Stage window = (Stage) buttonDeleteRequest.getScene().getWindow();
            app.showUserView(window,currentUser);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
