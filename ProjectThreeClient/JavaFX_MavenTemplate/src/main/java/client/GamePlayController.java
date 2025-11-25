package client;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.time.LocalTime;
import java.util.List;

public class GamePlayController {

    // reference back to ClientApp to send messages to the server
    private ClientApp clientApp;

    public void setClientApp(ClientApp app) {
        this.clientApp = app;
    }

    // left betting labels
    @FXML private Label anteLabel;
    @FXML private Label pairPlusLabel;
    @FXML private Label playLabel;
    @FXML private Label totalMoneyLabel;

    // left betting zones
    @FXML private StackPane antePane;
    @FXML private StackPane pairPlusPane;
    @FXML private StackPane playPane;

    // Dealer cards
    @FXML private ImageView dealerCard1;
    @FXML private ImageView dealerCard2;
    @FXML private ImageView dealerCard3;

    // Player cards
    @FXML private ImageView playerCard1;
    @FXML private ImageView playerCard2;
    @FXML private ImageView playerCard3;

    // log output
    @FXML private ListView<String> logView;

    // game state
    private int anteBet = 0;
    private int pairPlusBet = 0;
    private int playBet = 0;
    private boolean cardsDealt = false;

    private final Image CARD_BACK =
            new Image(getClass().getResourceAsStream("/images/card_back.jpeg"));

    @FXML
    public void initialize() {

        // increase bets when clicked
        pairPlusPane.setOnMouseClicked(e -> increasePairPlus());
        antePane.setOnMouseClicked(e -> increaseAnte());

        // PLAY only works after dealing
        playPane.setOnMouseClicked(e -> {
            if (cardsDealt) increasePlay();
        });

        addLog("Client Ready");
    }

    //private betting logic
    private void increaseAnte() {
        anteBet += 5;
        anteLabel.setText("$" + anteBet);
        addLog("Ante raised to $" + anteBet);
    }

    private void increasePairPlus() {
        pairPlusBet += 5;
        pairPlusLabel.setText("$" + pairPlusBet);
        addLog("Pair Plus raised to $" + pairPlusBet);
    }

    private void increasePlay() {
        playBet += 5;
        playLabel.setText("$" + playBet);
        addLog("Play bet raised to $" + playBet);
    }

    //top menu
    @FXML
    private void onFreshStart() {
        resetBets();
        addLog("Fresh Start");
    }

    @FXML
    private void onNewLook() {
        addLog("Theme switching not implemented yet");
    }

    @FXML
    private void onExit() {
        System.exit(0);
    }

    @FXML
    private void scrollLog() {
        logView.scrollTo(logView.getItems().size() - 1);
    }

    //buttons actions

    @FXML
    private void onDeal() {
        addLog("Sending DEAL to server");
        cardsDealt = true;

        // show dealer face-down
        dealerCard1.setImage(CARD_BACK);
        dealerCard2.setImage(CARD_BACK);
        dealerCard3.setImage(CARD_BACK);

        // send DEAL to server
        PokerInfo info = new PokerInfo(MessageType.DEAL);
        info.setAnte(anteBet);
        info.setPairPlus(pairPlusBet);

        clientApp.sendToServer(info);
    }

    @FXML
    private void onPlay() {
        addLog("Sending PLAY to server");
        clientApp.sendToServer(new PokerInfo(MessageType.PLAY));
    }

    @FXML
    private void onFold() {
        addLog("Sending FOLD to server");
        clientApp.sendToServer(new PokerInfo(MessageType.FOLD));
    }

//assistance with logging

    public void addLog(String msg) {
        String time = LocalTime.now().withNano(0).toString();
        logView.getItems().add(time + " " + msg);
        logView.scrollTo(logView.getItems().size() - 1);
    }

//loading the cards with the images

    private Image loadCard(int rank, int suit) {

        // Convert rank to string used in filenames
        String rankName;
        switch (rank) {
            case 11: rankName = "jack"; break;
            case 12: rankName = "queen"; break;
            case 13: rankName = "king"; break;
            case 14: rankName = "ace"; break;
            default: rankName = String.valueOf(rank);
        }

        // Convert suit number to string
        String suitName;
        switch (suit) {
            case 0: suitName = "clubs"; break;
            case 1: suitName = "diamonds"; break;
            case 2: suitName = "hearts"; break;
            case 3: suitName = "spades"; break;
            default: suitName = "clubs";
        }

        // Construct file name
        String file = rankName + "_of_" + suitName + ".jpeg";

        return new Image(getClass().getResourceAsStream("/images/" + file));
    }

    private void fadeInCard(ImageView view, Image img) {
        view.setOpacity(0);
        view.setImage(img);

        FadeTransition ft = new FadeTransition(Duration.millis(300), view);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    // shows the cards for player and dealer

    public void showPlayerCards(List<Card> cards) {
        fadeInCard(playerCard1, loadCard(cards.get(0).getRank(), cards.get(0).getSuit()));
        fadeInCard(playerCard2, loadCard(cards.get(1).getRank(), cards.get(1).getSuit()));
        fadeInCard(playerCard3, loadCard(cards.get(2).getRank(), cards.get(2).getSuit()));
    }

    public void showDealerCards(List<Card> cards) {
        fadeInCard(dealerCard1, loadCard(cards.get(0).getRank(), cards.get(0).getSuit()));
        fadeInCard(dealerCard2, loadCard(cards.get(1).getRank(), cards.get(1).getSuit()));
        fadeInCard(dealerCard3, loadCard(cards.get(2).getRank(), cards.get(2).getSuit()));
    }

    public void showMessage(String msg) {
        addLog(msg);
    }

    //private helper for resetting

    public void resetBets() {
        anteBet = 0;
        pairPlusBet = 0;
        playBet = 0;

        anteLabel.setText("$0");
        pairPlusLabel.setText("$0");
        playLabel.setText("$0");
    }

    public void resetBoardForNextRound() {
        resetBets();

        // clear cards
        playerCard1.setImage(null);
        playerCard2.setImage(null);
        playerCard3.setImage(null);

        dealerCard1.setImage(null);
        dealerCard2.setImage(null);
        dealerCard3.setImage(null);

        cardsDealt = false;
    }
}




