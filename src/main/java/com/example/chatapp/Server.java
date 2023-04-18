package com.example.chatapp;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    /***
     * Listens for clients to connect
     */
    private ServerSocket serverSocket;
    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
    public void startServer() throws IOException {
        try {
            while(!serverSocket.isClosed()){
                Socket connectionSocket = serverSocket.accept();
                System.out.println("A new client has joined");
                ClientHandler clientHandler = new ClientHandler(connectionSocket);

                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public void closerServerSocket(){
        try{
            if (serverSocket != null){
                serverSocket.close();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    public static void main(String[]args){
        try {
            System.out.println("The server is listening on port 3000....");
            ServerSocket serverSocket = new ServerSocket(3000);
            Server server = new Server(serverSocket);
            server.startServer();

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


}

