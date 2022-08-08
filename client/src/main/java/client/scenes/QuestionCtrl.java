package client.scenes;

import client.utils.ServerUtils;
import commons.*;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import javafx.util.Duration;

/**
 * Parent class for MCQuestionCtrl and for OQCtrl
 */
public class QuestionCtrl {

    protected final ServerUtils server;
    protected MainCtrl mainCtrl;
    protected PlayerCtrl playerCtrl;

    protected boolean isTransition;
    protected boolean doublePoints;
    public int currentQuestionScore;

    /**
     * The next 3 attributes are true if their corresponding Joker was used
     * or if their Joker don't make sense to be enabled
     */
    public static boolean doublePointsJoker;
    public static boolean shownAnswerJoker;
    public static boolean decreasedTimerJoker;

    @FXML
    protected Button pointsJoker;
    @FXML
    protected Button timeJoker;
    @FXML
    protected Button wrongAnsJoker;
//    @FXML
//    protected Text timeRemaining;
    @FXML
    protected Pane popupField;
    @FXML
    protected Text score;
    @FXML
    protected Text questionNumber;
    @FXML
    protected Text question;
    @FXML
    protected Button emoji1;
    @FXML
    protected Button emoji2;
    @FXML
    protected Button emoji3;
    @FXML
    protected Button emoji4;
    @FXML
    protected Button emoji5;

    @FXML
    protected Text ending;
    @FXML
    protected Text latestEmoji;
    @FXML
    protected Text latestJoker;

    @FXML
    protected Pane timeBar;

    protected Timer timer;
    protected String currentQuestion;
    protected Timer secondTimer;
    protected double timeTaken;
    protected int waitingTime;
    protected int waitingMilliTime;

    /**
     * Constructs and initializes a QuestionCtrl with the given attributes
     *
     * @param server - the server for this game
     * @param mainCtrl - the mainCtrl for this game
     */
    public QuestionCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * This timeline is responsible for counting down the time in between showing the answers
     * and displaying the next question.
     */

    Timeline secondTimeline = new Timeline(new KeyFrame(Duration.seconds(0.1),
            f -> {
                if (secondTimer.getCurrentTime().equals("0.0")) {
                    stopTimer();
                } else {
                    changeRelaxTimer();
                }
            }));

