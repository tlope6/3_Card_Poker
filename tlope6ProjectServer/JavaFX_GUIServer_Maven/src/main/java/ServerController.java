import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class ServerController {

    @FXML private TextField portField;
    @FXML private Button startButton;
    @FXML private Button stopButton;
    @FXML private ListView<String> logView;

    private PokerServer server;

    @FXML
    public void initialize() {
        stopButton.setDisable(true);
    }

    @FXML
    public void startServer() {
        int port = Integer.parseInt(portField.getText());

        server = new PokerServer(port, msg ->
                Platform.runLater(() -> logView.getItems().add(msg))
        );

        server.startServer();

        logView.getItems().add("Server starting...");
        startButton.setDisable(true);
        stopButton.setDisable(false);
        portField.setDisable(true);
    }

    @FXML
    public void stopServer() {
        if (server != null) {
            server.stopServer();
        }

        logView.getItems().add("Server stopped.");

        startButton.setDisable(false);
        stopButton.setDisable(true);
        portField.setDisable(false);
    }
}
