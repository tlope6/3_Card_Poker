
package client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;


public class MenuController {
    @FXML
    private TextField ipField;

    @FXML
    private TextField portField;

    @FXML
    private Label statusLabel;


    private ClientApp client; //main networking for the clients


    @FXML
    public void intialize() {
        client = new ClientApp(); //creating the network instance
    }

    //connect button pressed
    @FXML
    public void onConnect(ActionEvent event) {
        String ip = ipField.getText();
        String portText = portField.getText();


        try {
            int port = Integer.parseInt(portText);

            if (ip.isEmpty()) {
                statusLabel.setText("Status: Enter IP");
                return;
            }

            statusLabel.setText("Status: Connecting...");

                //Switch to gameplay scene
            Parent root =  FXMLLoader.load(getClass().getResource("/gameplay.fxml"));
            Stage stage =  (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();

        } catch (Exception e) {
            statusLabel.setText("Status: Invalid IP or Port");
        }
    }

    //Quit button
    @FXML
    public void onQuit(ActionEvent event) {
        System.exit(0);
    }

}