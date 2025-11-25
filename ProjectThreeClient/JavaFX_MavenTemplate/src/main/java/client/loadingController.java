package client;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;


public class loadingController {
    @FXML private ImageView outlineImage;
    @FXML private ImageView colorImage;
    @FXML private Label loadingLabel;
    @FXML private Timeline dotsTimeline;



    @FXML
    private void initialize(){

        //loading images
        outlineImage.setImage(new Image(getClass().getResource("/images/outline.png").toExternalForm()));
        colorImage.setImage(new Image(getClass().getResource("/images/color.jpg").toExternalForm()));

        startDotAnimation(); //animated dots
        playFadeAnimation(); // fade from outline ->color
    }


    //loading dots animation
    private void startDotAnimation(){
        dotsTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> loadingLabel.setText("Loading")),
                new KeyFrame(Duration.seconds(0.4), e -> loadingLabel.setText("Loading.")),
                new KeyFrame(Duration.seconds(0.8), e -> loadingLabel.setText("Loading..")),
                new KeyFrame(Duration.seconds(1.2), e -> loadingLabel.setText("Loading...")),
                new KeyFrame(Duration.seconds(1.6), e -> loadingLabel.setText("Loading...."))
        );

        dotsTimeline.setCycleCount(Timeline.INDEFINITE);
        dotsTimeline.play();

    }
    //fade animation for the card outlines for the laoding
    private void playFadeAnimation(){
        FadeTransition fadeInColor = new FadeTransition(Duration.seconds(2), colorImage);
        fadeInColor.setFromValue(0);
        fadeInColor.setToValue(1);

        FadeTransition fadeOutOutline = new FadeTransition(Duration.seconds(2), outlineImage);
        fadeOutOutline.setFromValue(1);
        fadeOutOutline.setToValue(0);

        fadeInColor.play();
        fadeOutOutline.play();

        fadeInColor.setOnFinished(e -> finishLoading());

    }

    //switching to the next scene

    private void finishLoading() {

        // stop the loading dots
        if (dotsTimeline != null) dotsTimeline.stop();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/welcome.fxml"));
            Stage stage = (Stage) outlineImage.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}