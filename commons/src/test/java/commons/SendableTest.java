package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SendableTest {

    @Test
    public void constructorTestCorrect() {
        var e = new Sendable(new Player("Sander", 0), 1);
        assertNotNull(e);
    }

    @Test
    public void constructorTestWrongSendable() {
        assertThrows(IllegalArgumentException.class, () -> {
            var e = new Sendable(new Player("Sander", 0), -420);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            var e = new Sendable(new Player("Sander", 0), 2137);
        });
    }

    @Test
    public void constructorTestNoSender() {
        assertThrows(IllegalArgumentException.class, () -> {
            var e = new Sendable(null, 4);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            var e = new Sendable(new Player(), 4);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            var e = new Sendable(new Player("", 9), 4);
        });
    }

    @Test
    public void getSendableTest() {
        var e1 = new Sendable(new Player("Sander", 0), 1);
        var e2 = new Sendable(new Player("Alan", 69), 2);
        assertEquals(e1.getSendable(), 1);
        assertEquals(e2.getSendable(), 2);
    }

    @Test
    public void setSendableTest() {
        var e = new Sendable(new Player("Sander", 0), 1);
        e.setSendable(3);
        assertEquals(e.getSendable(), 3);
        e.setSendable(4);
        assertEquals(e.getSendable(), 4);
        assertThrows(IllegalArgumentException.class, () -> {
            e.setSendable(0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            e.setSendable(9);
        });
    }

    @Test
    public void getSenderTest() {
        var e = new Sendable(new Player("Mela", 42), 1);
        assertEquals(e.getSender(), new Player("Mela", 42));
    }

    @Test
    public void setSenderTest() {
        var e = new Sendable(new Player("Mela", 42), 1);
        e.setSender(new Player("Alan", 69));
        assertEquals(new Player("Alan", 69), e.getSender());
        e.setSender(new Player("Sander", 0));
        assertEquals(new Player("Sander", 0), e.getSender());
        assertThrows(IllegalArgumentException.class, () -> {
            e.setSender(null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            e.setSender(new Player());
        });
        assertThrows(IllegalArgumentException.class, () -> {
            e.setSender(new Player("", 7));
        });
    }

    @Test
    public void equalsTest() {
        var e1 = new Sendable(new Player("Mela", 42), 1);
        var e2 = new Sendable(new Player("Mela", 42), 1);
        var e3 = new Sendable(new Player("Sander", 42), 1);
        var e4 = new Sendable(new Player("Mela", 42), 2);
        var e5 = new Sendable(new Player("Mela", 69), 1);
        assertEquals(e1, e2);
        assertNotEquals(e1, e3);
        assertNotEquals(e1, e4);
        assertNotEquals(e1, e5);
    }

    @Test
    public void hashcodeTest() {
        var e1 = new Sendable(new Player("Mela", 42), 1);
        var e2 = new Sendable(new Player("Mela", 42), 1);
        var e3 = new Sendable(new Player("Sander", 42), 1);
        var e4 = new Sendable(new Player("Mela", 42), 2);
        var e5 = new Sendable(new Player("Mela", 69), 1);
        assertEquals(e1.hashCode(), e2.hashCode());
        assertNotEquals(e1.hashCode(), e3.hashCode());
        assertNotEquals(e1.hashCode(), e4.hashCode());
        assertNotEquals(e1.hashCode(), e5.hashCode());
    }

    @Test
    public void toStringTest() {
        var player = new Player("Sander", 0);
        var e = new Sendable(player, 5);
        String compareTo = "{\n" +
            "\"sender\": " + player.toString() + ",\n" +
            "\"sendable\": " + 5 + "\n}";
        assertNotNull(e.toString());
        System.out.println(compareTo);
        assertEquals(e.toString(), compareTo);
    }

    @Test
    public void testEmptyConstructor() {
        var empty = new Sendable();
        assertNotNull(empty);
    }

    @Test
    public void testEquals() {
        var e1 = new Sendable(new Player("Mela", 42), 1);
        assertEquals(e1, e1);
    }

    @Test
    public void testDifferentClasses() {
        var e1 = new Sendable(new Player("Mela", 42), 1);
        var e2 = new Player("Diana", 15);
        assertNotEquals(e1, e2);
    }

    @Test
    public void testEqualsNull() {
        var e1 = new Sendable(new Player("Mela", 42), 1);
        assertNotEquals(e1, null);
    }
}