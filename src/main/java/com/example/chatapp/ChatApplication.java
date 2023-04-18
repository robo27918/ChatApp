package com.example.chatapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChatApplication extends Application {



   /*** @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("chat-view.fxml"));
        Parent root = loader.load();
        ChatViewController chatController = loader.getController();

        Scene scene = new Scene(root, 500, 456);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Start the message listener
        chatController.startMessageListener();
    }***/
   @Override
   public void start(Stage primaryStage) throws Exception{
       FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
       Parent root = loader.load();
       LoginViewController loginController = loader.getController();
       Scene scene = new Scene(root, 500, 456);
       primaryStage.setScene(scene);
       primaryStage.show();
   }


    public static void main(String[] args) {
        launch();
    }
}