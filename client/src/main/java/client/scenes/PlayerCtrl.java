package client.scenes;

import commons.Player;
import commons.Question;

public interface PlayerCtrl {

    /**
     * Gets the player.
     * @return - return the player.
     */
    Player getPlayer();

    /**
     * Sets a player.
     * @param player - the player to be set.
     */
    void setPlayer(Player player);

    /**
     * Gets the current question.
     * @return - the current question.
     */
    Question getCurrentQuestion();

    /**
     * Sets the current question.
     * @param currentQuestion - the question to be set a current.
     */
    void setCurrentQuestion(Question currentQuestion);

    /**
     * Gets the game id.
     * @return - the game id.
     */
    int getGameID();

    /**
     * Sets the game id.
     * @param gameID - the game id to be set.
     */
    void setGameID(int gameID);

    /**
     * Gets the question number.
     * @return - the question number.
     */
    int getQuestionNumber();

    /**
     * Sets the question number.
     * @param questionNumber - The number to be set as the questionumber
     */
    void setQuestionNumber(int questionNumber);

    /**
     * Gets the score of the player.
     * @return - the score of the player.
     */
    int getScore();

    /**
     * Sets the score of the player.
     * @param score - the score to be set as the score of the player.
     */
    void setScore(int score);

    /**
     * Method for navigating to the next question scene.
     */
    void nextQuestion();
}
