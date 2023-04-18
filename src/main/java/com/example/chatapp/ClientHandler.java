package com.example.chatapp;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{
    //holds all the clients that are being served by the server
    // belongs to the entire class, not just one particular instance
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader buffReader;
    private BufferedWriter buffWriter;
    private String clientName;
    public ClientHandler(Socket socket){
        try{
            this.socket = socket;
            this.buffWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.buffReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientName = buffReader.readLine();
            //add this  clientHandler to arraylist
            clientHandlers.add(this);
            broadcastMessage("Server: " + clientName +  " has entered the chat");
        }
        catch(Exception e){
            closeAll (socket, buffReader, buffWriter);

        }
    }
    public void removeClientHandler(){
        clientHandlers.remove(this);
        broadcastMessage("Server: " + clientName + " has left the chat");
    }
    public void closeAll(Socket socket, BufferedReader buffReader, BufferedWriter buffWriter){
        removeClientHandler();
        try{
            if(buffReader!=null){
                buffReader.close();
            }
            if (buffWriter!=null){
                buffWriter.close();
            }
            if(socket!=null){
                socket.close();
            }

        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    public void broadcastMessage(String message){
        for (ClientHandler clientHandler: clientHandlers){
            try{


                    clientHandler.buffWriter.write(message);
                    clientHandler.buffWriter.newLine();
                    clientHandler.buffWriter.flush();

            }
            catch(Exception e){
                closeAll(socket,buffReader,buffWriter);
            }
        }
    }
    @Override
    public void run() {
        //listen for messages on new thread since this is a blocking operation
        String messageIn;
        System.out.println("A new client has entered the chat ... printing from run method in Clienthandler...");
        while(socket.isConnected()){
            try{
                messageIn = buffReader.readLine();
                // broadcast to all other clients
                broadcastMessage(messageIn);
            }
            catch(Exception e){
                closeAll(socket,buffReader,buffWriter);
                break;
            }
        }

    }
}