package Game;

import java.net.Socket;


public class player {
    private String username;
    private String password;
    private Socket CurrentSocket;
    private int points;

    public player(String username, String password, Socket CurrentSocket) {
        this.username = username;
        this.password = password;
        this.CurrentSocket = CurrentSocket;
        this.points = 0;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Socket getCurrentSocket() {
        return CurrentSocket;
    }

    public int getPoints() {
        return points;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCurrentSocket(Socket CurrentSocket) {
        this.CurrentSocket = CurrentSocket;
    }

    public void setPoints(int points) {
        this.points = points;
    }
    
    public void addPoints(int points) {
        this.points = this.points + points;
    }

    @Override
    public String toString() {
        return username + ": " + points;
    }
    
    
}
