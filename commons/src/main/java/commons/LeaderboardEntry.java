package commons;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
import java.util.Objects;

@Entity
public class LeaderboardEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    public String name;
    public int score;
    public Date date;

    public LeaderboardEntry() {
        this.date = new Date(System.currentTimeMillis());
    }

    public LeaderboardEntry(String name, int score) {
        this.name = name;
        this.score = score;
        this.date = new Date(System.currentTimeMillis());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LeaderboardEntry that = (LeaderboardEntry) o;
        return score == that.score && 
                Objects.equals(name, that.name) &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, score, date);
    }

    @Override
    public String toString() {
        return "LeaderboardEntry{" +
                "name='" + name + '\'' +
                ", score=" + score +
                ", date=" + date +
                '}';
    }
}
