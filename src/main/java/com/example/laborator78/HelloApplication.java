package com.example.laborator78;

import com.example.laborator78.controller.HelloController;
import com.example.laborator78.domain.Friendship;
import com.example.laborator78.domain.User;
import com.example.laborator78.domain.validators.FriendshipValidator;
import com.example.laborator78.domain.validators.UserValidator;
import com.example.laborator78.repository.Repository;
import com.example.laborator78.repository.database.FriendshipDataBaseRepository;
import com.example.laborator78.repository.database.UserDataBaseRepository;
import com.example.laborator78.service.Network;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
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

        //utilizatorRepository.findAll().forEach(x-> System.out.println(x));
        Network service =new Network(userRepository,friendshipRepository);
        initView(service, stage);

    }

    private void initView(Network service,Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/example/laborator78/view/hello-view.fxml"));
        Scene scene = new Scene(loader.load(), 320, 240);
        primaryStage.setTitle("Hello!");

        HelloController controller = loader.getController();
        controller.setService(service, primaryStage);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}