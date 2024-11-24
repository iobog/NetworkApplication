package com.example.laborator78.controller;

import com.example.laborator78.HelloApplication;
import com.example.laborator78.domain.UserRequestDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FriendshipRequestsController {

    @FXML
    public Button buttonAccept;
    @FXML
    public Button buttonReject;
    @FXML
    TableView<UserRequestDTO> tableView;

    @FXML
    private TableColumn<UserRequestDTO,String> tableColumnFirstName;
    @FXML
    TableColumn<UserRequestDTO,String> tableColumnLastName;
    @FXML
    TableColumn<UserRequestDTO,String> tableColumnDate;

    @FXML
    public Button backButton;

    ObservableList<UserRequestDTO> model = FXCollections.observableArrayList();

    HelloApplication app;

    public void setApp(HelloApplication app){
        this.app = app;
        //this.app.service.addObserver(this);
        initModel();
    }

    @FXML
    public void initialize() {
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<UserRequestDTO, String>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<UserRequestDTO, String>("lastName"));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<UserRequestDTO,String >("created_at"));
        tableView.setItems(model);

        buttonAccept.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull());
        buttonReject.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull());
    }

    private void initModel() {
        var id = app.user.getId();

        Iterable<UserRequestDTO> requests = app.service.getFriendshipRequests(app.user);
        List<UserRequestDTO> users = StreamSupport.stream(requests.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(users);
    }


    public void onAcceptButtonClick(ActionEvent actionEvent){
        try{
            UserRequestDTO userRequestDTO = tableView.getSelectionModel().getSelectedItem();
            if(userRequestDTO != null){
                app.service.acceptFriendshipRequest(app.user, userRequestDTO);
                app.showSucces("Friendship request accepted!");
                initModel();
            }
        }
        catch (Exception e){
            e.printStackTrace();
            app.showError("Unable to accept the friendship request. Please try again.");
        }
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

    public void onRejectButtonClick(ActionEvent actionEvent) {
        try{
            UserRequestDTO userRequestDTO = tableView.getSelectionModel().getSelectedItem();
            if(userRequestDTO != null){
                app.service.rejectFriendshipRequest(app.user, userRequestDTO);
                app.showSucces("Friendship request rejected!");
                initModel();
            }
        }
        catch (Exception e){
            e.printStackTrace();
            app.showError("Unable to accept the friendship request. Please try again.");
        }
    }
}
