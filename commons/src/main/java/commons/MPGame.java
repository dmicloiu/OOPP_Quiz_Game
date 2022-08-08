package commons;

import java.util.ArrayList;
import java.util.List;
public class MPGame extends Game {
    private final List<Player> players;
    private final List<Question> questions;

    private List<Player> finalLeaderBoard;
    private List<Player> halfLeaderBoard;

    public static int gameCount;


    /**
     * Constructs a new multiplayer game
     */
    public MPGame() {
        players = new ArrayList<>();
        questions = new ArrayList<>();
        status = "Waiting for players";
        setGameID(gameCount++);
        finalLeaderBoard = new ArrayList<>();
        halfLeaderBoard = new ArrayList<>();
    }

    /**
     * Constructs a new multiplayer game with a specific list of players
     * @param players - the list of players
     */
    public MPGame(List<Player> players, List<Question> questions) {
        this.questions = questions;
        this.players = players;
        setGameID(gameCount);
        finalLeaderBoard = new ArrayList<>();
        halfLeaderBoard = new ArrayList<>();
    }



    public List<Question> getQuestions() {
        return questions;
    }

    /**
     * Removes all players from the game
     */
    public void removeAllPlayers() {
        players.removeAll(players);
    }

    /**
     * Deletes a specific players
     * @param p - the player tp be deleted
     */
    public void deletePlayer(Player p) {
        players.remove(p);
    }

    /**
     * Adds a new player to the game
     * @param p - the new player to be added
     */
    public void addPlayer(Player p) {
        players.add(p);
    }

    /**
     * Adds a new player to the halfLeaderboard list.
     * @param player the player to be added.
     */
    synchronized public void addLeaderBoardEntry(Player player) {
        if (halfLeaderBoard.stream()
                .anyMatch(
                        value -> value.getName()
                                .equals(player.getName()))
        ) return;
        
        halfLeaderBoard.add(player);
    }

    /**
     * Adds a new player to the leaderboard list.
     * @param player the player to be added.
     */
    synchronized public void updateEntry(Player player) {
        if (finalLeaderBoard.stream()
                .anyMatch(
                        value -> value.getName()
                                .equals(player.getName()))
        ) return;
        
        finalLeaderBoard.add(player);
    }

    /**
     * Gets the finalLeaderboard list with its players.
     * @return - returns a list of players.
     */
    public List<Player> getLeaderBoard() {
        return this.finalLeaderBoard;
    }

    /**
     * Gets the halfLeaderboard list with its players.
     * @return - returns a list of players.
     */
    public List<Player> getHalfLeaderBoard() {
        return this.halfLeaderBoard;
    }

    /**
     * Gets all the players in the current game
     * @return the list of players
     */
    public List<Player> getAllPlayers() {
        return players;
    }
}
