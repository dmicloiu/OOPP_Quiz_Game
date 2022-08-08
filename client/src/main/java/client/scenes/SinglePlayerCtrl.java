package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.*;
import javafx.application.Platform;

public class SinglePlayerCtrl implements PlayerCtrl {

    private final ServerUtils server;
    private final MCQuestionCtrl mcQuestionCtrl;
    private final OQCtrl oqCtrl;

    private Player singlePlayer;
    private SPGame game;
    private Question currentQuestion;
    private int gameID;


    // currentQuestionEntity from Game to here
    private int questionNumber;

    @Inject
    public SinglePlayerCtrl(ServerUtils server,
                            MCQuestionCtrl mcQuestionCtrl, OQCtrl oqCtrl) {
        this.server = server;
        this.mcQuestionCtrl = mcQuestionCtrl;
        this.oqCtrl = oqCtrl;
    }

    /**
     * This method is called from the MainCtrl to initialise a new sp game
     */
    public void initialise(String name) {
        game = new SPGame(name);
        this.singlePlayer = game.getPlayer();
        this.gameID = game.getGameID();
        this.questionNumber = 0;
        server.registerForSendables("/topic/sendables/" + this.getGameID(), q -> {
            var player = q.getSender();
            String stringJoker = innerJoker(q);
            oqCtrl.setLatestJoker(player.getName() + stringJoker);
            mcQuestionCtrl.setLatestJoker(player.getName() + stringJoker);
        });
        nextQuestion();
    }

    /**
     * Method for navigating to the next question scene
     */
    public void nextQuestion() {
        if (questionNumber >= 20) {
            server.postEntryToLeaderboard(singlePlayer);
            server.unsubscribe();
            MainCtrl.showFinalScreen();
        } else {
            currentQuestion = server.getQuestion(gameID, questionNumber++);
            if (currentQuestion instanceof OpenQuestion) {
                oqCtrl.setPlayerCtrl(this);
                oqCtrl.setScore(singlePlayer.getScore());
                oqCtrl.disableEmojisForSingleplayer();
                oqCtrl.setLatestJoker("");
                Platform.runLater(oqCtrl.startTimer());
                Platform.runLater(MainCtrl.showOpenQuestion());
            } else {
                mcQuestionCtrl.setPlayerCtrl(this);
                mcQuestionCtrl.setChosen(0);
                mcQuestionCtrl.setScore(singlePlayer.getScore());
                mcQuestionCtrl.disableEmojisForSingleplayer();
                mcQuestionCtrl.setLatestJoker("");
                Platform.runLater(mcQuestionCtrl.startTimer());
                Platform.runLater(MainCtrl.showMCQuestion());
            }
        }
    }


    /**
     * This method is used whenever switching from one scene to another when
     * questions change. It is invoked whenever a new Sendable object is sent
     * via the websockets and used to generate the string representation of the
     * latest sent joker that is then displayed on the screen.
     *
     * @param q the joker sent via the websockets most recently
     *
     * @return a string representation of the Sendable q
     */
    public String innerJoker(Sendable q) {
        var joker = q.getSendable();
        var stringJoker = "";
        switch (joker) {
            case 6:
                stringJoker = " used 2x pts!";
                break;
            case 7:
                stringJoker = " used 1/2 time!";
                break;
            case 8:
                stringJoker = " used -wrong ans!";
                break;
            default:
                stringJoker = "gibberish.";
        }
        return stringJoker;
    }


    /**
     * Gets the player.
     * @return - return the player.
     */
    @Override
    public Player getPlayer() {
        return singlePlayer;
    }

    /**
     * Sets a player.
     * @param player - the player to be set.
     */
    @Override
    public void setPlayer(Player player) {
        this.singlePlayer = player;
    }

    /**
     * Gets the current question.
     * @return - the current question.
     */
    @Override
    public Question getCurrentQuestion() {
        return this.currentQuestion;
    }

    /**
     * Sets the current question.
     * @param currentQuestion - the question to be set a current.
     */
    @Override
    public void setCurrentQuestion(Question currentQuestion) {
        this.currentQuestion = currentQuestion;
    }

    /**
     * Gets the game id.
     * @return - the game id.
     */
    @Override
    public int getGameID() {
        return this.gameID;
    }

    /**
     * Sets the game id.
     * @param gameID - the game id to be set.
     */
    @Override
    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    /**
     * Gets the question number.
     * @return - the question number.
     */
    @Override
    public int getQuestionNumber() {
        return this.questionNumber;
    }
    /**
     * Sets the question number.
     * @param questionNumber - The number to be set as the questionumber
     */
    @Override
    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    /**
     * Gets the score of the player.
     * @return - the score of the player.
     */
    @Override
    public int getScore() {
        return this.singlePlayer.getScore();
    }

    /**
     * Sets the score of the player.
     * @param score - the score to be set as the score of the player.
     */
    @Override
    public void setScore(int score) {
        this.singlePlayer.setScore(score);
    }
}
