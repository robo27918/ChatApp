package com.example.chatapp;


import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public Client(Socket socket, String username) {


        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.username = username;
        } catch (IOException e) {
            closeAll(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessage(String message) {
        try {

            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();

        } catch (Exception e) {
            closeAll(socket, bufferedReader, bufferedWriter);
        }
    }
    public void sendUsername(String name) throws IOException {
        try {

            bufferedWriter.write(name);
            bufferedWriter.newLine();
            bufferedWriter.flush();

        } catch (Exception e) {
            closeAll(socket, bufferedReader, bufferedWriter);
        }
    }

    public void listenForMessages( ConcurrentLinkedQueue<String> messageQueue) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageFromServer;

                while (socket.isConnected()) {
                    try {
                        messageFromServer = bufferedReader.readLine();
                        messageQueue.add(messageFromServer);
                    } catch (IOException e) {
                        closeAll(socket, bufferedReader, bufferedWriter);
                        e.printStackTrace();

                    }
                }

            }
        }).start();
    }

    public void closeAll(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void closeOnExit(){
        try {
            if (this.bufferedReader != null) {
                this.bufferedReader.close();
            }
            if (this.bufferedWriter != null) {
                this.bufferedWriter.close();
            }
            if (this.socket != null) {
                this.socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
    /***
    public static void main(String []args ) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username: ");
        String username = scanner.nextLine();
        Socket socket = new Socket("localhost",3000);
        Client client = new Client(socket, username);

        client.listenForMessages();
        client.sendMessage();

    }***/

