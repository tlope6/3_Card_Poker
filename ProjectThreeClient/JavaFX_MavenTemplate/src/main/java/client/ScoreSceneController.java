
package client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.List;


public class ScoreSceneController {

    @FXML private Label resultLabel ; //handles the "You win!" or "You lose!"
    @FXML private Label winningsLabel; //shows the amount of money that would win

    //image viewing of the cards
    @FXML private ImageView dealerCard1;
    @FXML private ImageView dealerCard2;
    @FXML private ImageView dealerCard3;

    @FXML private ImageView playerCard1;
    @FXML private ImageView playerCard2;
    @FXML private ImageView playerCard3;

    //buttons
    @FXML private Button playAgainButton;
    @FXML private Button exitButton;


    //callbacks from the client server
    private Runnable onPlayAgain;
    private Runnable onExit;


    //intiializing the actions

    @FXML
    public void initialize() {
        //setting up the buttons
        playAgainButton.setOnAction(e -> {
          if (onPlayAgain != null) {
              onPlayAgain.run();
          }

        });

        exitButton.setOnAction(e -> {
            if(onExit != null) {
                onExit.run();
            }
        });
    }

    //setting up the callbacks
    public void setPlayAgainCallback(Runnable callback) {
        this.onPlayAgain = callback;
    }

    public void setExitCallback(Runnable callback) {
        this.onExit = callback;
    }

    //showing the results of the game on the screen
    public void showResult(String message, int winnings) {
        resultLabel.setText(message);

        //color coding the result text
        resultLabel.getStyleClass().clear();

        if (winnings > 0) {
            resultLabel.getStyleClass().add("win-text");
        } else if (winnings < 0) {
            resultLabel.getStyleClass().add("lose-text");
        } else {
            resultLabel.getStyleClass().add("neutral-text");
        }
    }
}