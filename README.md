# Math Quiz Competition with Queue System

This project implements a **competitive math quiz system** with real-time queue management. Clients must join a virtual queue before participating in a timed math challenge. The system ensures fairness, secure login, and smooth handling of multiple players.

---

## 📌 Features
- **Queue Management:** Players are placed in a waiting queue before the game starts.  
- **Game Start Conditions:** 
  - Starts when at least **5 players** join.  
  - If fewer players, game starts after **60 seconds** with a minimum of **2 players**.  
- **Secure Authentication:** Player credentials are validated against `Players.txt`.  
- **Multithreading Support:** 
  - `PlayerInfoThread` handles login and authentication.  
  - `QueueThread` manages the player queue.  
  - `GameThread` runs the quiz game logic.  
- **Resilience:** If a player disconnects mid-game, the quiz continues.  
- **Leaderboard & Scores:** Players earn points during the quiz, tracked in the server.  

---

## 📂 Project Structure
- `Client.java` → Client-side code to connect and play.  
- `Server.java` → Main server that manages queues and game sessions.  
- `PlayerInfoThread.java` → Handles user authentication and session management.  
- `QueueThread.java` → Manages the player queue logic.  
- `GameThread.java` → Core game logic and communication between players.  
- `Player.java` → Defines player objects with username, password, and score.  
- `Players.txt` → Stores registered players and credentials.  

---

## 🚀 How to Run
1. **Compile all Java files:**
   ```bash
   javac *.java
   ```
2. **Start the server:**
   ```bash
   java Server
   ```
3. **Start a client (repeat for multiple players):**
   ```bash
   java Client
   ```
4. Enter valid credentials from `Players.txt` to join the game.

---

## 🧩 Example Credentials (Players.txt)
```
omar/omar123
hadi/hadi123
fares/fares123
mariem/mariemBest
shlash/shlash123
```

---

## 📖 Notes
- Built as part of **CSC408 Assignment #2**.  
- Demonstrates **socket programming, multithreading, and queue management** in Java.   

