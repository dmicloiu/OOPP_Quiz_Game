package commons;

import java.util.Objects;

/**
 * This class is a wrapper to be sent to and by the server via websockets.
 * While not strictly necessary, making this class is a more elegant solution
 * than sending and receiving a pair of player and integer.
 */
public class Sendable {

    /**
     * The player who sent this sendable.
     */
    private Player sender;
    /**
     * The number of the sendable that has been sent.
     */
    private int sendable;

    /**
     * The default constructor of this sendable object.
     */
    public Sendable() {}

    /**
     * Constructs a new Emoji object.
     *
     * @param sender the sender of this sendable
     * @param sendable the number of the sendable that has been sent
     */
    public Sendable(Player sender, int sendable) {
        if (sender == null || sender.getName() == null || sender.getName().equals(""))
            throw new IllegalArgumentException("No sender!");
        this.sender = sender;
        if (sendable < 1 || sendable > 8) throw new IllegalArgumentException("No such sendable.");
        this.sendable = sendable;
    }

    /**
     * Returns the sender of this sendable.
     *
     * @return the Player who sent this sendable
     */
    public Player getSender() {
        return sender;
    }

    /**
     * Changes the sender of this sendable to a provided Player.
     *
     * @param sender the new Player sender of this sendable
     */
    public void setSender(Player sender) {
        if (sender == null || sender.getName() == null || sender.getName().equals(""))
            throw new IllegalArgumentException("No sender!");
        this.sender = sender;
    }

    /**
     * Returns the sendable ID.
     *
     * @return the integer ID of this sendable
     */
    public int getSendable() {
        return sendable;
    }

    /**
     * Changes this sendable's ID to a new one (effectively changes the sendable
     * to another one.)
     *
     * @param sendable the new sendable ID
     */
    public void setSendable(int sendable) {
        if (sendable < 1 || sendable > 8) throw new IllegalArgumentException("No such sendable.");
        this.sendable = sendable;
    }

    /**
     * Checks whether this and o are equivalent objects of the same class.
     *
     * @param o the Object to compare this to
     * @return boolean indicating whether this and o are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sendable sendable1 = (Sendable) o;
        return sendable == sendable1.sendable && Objects.equals(sender, sendable1.sender);
    }

    /**
     * Generates the hashcode for this sendable.
     *
     * @return the integer hashcode for this sendable
     */
    @Override
    public int hashCode() {
        return Objects.hash(sender, sendable);
    }

    /**
     * Returns the JSON format string representation of this sendable object.
     *
     * @return the string (in JSON format) representation of this sendable
     */
    public String toString() {
        return "{\n" +
            "\"sender\": " + sender.toString() + ",\n" +
            "\"sendable\": " + sendable + "\n}";
    }
}
