package com.example.laborator78.controller;

import com.example.laborator78.HelloApplication;
import com.example.laborator78.domain.MessageDTO;
import com.example.laborator78.domain.User;
import com.example.laborator78.utils.events.Event;
import com.example.laborator78.utils.observer.Observer;
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
import java.util.stream.Collectors;

public class ConversationController implements Observer {

    @FXML
    public ListView<MessageDTO> conversationList;
    @FXML
    public TextField messageField;
    @FXML
    public Label conversationName;
    @FXML
    public Label selectedReplyMessageLabel;
    @FXML
    public HBox replyBox;
    @FXML
    public TextField searchField;

    private MessageDTO selectedMessageForReply;
    private User user, currentUser;
    private HelloApplication app;
    private ObservableList<MessageDTO> conversationMessages;
    private ObservableList<MessageDTO> filteredMessages;

    public void setApp(HelloApplication app, User user) {
        this.app = app;
        this.user = user;
        app.service.addObserver(this); // Add this controller as an observer
        conversationName.setText(user.getFirstName() + " " + user.getLastName());
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        loadConversation();
    }

    private void loadConversation() {
        List<MessageDTO> messages = app.service.listMessages(currentUser, user);
        conversationMessages = FXCollections.observableArrayList(messages);
        filteredMessages = FXCollections.observableArrayList(messages); // Initially no filtering

        conversationList.setItems(filteredMessages);
        conversationList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(MessageDTO message, boolean empty) {
                super.updateItem(message, empty);
                if (empty || message == null) {
                    setGraphic(null);
                } else {
                    VBox messageBox = new VBox(3);

                    if (message.getReplyMessage().isPresent()) {
                        Text replyText = new Text("Reply to: [" + message.getReplyMessage().get() + "]");
                        replyText.setStyle("-fx-fill: gray; -fx-font-style: italic; -fx-font-size: 10;");
                        messageBox.getChildren().add(replyText);
                    }

                    TextFlow messageTextFlow = new TextFlow(new Text(message.getMessage()));
                    messageTextFlow.setStyle("-fx-background-color: " +
                            (message.getFrom_id().equals(user.getId()) ? "#d3d3d3" : "#f0f8ff") +
                            "; -fx-padding: 10; -fx-background-radius: 10;");

                    messageBox.getChildren().add(messageTextFlow);

                    HBox container = new HBox();
                    container.getChildren().add(messageBox);
                    if (message.getFrom_id().equals(user.getId())) {
                        container.setStyle("-fx-alignment: CENTER-LEFT; -fx-padding: 5");
                    } else {
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
                selectedReplyMessageLabel.setText("Reply to: [" + selectedMessage.getMessage() + "]");
                messageField.setPromptText("Reply to " + selectedMessage.getMessage());
            }
        });

        // Add listener for searchField changes
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterMessages(newValue);
        });
    }

    private void filterMessages(String query) {
        if (query == null || query.isEmpty()) {
            filteredMessages.setAll(conversationMessages); // If no query, show all messages
        } else {
            String lowerCaseQuery = query.toLowerCase();
            filteredMessages.setAll(conversationMessages.stream()
                    .filter(message -> message.getMessage().toLowerCase().contains(lowerCaseQuery))
                    .collect(Collectors.toList()));
        }
    }

    public void onSendButtonClick(ActionEvent actionEvent) {
        try {
            String message = messageField.getText();

            if (selectedMessageForReply != null)
                app.service.sendMessage(currentUser, user, message, Optional.ofNullable(selectedMessageForReply.getId()));
            else
                app.service.sendMessage(currentUser, user, message, Optional.empty());
            messageField.clear();
            clearReply();
        } catch (Exception e) {
            e.printStackTrace();
            app.showError("Unable to send the message. Please try again.");
        }
    }

    public void onBackButtonClick(ActionEvent actionEvent) {
        try {
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            app.showFriendsView(window, currentUser);
        } catch (Exception e) {
            app.showError("Unable to load the user view. Please try again.");
        }
    }

    public void onClearReplyButtonClicked(ActionEvent actionEvent) {
        clearReply();
    }

    private void clearReply() {
        selectedMessageForReply = null;
        replyBox.setVisible(false);
        selectedReplyMessageLabel.setText(null);
        messageField.setPromptText("Message");
    }

    @Override
    public void update(Event event) {
        if (event.getType().equals("message_sent")) {
            MessageDTO newMessage = (MessageDTO) event.getData();
            if ((newMessage.getFrom_id().equals(user.getId()) || newMessage.getTo_id().equals(user.getId())) &&
                    (newMessage.getFrom_id().equals(currentUser.getId()) || newMessage.getTo_id().equals(currentUser.getId()))) {
                conversationMessages.add(newMessage); // Add new message to the list
                conversationList.scrollTo(conversationMessages.size() - 1); // Scroll to the latest message
            }
        }
    }

    @Override
    public void update(String eventType, Object data) {
        if ("message_sent".equals(eventType) && data instanceof MessageDTO newMessage) {
            if ((newMessage.getFrom_id().equals(user.getId()) || newMessage.getTo_id().equals(user.getId())) &&
                    (newMessage.getFrom_id().equals(currentUser.getId()) || newMessage.getTo_id().equals(currentUser.getId()))) {
                conversationMessages.add(newMessage); // Add new message to the list
                filterMessages(searchField.getText()); // Re-filter messages based on the current search query
                conversationList.scrollTo(conversationMessages.size() - 1); // Scroll to the latest message
            }
        }
    }

}
