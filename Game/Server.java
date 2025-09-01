package Game;

import java.util.LinkedList;
import java.net.*;
import java.io.*;

public class Server {

    public static void main(String[] args) {
        //create the user queue using linked list
        LinkedList<player> userQueue = new LinkedList<player>();
        ServerSocket listenSocket = null;
        try {
            //create server socket
            listenSocket = new ServerSocket(55550);
            //create queue handling thread
            QueueThread QT = new QueueThread(userQueue);
            while (true) {
                //wait for clients to connect
                Socket clientSocket = listenSocket.accept();
                //handle each connection using a thread to validate the credintials
                new PlayerInfoThread(clientSocket, userQueue);
            }
        } catch (IOException e) {
            System.out.println("Error Listen socket:" + e.getMessage());
        }
    }
}
