package Game;

import java.net.*;
import java.io.*;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class PlayerInfoThread extends Thread {

    Socket clientSocket;
    DataInputStream in;
    DataOutputStream out;
    LinkedList<player> userQueue;

    public PlayerInfoThread(Socket clientSocket, LinkedList<player> userQueue) {
        try {
            this.clientSocket = clientSocket;
            this.userQueue = userQueue;
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            this.start();
        } catch (IOException e) {
            System.out.println("Error Connection:" + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            //get the user data from the message sent by the user
            String[] data = in.readUTF().split("/");
            String username = data[0];
            String password = data[1];
            boolean userFound = false;
            boolean userOn = false;

            //synchronize the file reading operation so only one thread can access it at time
            synchronized (this) {
                File file = new File("C:\\Users\\omarz\\OneDrive\\Documents\\NetBeansProjects\\DistHW2\\src\\Game\\Players.txt");
                Scanner input = new Scanner(file);
                while (input.hasNext()) {
                    String[] in = input.nextLine().split("/");
                    String user = in[0];
                    String pw = in[1];
                    //check if username exists and the password is correct
                    if (user.equals(username) && pw.equals(password)) {
                        userFound = true;
                        //check if user is not in player queue
                        for (int i = 0; i < userQueue.size(); i++) {
                            if (userQueue.get(i).getUsername().equals(user)) {
                                userOn = true;
                            }
                        }
                        break;
                    }
                }
                input.close();
            }

            //if user is found and not in the player queue tell the client that the login was successful
            if (userFound & !userOn) {
                out.writeUTF("Login successful");
                //add player to player queue (multiple threads will try to access the userQueue object, so synchronization is needed)
                synchronized (userQueue) {
                    userQueue.addLast(new player(username, password, clientSocket));
                    System.out.println(userQueue);
                }
            } else if (userOn) {
                out.writeUTF("Login failed. User already in queue.");
            } else {
                out.writeUTF("Login failed. User not found.");
            }

            //} catch (InterruptedException e) {
            //System.out.println(e.getMessage());
        } catch (EOFException e) {
            System.out.println("Error EOF:" + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error readline:" + e.getMessage());
        } finally {
        }
    }
}
