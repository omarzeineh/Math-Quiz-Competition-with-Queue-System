package Game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameThread extends Thread {

    LinkedList<player> gameQueue;

    public GameThread(LinkedList<player> gameQueue) {
    this.gameQueue = new LinkedList<player>();
    //shuffle the players (non random shuffle)
    synchronized (gameQueue) { 
        LinkedList<player> reordered = new LinkedList<>();
        for (int i = 0; i < gameQueue.size(); i++) {
            player p = gameQueue.get(i);
            if (i % 2 == 0) {
                reordered.addLast(p);
            } else {
                reordered.addFirst(p);
            }
        }
        gameQueue.clear();
        this.gameQueue.addAll(reordered);
    }

    this.start();
}

    @Override
public void run() {
    try {
        //tell clients that the game is starting
        sendToAll("game is starting");

        //play three rounds
        for (int round = 0; round < 3; round++) {
            //ask a player a question each round
            for (player currentPlayer : gameQueue) {
                //announce the to the clients the player that needs to respond
                sendToAll("Question for " + currentPlayer.getUsername());
                
                //generate a random question
                int a = (int)(Math.random()*10+1);
                int b = (int)(Math.random()*10+1);
                int correctAnswer = a+b;

                String question = "What is " + a + " + " + b + " ?";
                DataOutputStream out = new DataOutputStream(currentPlayer.getCurrentSocket().getOutputStream());
                DataInputStream in = new DataInputStream(currentPlayer.getCurrentSocket().getInputStream());

                //send the question
                sendToAll(question);
                currentPlayer.getCurrentSocket().setSoTimeout(10000); // 10 seconds timeout (player turn)

                try {
                    //take the players answer
                    String answerStr = in.readUTF();
                    int userAnswer = Integer.parseInt(answerStr);

                    //check if the answer is correct, if it is correct add a point, if not announce the correct answer
                    if (userAnswer == correctAnswer) {
                        currentPlayer.addPoints(1);
                        sendToAll("Correct, good job");
                    } else {
                        sendToAll("Wrong, The correct answer was: " + correctAnswer);
                    }
                    //send the stat board
                    sendToAll(gameQueue.toString());
                } catch (SocketTimeoutException e) {
                    out.writeUTF("You ran out of time, be faster next time.");
                } catch (NumberFormatException e) {
                    out.writeUTF("Invalid input. Numbers only.");
                }
            }
        }

        sendToAll("Game over. Final Scores:");
        //send the scores of each player to all players
        for (player p : gameQueue) {
            sendToAll(p.getUsername() + ": " + p.getPoints());
        }
        //announce the end of the game
        sendToAll("Thank you for playing!");

    } catch (IOException ex) {
        Logger.getLogger(GameThread.class.getName()).log(Level.SEVERE, null, ex);
    }
}

    public void sendToAll(String s) throws IOException {
        for (int i = 0; i < this.gameQueue.size(); i++) {
            DataOutputStream out = new DataOutputStream(gameQueue.get(i).getCurrentSocket().getOutputStream());
            out.writeUTF(s);
        }
    }

}
