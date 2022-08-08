package commons;

/**
 * Class for sending updates regarding the players in the waiting room
 */
public class PlayerUpdate {

    private Player player;
    private int type;

    /**
     * Constructs an empty PlayerUpdate
     */
    public PlayerUpdate() {}

    /**
     * Constructs a new PlayerUpdate
     *
     * @param player - the player for which there is an update to be processed
     * @param type - if it is = 0, then it should be added in the WR,
     *             if it is = 1, then it should be deleted from the WR
     *             if it is = 2, then the game should start for them
     *             if it is >= 3, then the timer should decrease for the game with gameID = type - 3
     */
    public PlayerUpdate(Player player, int type) {
        this.player = player;
        this.type = type;
    }

    /**
     * Getter for Player
     *
     * @return the value of Player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Getter for addition
     *
     * @return the value of addition
     */
    public int getType() {
        return type;
    }
}
