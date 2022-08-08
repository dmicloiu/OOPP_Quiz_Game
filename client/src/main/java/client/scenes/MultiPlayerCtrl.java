package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.*;
import javafx.application.Platform;

public class MultiPlayerCtrl implements PlayerCtrl {

    protected final ServerUtils server;
    private final MCQuestionCtrl mcQuestionCtrl;
    private final OQCtrl oqCtrl;

    private Player player;
    private Question currentQuestion;
    
    private int gameID;
    private int questionNumber;

    @Inject
    public MultiPlayerCtrl(ServerUtils server,
                           MCQuestionCtrl mcQuestionCtrl, OQCtrl oqCtrl) {
        this.server = server;
        this.mcQuestionCtrl = mcQuestionCtrl;
        this.oqCtrl = oqCtrl;
    }
    
    /**
     * This method is called from the MainCtrl to initialise a new mp game
     */
    public void initialise(String name, int gameID) {
        this.player = new Player(name, 0);
        this.gameID = gameID;
        this.questionNumber = 0;
        mcQuestionCtrl.enableEmojisForMultiplayer();
        oqCtrl.enableEmojisForMultiplayer();
        server.registerForSendables("/topic/sendables/" + this.getGameID(), q -> {
            var player = q.getSender();
            if (q.getSendable() <= 5) {
                String stringEmoji = innerEmoji(q);
                oqCtrl.setLatestEmoji(player.getName() + ": " + stringEmoji);
                mcQuestionCtrl.setLatestEmoji(player.getName() + ": " + stringEmoji);
            } else {
                String stringJoker = innerJoker(q);
                oqCtrl.setLatestJoker(player.getName() + stringJoker);
                mcQuestionCtrl.setLatestJoker(player.getName() + ": " + stringJoker);
            }
        });
    }


    public void halveTime(Player p, int gameID) {
        if (gameID == this.gameID && !p.getName().equals(player.getName())) {
            if (currentQuestion instanceof MCQuestion)
                mcQuestionCtrl.halveTime();
            else oqCtrl.halveTime();
        }
    }
    /**
     * Gets the number of players from this game
     * @return
     */
    public int getNumberOfPlayers() {
        return server.getNumberOfPlayers(gameID);
    }

    /**
     * Sends the request to decrease the time to the server
     */
    public void decreaseTime() {
        server.halveTime(gameID, player);
    }

    /**
     * Method for navigating to the next question scene. In addition, it will also set the
     * emoji's for the next scene.
     */
    public void nextQuestion() {
        if (questionNumber == 10) {
            MainCtrl.showHalfMultiLeaderBoard();
        } else if (questionNumber >= 20) {
            server.unsubscribe();
            MainCtrl.showFinalScreen();
        } else {
            loadQuestionType();
        }
    }

    /**
     * Loads everything for the next question and sets the emojis for that scene.
     */
    public void loadQuestionType() {
        currentQuestion = server.getQuestionGame(gameID, questionNumber++);
        if (currentQuestion instanceof OpenQuestion) {
            oqCtrl.setPlayerCtrl(this);
            oqCtrl.setLatestEmoji("");
            oqCtrl.setLatestJoker("");
            Platform.runLater(oqCtrl.startTimer());
            Platform.runLater(MainCtrl.showOpenQuestion());
        } else {
            mcQuestionCtrl.setPlayerCtrl(this);
            mcQuestionCtrl.setLatestEmoji("");
            mcQuestionCtrl.setLatestJoker("");
            mcQuestionCtrl.setChosen(0);
            mcQuestionCtrl.setTimeTaken(10);
            Platform.runLater(mcQuestionCtrl.startTimer());
            Platform.runLater(MainCtrl.showMCQuestion());
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
                stringJoker = " gibberish.";
        }
        return stringJoker;
    }

    /**
     * This method is used whenever switching from one scene to another when
     * questions change. It is invoked whenever a new Sendable object is sent
     * via the websockets and used to generate the string representation of the
     * latest sent emoji that is then displayed on the screen.
     *
     * @param q the emoji sent via the websockets most recently
     *
     * @return a string representation of the Sendable q
     */
    public String innerEmoji(Sendable q) {
        var emoji = q.getSendable();
        var stringEmoji = "";
        switch (emoji) {
            case 1:
                stringEmoji = "\uD83D\uDC4D";
                break;
            case 2:
                stringEmoji = "\u2764";
                break;
            case 3:
                stringEmoji = "\uD83D\uDC80";
                break;
            case 4:
                stringEmoji = "\uD83E\uDD21";
                break;
            case 5:
                stringEmoji = "\uD83D\uDE35";
                break;
            default:
                stringEmoji = "gibberish.";
        }
        return stringEmoji;
    }

    /**
     * Gets a player.
     * @return - return a player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets a player.
     * @param player - the player to be set.
     */
    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Gets the current question.
     * @return - the current question.
     */
    @Override
    public Question getCurrentQuestion() {
        return currentQuestion;
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
        return gameID;
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
    public int getQuestionNumber() {
        return questionNumber;
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
        return this.player.getScore();
    }

    /**
     * Sets the score of the player.
     * @param score - the score to be set as the score of the player.
     */
    @Override
    public void setScore(int score) {
        this.player.setScore(score);
    }

}
