package client;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientApp extends Application {

    private Stage window;

    private Scene gameplayScene;
    private GamePlayController gamePlayController;

    private Scene scoreScene;
    private ScoreSceneController scoreController;

    private ClientConnection connection;

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.window = primaryStage;

        // Load Gameplay Scene
        FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("/gameplay.fxml"));
        Parent gameRoot = gameLoader.load();
        gamePlayController = gameLoader.getController();
        gameplayScene = new Scene(gameRoot);

        // Load Score Scene
        FXMLLoader scoreLoader = new FXMLLoader(getClass().getResource("/score.fxml"));
        Parent scoreRoot = scoreLoader.load();
        scoreController = scoreLoader.getController();
        scoreScene = new Scene(scoreRoot);

        // Add CSS
        String css = getClass().getResource("/style.css").toExternalForm();
        gameplayScene.getStylesheets().add(css);
        scoreScene.getStylesheets().add(css);

        // Score Scene Buttons
        scoreController.setPlayAgainCallback(() -> switchToGameplay());
        scoreController.setExitCallback(() -> {
            if (connection != null) connection.close();
            Platform.exit();
        });

        // Register gameplay controller with ClientApp
        gamePlayController.setClientApp(this);

        // Show window
        window.setScene(gameplayScene);
        window.setTitle("Three Card Poker Client");
        window.show();

        // Connect to server automatically (optional)
        connectToServer("localhost", 5555);
    }

    // Connect to server
    public boolean connect(String ip, int port) {
        try {
            connectToServer(ip, port);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void connectToServer(String ip, int port) {
        connection = new ClientConnection(ip, port, info ->
                Platform.runLater(() -> handleServerInfo(info))
        );
        connection.start();
    }

    // Called by networking thread
    private void handleServerInfo(PokerInfo info) {

        switch (info.getType()) {

            case DEAL:
                gamePlayController.showPlayerCards(info.getPlayerCards());
                gamePlayController.addLog("Cards Dealt");
                break;

            case RESULT:
                gamePlayController.showDealerCards(info.getDealerCards());

                int winnings = info.getTotalWinnings();
                String msg = (winnings > 0) ? "YOU WIN!" : "YOU LOSE!";

                scoreController.showResult(msg, winnings);
                gamePlayController.showPlayerCards(info.getPlayerCards());
                gamePlayController.showDealerCards(info.getDealerCards());

                switchToScore();
                break;

            case NEW_GAME_READY:
                gamePlayController.resetBets();
                gamePlayController.addLog("New Game Ready");
                break;

            case ERROR:
                gamePlayController.addLog("SERVER ERROR: " + info.getInfo());
                break;
        }
    }

    // Send data TO server
    public void sendToServer(PokerInfo info) {
        if (connection != null) {
            connection.send(info);
        }
    }

    // Switch scenes
    private void switchToScore() {
        window.setScene(scoreScene);
    }

    private void switchToGameplay() {
        gamePlayController.resetBoardForNextRound();
        window.setScene(gameplayScene);
    }

    @Override
    public void stop() throws Exception {
        if (connection != null) {
            connection.close();
        }
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
