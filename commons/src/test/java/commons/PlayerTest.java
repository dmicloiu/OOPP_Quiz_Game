
package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    public void checkNoDataConstructor() {
        var player = new Player();
        assertNotNull(player);
    }

    @Test
    public void checkConstructor() {
        var player = new Player("Diana", 15);
        assertNotNull(player);
    }

    @Test
    public void checkNameGetter() {
        var player = new Player("Diana", 15);
        assertEquals("Diana", player.getName());
    }

    @Test
    public void checkScoreGetter() {
        var player = new Player("Diana", 15);
        assertEquals(15, player.getScore());
    }

    @Test
    public void equalsHashCode() {
        var player1 = new Player("Diana", 15);
        var player2 = new Player("Diana", 15);
        assertEquals(player1, player2);
        assertEquals(player1.hashCode(), player2.hashCode());
    }

    @Test
    public void notEqualsHashCodeScore() {
        var player1 = new Player("Diana", 15);
        var player2 = new Player("Diana", 20);
        assertNotEquals(player1, player2);
        assertNotEquals(player1.hashCode(), player2.hashCode());
    }

    @Test
    public void notEqualsHashCodeName() {
        var player1 = new Player("Diana", 15);
        var player2 = new Player("Tomas", 15);
        assertNotEquals(player1, player2);
        assertNotEquals(player1.hashCode(), player2.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        var player1 = new Player("Diana", 15);
        var player2 = new Player("Tomas", 20);
        assertNotEquals(player1, player2);
        assertNotEquals(player1.hashCode(), player2.hashCode());
    }


    @Test
    public void hasToString() {
        var actual = new Player("Diana", 15).toString();
        String expected =  "{\n\"name\": \"Diana\",\n\"score\": 15\n}";
        assertEquals(expected, actual);
    }

    @Test
    public void equalsTest() {
        var player = new Player("Diana", 15);
        assertTrue(player.equals(player));
    }

    @Test
    public void equalsTestDifferent() {
        var player1 = new Player("Diana", 15);
        var player2 = new Player("Tomas", 20);
        assertFalse(player1.equals(player2));
    }

    @Test
    public void equalsTestDifferentName() {
        var player1 = new Player("Diana", 15);
        var player2 = new Player("Tomas", 15);
        assertFalse(player1.equals(player2));
    }

    @Test
    public void equalsTestDifferentScore() {
        var player1 = new Player("Diana", 15);
        var player2 = new Player("Diana", 20);
        assertFalse(player1.equals(player2));
    }

    @Test
    public void testSetScore() {
        var player = new Player("Diana", 15);
        player.setScore(10);
        assertEquals(10, player.getScore());
    }


}
