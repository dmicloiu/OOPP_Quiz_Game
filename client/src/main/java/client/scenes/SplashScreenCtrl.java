package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

import java.util.prefs.Preferences;

public class SplashScreenCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField serverIP;
    @FXML
    private Text messageIP;

    /**
     * Constructs and initializes a SplashScreenCtrl
     *
     * @param server - the server utility class
     * @param mainCtrl - the main JavaFX controller
     */
    @Inject
    public SplashScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Initialises the scene by clearing the status message
     */
    public void initialise() {
        this.messageIP.setText("");
    }

    /**
     * Initialises the {@link TextField} with the previous serverIP used
     */
    public void loadPreferences() {
        Preferences preferences = Preferences.userNodeForPackage(SplashScreenCtrl.class);
        this.serverIP.setText(preferences.get("serverIP", ""));
    }

    /**
     * Saves the IP entered by the player
     */
    public void connect() {
        if (serverIP.getText().isBlank()) {
            messageIP.setText("You have to fill in this field");
            return;
        }

        if (!server.testConnection(serverIP.getText())) {
            messageIP.setText("Unable to connect to the server");
            return;
        }
        
        server.setServerIPAndStompSession(serverIP.getText());
        mainCtrl.showMainMenu();
        
        Preferences preferences = Preferences.userNodeForPackage(SplashScreenCtrl.class);
        preferences.put("serverIP", serverIP.getText());
    }

    /**
     * Enables the user to click the only button on the screen with ENTER
     * @param e - the key event
     */
    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                connect();
                break;
            default:
                break;
        }
    }
}
