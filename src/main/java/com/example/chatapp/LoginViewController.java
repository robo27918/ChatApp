package com.example.chatapp;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginViewController {

    @FXML
    Button login_button;
    @FXML
    TextField user_tf;
    @FXML
    Label error_label;

    private String username;
    private Parent root;
    @FXML public void onClickLogin (){
        if (!user_tf.getText().equals(""))
        {

            username = user_tf.getText();
            Platform.runLater(()->{

                try {
                    // Create the chat view window
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("chat-view.fxml"));
                    root = fxmlLoader.load();
                    //Controller setup to pass name text field to the chatview
                    ChatViewController chatViewController = fxmlLoader.getController();
                    chatViewController.setUsername(username);
                    Stage stage = new Stage();

                    Scene scene = null;
                    scene = new Scene(root, 500, 456);

                    stage.setScene(scene);
                    stage.show();
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });
        }
        else{
            error_label.setText("Username field must not be blank!");
        }
    }

}
