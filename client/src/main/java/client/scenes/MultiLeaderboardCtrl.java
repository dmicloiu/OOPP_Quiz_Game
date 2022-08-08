package client.scenes;

import client.utils.NameTakenException;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Player;
import commons.Timer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Comparator;
import java.util.stream.Collectors;

public class MultiLeaderboardCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final MultiPlayerCtrl multiPlayerCtrl;

    private static ObservableList<Player> data;

    @FXML
    private TableView<Player> multiBoard;
    @FXML
    private TableColumn<Player, String> name;
    @FXML
    private TableColumn<Player, String> currentPlace;
    @FXML
    private TableColumn<Player, String> highScore;
    @FXML
    private Text half;
    @FXML
    private Button quit;
    @FXML
    private Button play;
    @FXML
    private Text warningText;

    Timer timer;

    /**
     * Timeline for showing the leaderboard and continuing with the game.
     */
    Timeline midTime = new Timeline(new KeyFrame(Duration.seconds(0.1),
        f -> {
            if (timer.getCurrentTime().equals("0.0")) {
                stopTimer();
            } else {
                char endsWith = timer.getCurrentTime().charAt(timer.getCurrentTime().length() - 1);
                switch (endsWith) {
                    case '0':
                    case '1':
                        half.setOpacity(1);
                        break;
                    case '9':
                    case '2':
                        half.setOpacity(0.9);
                        break;
                    case '8':
                    case '3':
                        half.setOpacity(0.7);
                        break;
                    case '7':
                    case '4':
                        half.setOpacity(0.4);
                        break;
                    case '6':
                    case '5':
                        half.setOpacity(0);
                        break;
                    default:
                        break;
                }
                changeTimer();
            }
        }));

    @Inject
    public MultiLeaderboardCtrl(ServerUtils server, MainCtrl mainCtrl,
                                MultiPlayerCtrl multiPlayerCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.multiPlayerCtrl = multiPlayerCtrl;
    }

    /**
     * Initializes the leaderboard sets the timeline Cycle Count.
     */
    public void initialize() {
        name.setCellValueFactory(
            p -> new SimpleStringProperty(p.getValue().getName())
        );
        highScore.setCellValueFactory(
            p -> new SimpleStringProperty("" + p.getValue().getScore())
        );
        currentPlace.setCellValueFactory(
            p -> new SimpleStringProperty("" + place(p.getValue()))
        );
        midTime.setCycleCount(Timeline.INDEFINITE);
    }

    /**
     * Stops the timeline and continues the player to the next scene.
     */
    public void stopTimer() {
        midTime.stop();
        multiPlayerCtrl.loadQuestionType();
    }

    /**
     * Decreases the timer and updates the text to display the correct time.
     */
    public void changeTimer() {
        timer.timerDec();
        half.setText("Game will continue in: " + timer.getCurrentTime() + "s");
    }

    /**
     * Initializes the halfway version of the leaderboard scene.
     */
    public void initializeHalfScene() {
        timer = new Timer(5, 0);
        half.setText("Game will continue in: 5.0s");
        quit.setDisable(true);
        quit.setOpacity(0);
        play.setDisable(true);
        play.setOpacity(0);
        half.setOpacity(1);
        warningText.setVisible(false);
        refreshHalf();
        midTime.play();
    }

    /**
     * Initializes the final version of the leaderboard scene.
     */
    public void initializeFinalScene() {
        quit.setDisable(false);
        warningText.setVisible(false);
        quit.setOpacity(1);
        play.setDisable(false);
        play.setOpacity(1);
        half.setOpacity(0);
        refreshFinal();
    }

    /**
     * Sends the player to the waiting room, in order to play another multiplayer game
     */
    public void play() {
        int gameID;
        try {
            gameID = mainCtrl.mainMenuCtrl.setName(server.getWaitingPlayers(),
                    multiPlayerCtrl.getPlayer().getName());
        } catch (NameTakenException e) {
            warningText.setVisible(true);
            return;
        }

        warningText.setVisible(false);
        mainCtrl.initialiseMultiGame(name.getText(), gameID);
        mainCtrl.showWaitingRoom();
    }
    /**
     * finds a leaderboardEntry in the sorted observableList and with that determines
     * the ranking place that leaderboardEntry corresponds to.
     * @param entry - leaderboardEntry
     * @return the ranking number of the player in the leaderboardEntry
     */
    public int place(Player entry) {
        int place = 0;
        if (data.isEmpty()) {
            return place;
        } else {
            for (int i = 0; i < data.size(); i++) {
                if (entry.getName().equals(data.get(i).getName())) {
                    if (entry.getScore() == data.get(i).getScore()) {
                        place = i + 1;
                        break;
                    }
                }
            }
            return place;
        }
    }

    /**
     * Gets the list of final scores and adds them to the leaderboard.
     */
    public void refreshFinal() {
        var playerPlacementList =
            server.getSpecificGame(multiPlayerCtrl.getGameID()).getLeaderBoard();
        playerPlacementList = playerPlacementList.stream()
                .sorted(Comparator.comparing(Player::getScore).reversed()
                .thenComparing(Player::getName))
                .collect(Collectors.toList());
        data = FXCollections.observableList(playerPlacementList);
        multiBoard.setItems(data);
    }

    /**
     * Gets the list of halfway scores and adds them to the leaderboard.
     */
    public void refreshHalf() {
        var playerPlacementList =
            server.getSpecificGame(multiPlayerCtrl.getGameID()).getHalfLeaderBoard();
        playerPlacementList = playerPlacementList.stream()
            .sorted(Comparator.comparing(Player::getScore).reversed()
                .thenComparing(Player::getName))
            .collect(Collectors.toList());
        data = FXCollections.observableList(playerPlacementList);
        multiBoard.setItems(data);
    }

    public void goBack() {
        mainCtrl.showMainMenu();
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ESCAPE:
                goBack();
                break;
            default:
                break;
        }
    }
}
