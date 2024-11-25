package com.example.laborator78;

import com.example.laborator78.controller.*;
import com.example.laborator78.domain.Friendship;
import com.example.laborator78.domain.Message;
import com.example.laborator78.domain.User;
import com.example.laborator78.domain.validators.FriendshipRequestValidator;
import com.example.laborator78.domain.validators.FriendshipValidator;
import com.example.laborator78.domain.validators.MessageValidator;
import com.example.laborator78.domain.validators.UserValidator;
import com.example.laborator78.repository.Repository;
import com.example.laborator78.repository.database.FriendshipDataBaseRepository;
import com.example.laborator78.repository.database.MessageDataBaseRepository;
import com.example.laborator78.repository.database.RequestDataBaseRepository;
import com.example.laborator78.repository.database.UserDataBaseRepository;
import com.example.laborator78.service.Network;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    public Network service;
    public User user;

    @Override
    public void start(Stage stage) throws IOException {

        System.out.println("Reading data from file");
        String username="postgres";
        String pasword="Parola2019";
        String url="jdbc:postgresql://localhost:5432/Retea";
        UserDataBaseRepository userRepository=
                new UserDataBaseRepository(url,username, pasword,  new UserValidator());

        FriendshipDataBaseRepository friendshipRepository =
                new FriendshipDataBaseRepository(url,username,pasword,new FriendshipValidator(userRepository));

        RequestDataBaseRepository requestRepository =
                new RequestDataBaseRepository(url,username,pasword, new FriendshipRequestValidator());

        MessageDataBaseRepository messageRepository =
                new MessageDataBaseRepository(url,username,pasword, new MessageValidator());

        service =new Network(userRepository,friendshipRepository,requestRepository,messageRepository);
        //24 28 29
        var user1 = service.findUser(Long.valueOf(29));
        if(user1.isPresent())
            this.user = user1.get();
        this.showUserView(stage);
        //initView(stage);

    }

    private void initView(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/example/laborator78/view/hello-view.fxml"));
        Scene scene = new Scene(loader.load(), 320, 240);
        primaryStage.setTitle("Hello!");

        HelloController controller = loader.getController();
        //controller.setService(service, primaryStage);
        controller.setApp(this);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void showHelloView(Stage stage) throws IOException {
        initView(stage);
    }

    public void showLoginView(Stage stage) throws  IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/laborator78/view/login-view.fxml"));
        Parent loginView = loader.load();

        // Get the LoginController instance
        LoginController loginController = loader.getController();

        // Pass the service and stage to the LoginController
        loginController.setApp(this);

        // Set the new scene
        Scene loginScene = new Scene(loginView);
        stage.setScene(loginScene);
        stage.show();
    }

    public void showSignupView(Stage stage) throws IOException {
        // Load the Signup view using FXMLLoader
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/laborator78/view/signup-view.fxml"));
        Parent signupView = loader.load();

        // Get the SignupController instance
        SignupController signupController = loader.getController();

        // Pass the service and stage to the SignupController
        //signupController.setService(service, dialogStage);
        signupController.setApp(this);

        // Set the new scene
        Scene signupScene = new Scene(signupView);
        stage.setScene(signupScene);
        stage.show();
    }

    public void showUserView(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/laborator78/view/user-view.fxml"));
        Parent loginView = loader.load();
        UserController userController = loader.getController();
        userController.setApp(this);
        Scene loginScene = new Scene(loginView);
        stage.setScene(loginScene);
        stage.show();
    }

    public void showFriendsView(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/laborator78/view/friends-view.fxml"));
        Parent loginView = loader.load();
        FriendsController friendsController = loader.getController();
        friendsController.setApp(this);
        Scene loginScene = new Scene(loginView);
        stage.setScene(loginScene);
        stage.show();
    }

    public void showRecommendedFreindsView(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/laborator78/view/recommended-friends-view.fxml"));
        Parent loginView = loader.load();
        RecommendedFriendsController recommendedFreindsController = loader.getController();
        recommendedFreindsController.setApp(this);
        Scene loginScene = new Scene(loginView);
        stage.setScene(loginScene);
        stage.show();
    }

    public void showFriendRequestsView(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/laborator78/view/friendship-requests-view.fxml"));
        Parent loginView = loader.load();
        FriendshipRequestsController friendRequestsController = loader.getController();
        friendRequestsController.setApp(this);
        Scene loginScene = new Scene(loginView);
        stage.setScene(loginScene);
        stage.show();
    }


    public void showSentFriendRequestsView(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/laborator78/view/sent-friendship-requests-view.fxml"));
        Parent loginView = loader.load();
        SentFriendshipRequestsController sentFriendshipRequestsController = loader.getController();
        sentFriendshipRequestsController.setApp(this);
        Scene loginScene = new Scene(loginView);
        stage.setScene(loginScene);
        stage.show();

    }

    public void showConversationView(Stage stage, User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/laborator78/view/conversation-view.fxml"));
        Parent loginView = loader.load();
        ConversationController conversationController = loader.getController();
        conversationController.setApp(this, user);
        Scene loginScene = new Scene(loginView);
        stage.setScene(loginScene);
        stage.show();

    }


    public void showSucces(String message) {
        // Optional: Use an alert dialog to show error messages
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    public void showError(String message) {
        // Optional: Use an alert dialog to show error messages
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }

}