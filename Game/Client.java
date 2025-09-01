package Game;

import java.net.*;
import java.io.*;

public class Client {

    public static void main(String args[]) {
        // username args[0]
        // password args[1]
        Socket s = null;
        try {
            //credintials
            String message = args[0] + "/" + args[1];
            s = new Socket("localhost", 55550);
            DataInputStream in = new DataInputStream(s.getInputStream());
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            //send credintials
            out.writeUTF(message);
            String d = in.readUTF();
            System.out.println(d);
            boolean done = false;
            //check if it was able to login
            if (d.equals("Login successful")) {
                //create bufferedreader to not block the program when we want to input
                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
                while (!done) {
                    String serverMsg = in.readUTF();
                    System.out.println(serverMsg);
                    //check if game started
                    if (serverMsg.equals("game is starting")) {
                        while (true) {
                            String msg = in.readUTF();
                            System.out.println(msg);
                            //check if question is addressed to the client
                            if (msg.equals("Question for " + args[0])) {
                                String question = in.readUTF();
                                System.out.println("Question: " + question);

                                System.out.print("Your answer: ");
                                //take answer from user
                                String answer = userInput.readLine();

                                out.writeUTF(answer);

                                String result = in.readUTF();
                                System.out.println(result);
                            }
                            //check if the game is over
                            if(msg.equals("Thank you for playing!")) {
                                done = true;
                                break;
                            }
                        }
                    }
                }
            }
            
        } catch (UnknownHostException e) {
            System.out.println("Error Socket:" + e.getMessage());
        } catch (EOFException e) {
            System.out.println("Error EOF:" + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error readline:" + e.getMessage());
            //} catch (InterruptedException e) {
            //  e.printStackTrace();
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (IOException e) {
                    System.out.println("Error close:" + e.getMessage());
                }
            }
        }
    }
}
