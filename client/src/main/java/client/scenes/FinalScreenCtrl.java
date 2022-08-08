package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Timer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

public class FinalScreenCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    public static Timer timer;

    @FXML
    private Label endText;

    @Inject
    public FinalScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        timer = new Timer(3, 0);
        timeline.setCycleCount(Timeline.INDEFINITE);
    }
    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.1),
            f -> {
                if (timer.getCurrentTime().equals("0.0")) {
                    showLeaderboard();
                } else {
                    timer.timerDec();
                }
            }));

    /**
     * Stops the timer and shows the final leaderboard
     */
    public void showLeaderboard() {
        timeline.stop();
        if (MainCtrl.lastGamePlayed.equals("multiplayer"))
            mainCtrl.showFinalMultiLeaderboard();
        else MainCtrl.showSingleLeaderboard("game");
        timer.setTime(3, 0);
    }

    /**
     * Sets the text to display the final score fo the player.
     * @param txt - The text that will be displayed.
     */
    public void setEndText(String txt) {
        endText.setText(txt);
    }

    /**
     * Allows the player to use short-cuts like pressing escape to quit the scene.
     * @param e - The key that is being pressed.
     */
    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ESCAPE:
                showLeaderboard();
                break;
            default:
                break;
        }
    }

}
