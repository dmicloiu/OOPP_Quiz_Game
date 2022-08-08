package commons;


import java.util.ArrayList;
import java.util.List;

public abstract class Game {
    protected int gameID;
    protected String status;
    protected List<Question> questions;

    public Game() {
        questions = new ArrayList<>();
    }


    public List<Question> getQuestions() {
        return questions;
    }

    /**
     * Getter for the id of the game
     * @return the game id
     */
    public int getGameID() {
        return gameID;
    }

    /**
     * Setting the id of the game
     * @param gameID the new game id
     */
    public void setGameID(int gameID) {
        this.gameID = gameID;
    }


}