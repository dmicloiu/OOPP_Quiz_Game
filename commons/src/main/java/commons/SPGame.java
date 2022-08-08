package commons;

import java.util.ArrayList;

public class SPGame extends Game {
    private Player player;
    private int gameCount;

    /**
     * Constructs a new single player game
     *
     * @param username - the username of the player
     */
    public SPGame(String username) {
        player = new Player(username, 0);
        status = "Playing";
        setGameID(gameCount++);
        questions = new ArrayList<>();
    }


    /**
     * Gets the player of this game
     *
     * @return the player of this game
     */
    public Player getPlayer() {
        return player;
    }
}
