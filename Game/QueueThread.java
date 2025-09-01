package Game;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QueueThread extends Thread {

    long timer;
    LinkedList<player> userQueue;

    public QueueThread(LinkedList<player> userQueue) {
        this.timer = System.currentTimeMillis();
        this.userQueue = userQueue;
        this.start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                //count how many seconds have passed since the timer was last reset
                int secs = (int) ((System.currentTimeMillis() - this.timer) / 1000);
                //other threads will try to access the userQueue object, so synchronize on it
                synchronized (userQueue) {
                    Thread.sleep(100);
                    //send how many seconds have passed to all clients in the queue
                    for (int i = 0; i < this.userQueue.size(); i++) {
                        DataOutputStream out = new DataOutputStream(userQueue.get(i).getCurrentSocket().getOutputStream());
                        out.writeUTF(String.valueOf(secs));
                    }
                    //check if there are atleast five players inside the queue, if so start a game immediatly and reset the timer
                    if (this.userQueue.size() >= 5) {
                        LinkedList<player> gameQueue = new LinkedList<player>();
                        for (int i = 0; i < 5; i++) {
                            //create a game queue with the first five players in the queue
                            gameQueue.addLast(userQueue.removeFirst());
                        }
                        //create a game thread using the first five players in the queue
                        new GameThread(gameQueue);
                        //reset the timer
                        this.timer = System.currentTimeMillis();
                        //if there are atleast two players and 60 seconds passed without a game starting, start a game, with as many players as possible
                    } else if (secs == 60 & 2 <= this.userQueue.size()) {
                        LinkedList<player> gameQueue = new LinkedList<player>();
                        int size = this.userQueue.size();
                        for (int i = 0; i < size; i++) {
                            gameQueue.addLast(userQueue.removeFirst());
                        }
                        new GameThread(gameQueue);
                        this.timer = System.currentTimeMillis();
                      // if 60 seconds passed without a game starting reset the timer
                    } else if (secs == 60 & 2 > this.userQueue.size()) {
                        this.timer = System.currentTimeMillis();
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error readline:" + e.getMessage());

        } catch (InterruptedException ex) {
            Logger.getLogger(QueueThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