    /**
     * This timeline is responsible for counting down the time in between showing the question
     * and displaying the answer.
     */

    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.1),
            e -> {
                if (timer.getCurrentTime().equals("0.0") && waitingTime == 0
                    && waitingMilliTime == 0) {
                    startSecondTimer();
                    question.setText(currentQuestion);
                    timeJoker.setDisable(true);
                    pointsJoker.setDisable(true);
                    wrongAnsJoker.setDisable(true);
                } else {
                    if (timer.getCurrentTime().equals("0.0")) {
                        timer.setTime(waitingTime, waitingMilliTime);
                        changeTimer();
                        if (this instanceof OQCtrl) ((OQCtrl) this).disable();
                        else ((MCQuestionCtrl) this).disable();
                        currentQuestion = question.getText();
                        question.setText("Wait for the other players");
                        waitingTime = 0;
                        waitingMilliTime = 0;
                    } else changeTimer();
                }
            }));

    /**
     * Gets called when initializing the controller.
     * Sets the initial texts on the screen and link the emoji buttons to a function.
     * Also creates the timer for the questions.
     */
    public void initialize() {
        emoji1.setOnAction(e -> sendEmoji("/app/sendables/" + playerCtrl.getGameID(), 1));
        emoji2.setOnAction(e -> sendEmoji("/app/sendables/" + playerCtrl.getGameID(), 2));
        emoji3.setOnAction(e -> sendEmoji("/app/sendables/" + playerCtrl.getGameID(), 3));
        emoji4.setOnAction(e -> sendEmoji("/app/sendables/" + playerCtrl.getGameID(), 4));
        emoji5.setOnAction(e -> sendEmoji("/app/sendables/" + playerCtrl.getGameID(), 5));
        pointsJoker.setOnAction(j -> sendJoker("/app/sendables/" + playerCtrl.getGameID(), 6));
        timeJoker.setOnAction(j -> sendJoker("/app/sendables/" + playerCtrl.getGameID(), 7));
        wrongAnsJoker.setOnAction(j -> sendJoker("/app/sendables/" + playerCtrl.getGameID(), 8));
        timeline.setCycleCount(Timeline.INDEFINITE);
        secondTimeline.setCycleCount(Timeline.INDEFINITE);
    }

    /**
     * Sets the tine taken for the player to answer
     * @param taken the number of seconds needed
     */
    public void setTimeTaken(double taken) {
        this.timeTaken = taken;
    }

    /**
     * Updating the score of the player when starting the MCQuestion
     * @param score the score of the player
     */
    public void setScore(int score) {
        this.score.setText("Score: " + score);
    }

    /**
     * This method is used to make sure the emojis aren't used by the player
     * in singleplayer mode. This is because singleplayer and multiplayer game
     * IDs won't necessarily work in tandem, so having the client send emojis
     * for singleplayer would not only not make sense, it could also interfere
     * with an ongoing multiplayer game.
     */
    public void disableEmojisForSingleplayer() {
        emoji1.setDisable(true);
        emoji2.setDisable(true);
        emoji3.setDisable(true);
        emoji4.setDisable(true);
        emoji5.setDisable(true);
        emoji1.setVisible(false);
        emoji2.setVisible(false);
        emoji3.setVisible(false);
        emoji4.setVisible(false);
        emoji5.setVisible(false);
        latestEmoji.setVisible(false);
    }

    /**
     * This method is used to make sure that the player can once again
     * see and use emojis in a multiplayer game after having played
     * a singleplayer game.
     */
    public void enableEmojisForMultiplayer() {
        emoji1.setDisable(false);
        emoji2.setDisable(false);
        emoji3.setDisable(false);
        emoji4.setDisable(false);
        emoji5.setDisable(false);
        emoji1.setVisible(true);
        emoji2.setVisible(true);
        emoji3.setVisible(true);
        emoji4.setVisible(true);
        emoji5.setVisible(true);
        latestEmoji.setVisible(true);
    }


    /**
     * Updates the screen prompts for the current question
     */
    public void showQuestion() {
        isTransition = false;
        waitingTime = 0;
        waitingMilliTime = 0;
        doublePoints = false;
        if (playerCtrl.getQuestionNumber() == 1) {
            shownAnswerJoker = false;
            decreasedTimerJoker = false;
            doublePointsJoker = false;
        }
        if (playerCtrl instanceof MultiPlayerCtrl &&
                ((MultiPlayerCtrl) playerCtrl).getNumberOfPlayers() == 1)
            decreasedTimerJoker = true;
        if (playerCtrl instanceof SinglePlayerCtrl) {
            decreasedTimerJoker = true;
        }
        wrongAnsJoker.setDisable(shownAnswerJoker);
        pointsJoker.setDisable(doublePointsJoker);
        timeJoker.setDisable(decreasedTimerJoker);
        questionNumber.setText(playerCtrl.getQuestionNumber() + "/20");
        timeBar.setMaxWidth(300.0);
        score.setText("Score: " + playerCtrl.getScore());
    }

    /**
     * Sends the command to the server to decrease the time for all the players in the current game
     * except for the player that pressed the corresponding Joker
     */
    public void decreaseTime() {
        decreasedTimerJoker = true;
        timeJoker.setDisable(true);
        ((MultiPlayerCtrl) playerCtrl).decreaseTime();
    }

    /**
     * Decreases the time for all the players in the current game except for the player that pressed
     * the corresponding Joker
     */
    public void halveTime() {
        timeline.stop();
        timer.halveTime();
        waitingTime = timer.getTime();
        waitingMilliTime = timer.getMilliSeconds();
        timeline.play();
        timeJoker.setDisable(true);
        halveTimeAnimation();
    }

    /**
     * Stops the first timeline after it is finished and starts the second timeline.
     */
    public void startSecondTimer() {
        timeline.stop();
//        timeRemaining.setText("Time until next question: 5s");
        timeBar.setMaxWidth(300.0);
        secondTimeline.play();
    }

    /**
     * Changes the text at the top of the scene and decreases the timer of the first timeline.
     */
    public void changeTimer() {
        timer.timerDec();
        timeBar.setMaxWidth(300.0 * (Double.parseDouble(timer.getCurrentTime()) / 15.0));
    }

    /**
     * Stop the second timeline and then checks if the current question number is greater than
     * 20. If so end the game if not restart the first timeline and go to the next scene.
     */
    public void stopTimer() {
        hidePopUp();
        secondTimeline.stop();
        playerCtrl.nextQuestion();
    }

    /**
     * Updates the score of the player, so it can be displayed later in the leaderboard.
     */
    public void updateScores() {
        if (!server.isEmpty()) {
            if (server.getSpecificGame(playerCtrl.getGameID()) != null) {
                if (playerCtrl.getQuestionNumber() == 10) {
                    server.addScore(playerCtrl.getGameID(), playerCtrl.getPlayer());
                } else if (playerCtrl.getQuestionNumber() == 20) {
                    server.updateScore(playerCtrl.getGameID(), playerCtrl.getPlayer());
                }
            }
        }
    }

    public void awardPoints() {
        setScore(playerCtrl.getScore() + currentQuestionScore);
        playerCtrl.setScore(playerCtrl.getScore() + currentQuestionScore);

        updateScores();
    }

    /**
     * Setter for the controller to be single player/multiplayer
     * @param ctrl the controller used
     */
    public void setPlayerCtrl(PlayerCtrl ctrl) {
        this.playerCtrl = ctrl;
    }

    /**
     * Doubles the points for a question whenever the corresponding button is pressed
     * (during the 10s when the question is shown or during the next 5s - the corresponding break)
     */
    public void doublePoints() {
        doublePointsJoker = true;
        pointsJoker.setDisable(true);
        doublePoints = true;
    }

    /**
     * Sets the time it took for the player to answer the current question
     */
    public void optionSelected() {
        this.setTimeTaken(15 - Double.parseDouble(timer.getCurrentTime()));
    }

    /**
     * This method is called by every emoji button upon being clicked by the player.
     * The purpose is to send an Emoji via the websockets to the server, which then relays
     * it to every client in that particular game.
     *
     * @param dest the url of the websocket on the serverside
     * @param e the id of the emoji to be sent
     */
    public void sendEmoji(String dest, int e) {
        var emoji = new Sendable(playerCtrl.getPlayer(), e);
        server.send(dest, emoji);
    }

    /**
     * This method is called by every emoji button upon being clicked by the player.
     * The purpose is to send an Emoji via the websockets to the server, which then relays
     * it to every client in that particular game.
     *
     * @param dest the url of the websocket on the serverside
     * @param e the id of the emoji to be sent
     */
    public void sendJoker(String dest, int e) {
        var joker = new Sendable(playerCtrl.getPlayer(), e);
        server.send(dest, joker);
    }


    /**
     * Changes the text at the top of the scene and decreases the timer of the second timeline.
     */
    public void changeRelaxTimer() {
        secondTimer.timerDec();
//        timeRemaining.setText("Time until next question: " + secondTimer.getCurrentTime() + "s");
        timeBar.setMaxWidth(300.0 * (Double.parseDouble(secondTimer.getCurrentTime()) / 5.0));
    }

    /**
     * Allows the first timeline to be started and resets everything to what will be shown in the
     * first round.
     * @return
     */
    public Runnable startTimer() {
        return new Runnable() {
            @Override
            public void run() {
                timer = new Timer(15, 0);
                secondTimer = new Timer(5, 0);
                timeBar.setMaxWidth(300.0);
                timeline.play();
            }
        };
    }

    /**
     * Show a popup that asks the player if he really wants to disconnect. The timer
     * will remain to count down, because otherwise you could pause the game to get a
     * unfair advantage.
     */
    public void showPopUp() {
        trueDisable();
        popupField.setDisable(false);
        popupField.setOpacity(1);
    }

    /**
     * Hides the popup and disables everything in that popup so you cannot accidentily press
     * the buttons.
     */
    public void hidePopUp() {
        popupField.setDisable(true);
        popupField.setOpacity(0);
        enable();
    }

    /**
     * Disables everything on screen to be clickable including the jokers but not the
     * go back button.
     */
    public void trueDisable() {
        emoji1.setDisable(true);
        emoji2.setDisable(true);
        emoji3.setDisable(true);
        emoji4.setDisable(true);
        emoji5.setDisable(true);
    }

    /**
     * Enables everything on screen to be clickable.
     */
    public void enable() {
        emoji1.setDisable(false);
        emoji2.setDisable(false);
        emoji3.setDisable(false);
        emoji4.setDisable(false);
        emoji5.setDisable(false);
    }

    /**
     * Goes back to the splash screen
     */
    public void goBack() {
        hidePopUp();
        timeline.stop();
        secondTimeline.stop();
        mainCtrl.showMainMenu();
        latestJoker.setText("");
        server.unsubscribe();
        enable();
    }

    /**
     * This method is invoked whenever the server sends a new emoji through
     * the websockets. It sets the latestEmoji text field to a new value which
     * reflects the most recent emoji being selected by anyone in the game.
     *
     * @param latest the String representation of the notification of the latest
     *               emoji that had been sent in this scene
     */
    public void setLatestEmoji(String latest) {
        latestEmoji.setText(latest);
    }

    /**
     * This method is invoked whenever the server sends a new joker string through
     * the websockets. It sets the latestJoker text field to a new value which
     * reflects the most recent joker being used by anyone in the game.
     *
     * @param latest the String representation of the notification of the latest
     *               joker that had been sent in this scene
     */
    public void setLatestJoker(String latest) {
        latestJoker.setText(latest);
    }
    
    private void halveTimeAnimation() {
        Timeline rotate = new Timeline();
        rotate.getKeyFrames().add(new KeyFrame(Duration.seconds(1),
                new KeyValue(timeJoker.translateYProperty(), -25),
                new KeyValue(timeJoker.rotateProperty(), 360))
        );
        Timeline down = new Timeline();
        down.getKeyFrames().add(new KeyFrame(Duration.seconds(0.5),
                new KeyValue(timeJoker.translateYProperty(), 0))
        );

        SequentialTransition sequence = new SequentialTransition(rotate, down);

        sequence.setOnFinished(actionEvent -> {
            timeJoker.setTranslateY(0);
            timeJoker.setRotate(0);
        });
        sequence.play();
    }
}

