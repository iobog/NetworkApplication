package com.example.laborator78.controller;

import com.example.laborator78.HelloApplication;
import com.example.laborator78.domain.Message;
import com.example.laborator78.domain.MessageDTO;
import com.example.laborator78.domain.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class ConversationController {

    @FXML
    public ListView<MessageDTO> conversationList;

    @FXML
    public TextField messageField;
    @FXML
    public Label conversationName;
    @FXML
    public Label selectedReplyMessageLable;
    @FXML
    public HBox replyBox;


    private MessageDTO selectedMessageForReply;

    private User user;
    HelloApplication app;

    public void setApp(HelloApplication app, User user) {
        this.app = app;
        this.user = user;
        conversationName.setText(user.getFirstName() + " " + user.getLastName());
        loadConversation();
    }


    private void loadConversation() {
        List<MessageDTO> messages = app.service.listMessages(app.user,user);
        ObservableList<MessageDTO> friendsList = FXCollections.observableArrayList();
        friendsList.setAll(messages);

        conversationList.setItems(friendsList);
        conversationList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(MessageDTO message, boolean empty) {
                super.updateItem(message, empty);
                if (empty || user == null) {
                    //setText(null);
                    setGraphic(null);
                } else {
                    VBox messageBox = new VBox(5);

                    if (message.getReplyMessage().isPresent()) {
                        Text replyText = new Text("Reply to: [" + message.getReplyMessage().get() + "]");
                        replyText.setStyle("-fx-fill: gray; -fx-font-style: italic; -fx-font-size: 10;");
                        messageBox.getChildren().add(replyText);
                    }

                    TextFlow messageTextFlow = new TextFlow(new Text(message.getMessage()));
                    messageTextFlow.setStyle("-fx-background-color: " + (message.getFrom_id().equals(user.getId()) ? "#d3d3d3" : "#f0f8ff") + "; -fx-padding: 10; -fx-background-radius: 10;");

                    messageBox.getChildren().add(messageTextFlow);

                    HBox container = new HBox();
                    container.getChildren().add(messageBox);
                    if (message.getFrom_id().equals(user.getId())) {
                        container.setStyle("-fx-alignment: CENTER-LEFT; -fx-padding: 5");
                    }
                    else {
                        container.setStyle("-fx-alignment: CENTER-RIGHT; -fx-padding: 5");
                    }
                    setGraphic(container);
                }
            }
        });

        conversationList.setOnMouseClicked(event -> {
            MessageDTO selectedMessage = conversationList.getSelectionModel().getSelectedItem();
            if (selectedMessage != null) {
                selectedMessageForReply = selectedMessage;
                replyBox.setVisible(true);
                selectedReplyMessageLable.setText("Reply to: [" + selectedMessage.getMessage() + "]");
                messageField.setPromptText("Reply to " + selectedMessage.getMessage());
            }
        });
    }


    public void onSendButtonClick(ActionEvent actionEvent) {
        try{
            String message = messageField.getText();

            if(selectedMessageForReply!=null)
                app.service.sendMessage(app.user,user,message, Optional.ofNullable(selectedMessageForReply.getId()));
            else
                app.service.sendMessage(app.user,user,message,Optional.empty());
            messageField.clear();
            clearReply();
            loadConversation();
        }
        catch (Exception e){
            e.printStackTrace();
            app.showError("Unable to send the message. Please try again.");
        }
    }

    public void onBackButtonClick(ActionEvent actionEvent) {
        try{
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            app.showFriendsView(window);
        }
        catch (Exception e){
            //e.printStackTrace();
            app.showError("Unable to load the user view. Please try again.");
        }

    }

    public void onClearReplyButtonClicked(ActionEvent actionEvent) {
        clearReply();
    }

    private void clearReply() {
        selectedMessageForReply = null;
        replyBox.setVisible(false);
        selectedReplyMessageLable.setText(null);
        messageField.setPromptText("Message");
    }
}
