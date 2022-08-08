package client.scenes;

import client.utils.NameTakenException;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Player;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.util.List;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

public class MainMenuCtrl {


    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField name;
    @FXML
    private Text warningText;

    @Inject
    public MainMenuCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Initialises the {@link TextField} with the previous name used
     */
    public void loadPreferences() {
        Preferences preferences = Preferences.userNodeForPackage(MainMenuCtrl.class);
        this.name.setText(preferences.get("name", ""));
    }
    
    /**
     * Method called when the player pressed "Multiplayer" button
     * the method initialises the multi game and redirects the user to the waiting room
     */
    public void multiMode() {
        if (!server.testConnection()) {
            showWarning("Unable to connect to server");
            return;
        }
        
        int gameID;
        try {
            gameID = setName(server.getWaitingPlayers(), name.getText());
        } catch (NameTakenException e) {
            showWarning("Name already taken");
            return;
        } catch (IllegalArgumentException e) {
            showWarning("Please enter a name");
            return;
        }
        
        warningText.setVisible(false);
        mainCtrl.initialiseMultiGame(name.getText(), gameID);
        mainCtrl.showWaitingRoom();
    }

    /**
     * Method called when the user pressed the "Back" button
     * It redirects the user to the splash screen
     */
    public void goBack() {
        mainCtrl.showSplash();
    }

    /**
     * Method called when the player pressed "Singleplayer" button
     * the method initialises the single game and shows the user the next question
     */
    public void singleMode() {
        if (name.getText().isBlank()) {
            showWarning("Please enter a name");
            return;
        }

        if (!server.testConnection()) {
            showWarning("Unable to connect to server");
            return;
        }

        warningText.setVisible(false);

        setNamePreference(name.getText());
        mainCtrl.initialiseGame(name.getText());
    }

    /**
     * Method called when the user presses the "Leaderboard" method
     * It redirects the user to the single player global leaderboard
     */
    public void singleLeaderboard() {
        MainCtrl.showSingleLeaderboard("menu");
    }

    /**
     * Redirects to the editActivities scene.
     */
    public void editActivities() {
        mainCtrl.showActivitiesOverview();
    }

    /**
     * Tries to add a new player to the server with the given name.
     *
     * @param players a list of current players in the game
     * @param name the name of the player to add
     * @throws IllegalArgumentException when the name only consists of spaces or is empty
     * @throws NameTakenException when the name is already taken by another player
     */
    protected int setName(List<Player> players, String name)
            throws IllegalArgumentException, NameTakenException {

        if (name.isBlank()) throw new IllegalArgumentException("name may not be empty");

        List<String> playerNames = players.stream()
                .map(Player::getName)
                .collect(Collectors.toList());

        if (playerNames.contains(name)) {
            throw new NameTakenException();
        }

        setNamePreference(name);
        return server.addPlayer(name);
    }

    /**
     * Method for making the warning visible in case of an exception
     * @param warning the text that the warning is supposed to show
     */
    private void showWarning (String warning) {
        warningText.setText(warning);
        warningText.setVisible(true);
    }
    
    private void setNamePreference(String name) {
        Preferences preferences = Preferences.userNodeForPackage(MainMenuCtrl.class);
        preferences.put("name", name);
    }

}
