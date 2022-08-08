package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Player;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;


public class WaitingRoomCtrl {

    public final ServerUtils server;
    private final MainCtrl mainCtrl;
    private static ObservableList<Player> data;

    @FXML
    private TableView<Player> playerTable;
    @FXML
    private TableColumn<Player, String> playerColumn;
    @FXML
    private Text numberOfPlayers;

    @Inject
    public WaitingRoomCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Initializes the table and registers for updates
     */
    public void initializeScene() {
        playerColumn.setCellValueFactory(
                p -> new SimpleStringProperty(p.getValue().getName())
        );
        mainCtrl.startThread();
    }

    /**
     * Updates the waiting room
     * @param b - the type of the received PlayerUpdate
     * @param player - the player for which there is an update
     */
    public void update(int b, Player player) {
        if (b == 0)
            data.add(player);
        else if (b == 1)
            data.remove(player);
        else {
            try {
                data.removeAll();
                if (ServerUtils.player != null) {
                    mainCtrl.startMultiGame();
                    ServerUtils.player = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        refresh();
    }
    /**
     * Starts the game
     */
    public void start() {
        try {
            server.start();
        } catch (Exception e) { }
    }

    /**
     * Refreshes the list of players in the waiting room.
     */
    public void refresh() {
        var players = server.getWaitingPlayers();
        data = FXCollections.observableList(players);
        playerTable.setItems(data);
        numberOfPlayers.setText(players.size() == 1 ?
                "There is currently 1 player waiting for the game to start." :
                "There are currently " + players.size() +
                        " players waiting for the game to start.");
    }

    /**
     * Method called when the user presses the "Back" button
     * Redirects the player to the main menu scene and deletes the player from the waiting room
     */
    public void goBack() {
        server.deletePlayer();
        mainCtrl.showMainMenu();
    }

    /**
     * Allows the player to use short-cuts like pressing escape to quit the scene.
     * @param e - The key that is being pressed.
     */
    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ESCAPE:
                goBack();
                break;
            default:
                break;
        }
    }

    /**
     * Stops the thread responsible for the long polling.
     */
    public void stop() {
        server.stop();
    }
}
