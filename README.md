Three Card Poker Game 
A full Java implementation of the Three Card Poker game featuring both a **client-side JavaFX interface** and a **server-side Java engine**. The client and server communicate through **TCP sockets**, enabling real-time gameplay and strict server-side hand evaluation for fairness.

---

Features

Gameplay
- Complete Three Card Poker rules:
  - Ante & Play bets
  - Optional Pair Plus bet
  - Dealer qualification (Queen high or better)
  - Automatic hand ranking and winner determination
- Random shuffling & dealing from a standard 52-card deck
- Server-side validation to prevent cheating

---

Client (JavaFX)
- Built using **Java, JavaFX, FXML, and CSS**
- Clean, interactive UI
- Displays:
  - Player’s cards
  - Dealer’s cards (after reveal)
  - Winnings/losses
  - Bets and error messages
- Sends all actions (bets, play/fold decisions) to the server through sockets

---

Server (Java)
- Pure Java backend running as a **Socket Server**
- Handles:
  - Deck generation & shuffling
  - Dealing cards
  - Evaluating player and dealer hands
  - Applying Three Card Poker rules
  - Sending results and game states back to the client
- Runs independently from the client

---

Communication (Sockets)

The game uses **TCP socket communication**:

- The **server** opens a `ServerSocket` and listens for clients.
- The **client** connects using a `Socket` to the server’s host + port.
- Communication flow:
  - Client sends: bets, actions (play/fold)
  - Server responds: dealt cards, dealer hand, results, payouts


