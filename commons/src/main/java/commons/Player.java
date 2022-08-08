package commons;

import java.util.Objects;

public class Player {

    private String name;
    private int score;

    public Player() {}

    public Player(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return this.name;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;
        Player player = (Player) o;
        return score == player.score && name.equals(player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, score);
    }

    @Override
    public String toString() {
        return "{\n" +
            "\"name\": " + "\"" + name + "\"" + ",\n" +
            "\"score\": " + score +
            "\n}";
    }
}
