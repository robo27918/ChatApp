package com.example.chatapp;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.Socket;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.time.LocalTime;
public class ChatViewController {
    @FXML
    private Button logout;
    @FXML private ListView<String> message_list;
    @FXML private TextArea message_ta;
    @FXML private Button send;
    private ConcurrentLinkedQueue<String> messageQueue = new ConcurrentLinkedQueue<>();
    //private Client client;
    ObservableList<String> messages_observable_list = FXCollections.observableArrayList();
    Socket socket ;
    Client client;
    private String username;
    private Boolean entered_chat = false;


    // sams ip@ "10.110.246.122"
    public void  initialize() throws IOException {
        // Initialize client class here?
        this.socket =  new Socket ("localhost", 3000);
        this.client = new Client (this.socket, this.username);
        startMessageListener();

    }
    public void startMessageListener() {

        client.listenForMessages(messageQueue);

        Thread uiThread = new Thread(() -> {
            while (true) {
                if (!messageQueue.isEmpty()) {
                    String message = messageQueue.poll();
                    Platform.runLater(() -> message_list.getItems().add(message));
                }
            }
        });
        uiThread.setDaemon(true);
        uiThread.start();
    }
    @FXML public void onClickSend(){

        try{
            // this just updates the view locally but we still need to get this message out to the server
            if (!message_ta.getText().isBlank()) {
                String currTime = getCurrentTime();
                String message_str = "[" + username + "] " + currTime + ": " + message_ta.getText();
                //System.out.println(message_str);
                //messages_observable_list.add(message_str);
                //message_list.setItems(messages_observable_list);
                if (entered_chat) {

                    //System.out.println(message_str);
                    //messages_observable_list.add(message_str);
                    //message_list.setItems(messages_observable_list);
                    client.sendMessage(message_str);

                } else {
                    entered_chat = !entered_chat;
                    client.sendMessage(this.username);
                    client.sendMessage(message_str);
                }
                message_ta.clear();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    @FXML public void onClickLogout() throws IOException {
        String currTime = getCurrentTime();
        client.sendMessage( currTime +": " + "User has said goodbye...");
        client.closeOnExit();
        this.socket.close();
        System.exit(0); // will this close the app?
    }

    //utility method to get the current time for time stamps on messages
    public String getCurrentTime(){
        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String formattedTime = currentTime.format(formatter);
        return formattedTime;
    }
    public void setUsername (String name)
    {
        this.username = name;
    }


}