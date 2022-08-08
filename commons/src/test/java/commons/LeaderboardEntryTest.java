package commons;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class LeaderboardEntryTest {

    @Test
    public void noDataConstructorTest() {
        var le = new LeaderboardEntry();
        assertNotNull(le);
        assertNotNull(le.getDate());
    }

    @Test
    public void constructorTest() {
        var le = new LeaderboardEntry("John Doe", 602);
        assertNotNull(le);
        assertNotNull(le.getName());
        assertNotNull(le.getDate());
    }

    @Test
    public void nameGetterTest() {
        var le = new LeaderboardEntry("John Doe", 602);
        assertEquals("John Doe", le.getName());
    }

    @Test
    public void nameSetterTest() {
        var le = new LeaderboardEntry("John Doe", 602);
        le.setName("Jane Doe");
        assertEquals("Jane Doe", le.getName());
    }

    @Test
    public void scoreGetterTest() {
        var le = new LeaderboardEntry("John Doe", 602);
        assertEquals(602, le.getScore());
    }

    @Test
    public void scoreSetterTest() {
        var le = new LeaderboardEntry("John Doe", 602);
        le.setScore(603);
        assertEquals(603, le.getScore());
    }

    @Test
    public void dateGetterTest() {
        var le = new LeaderboardEntry("John Doe", 602);
        var target = new Date(System.currentTimeMillis());
        assertTrue(Math.abs(target.getTime() - le.date.getTime()) < 10000); //within 10 seconds
    }

    @Test
    public void dateSetterTest() throws InterruptedException {
        var le = new LeaderboardEntry("John Doe", 602);
        Thread.sleep(1000);
        var target = new Date(System.currentTimeMillis());
        le.setDate(target);
        assertEquals(target, le.getDate());
    }

    @Test
    public void equalsTestSuccess() {
        var le1 = new LeaderboardEntry("John Doe", 602);
        var le2 = new LeaderboardEntry("John Doe", 602);
        assertEquals(le1, le2);
    }

    @Test
    public void equalsTestDifferentNameFailure() {
        var le1 = new LeaderboardEntry("Jane Doe", 602);
        var le2 = new LeaderboardEntry("John Doe", 602);
        assertNotEquals(le1, le2);
    }

    @Test
    public void equalsTestDifferentScoreFailure() {
        var le1 = new LeaderboardEntry("Jane Doe", 502);
        var le2 = new LeaderboardEntry("Jane Doe", 602);
        assertNotEquals(le1, le2);
    }

    @Test
    public void hashcodeSameTest() {
        var le1 = new LeaderboardEntry("John Doe", 602);
        var hash1 = le1.hashCode();
        var le2 = new LeaderboardEntry("John Doe", 602);
        var hash2 = le2.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    public void hashcodeDifferentNameTest() {
        var le1 = new LeaderboardEntry("John Doe", 602);
        var hash1 = le1.hashCode();
        var le2 = new LeaderboardEntry("Jane Doe", 602);
        var hash2 = le2.hashCode();
        assertNotEquals(hash1, hash2);
    }

    @Test
    public void hashcodeDifferentScoreTest() {
        var le1 = new LeaderboardEntry("John Doe", 602);
        var hash1 = le1.hashCode();
        var le2 = new LeaderboardEntry("John Doe", 206);
        var hash2 = le2.hashCode();
        assertNotEquals(hash1, hash2);
    }

    @Test
    public void toStringTest() {
        var le = new LeaderboardEntry("John Doe", 602);
        String target = "LeaderboardEntry{name='John Doe', score=602, date=" +
            le.getDate() + "}";
        assertEquals(le.toString(), target);
    }

    @Test
    public void testEquals() {
        var le1 = new LeaderboardEntry("John Doe", 602);
        assertEquals(le1, le1);
    }

    @Test
    public void testEqualsDifferentTypes() {
        var le1 = new Player("John Doe", 602);
        var le2 = new LeaderboardEntry("Jane Doe", 602);
        assertNotEquals(le1, le2);
    }

    @Test
    public void testEqualsWithDate() {
        var le1 = new LeaderboardEntry("John Doe", 602);
        var le2 = new LeaderboardEntry("John Doe", 602);
        Date date = new Date(2022, 11, 15, 20, 00);
        le1.setDate(date);
        le2.setDate(date);
        assertEquals(le1, le2);
    }

    @Test
    public void testNotEqualsWithDate() {
        var le1 = new LeaderboardEntry("John Doe", 602);
        var le2 = new LeaderboardEntry("John Doe", 602);
        Date date1 = new Date(2022, 11, 15, 20, 00);
        Date date2 = new Date(2022, 10, 15, 20, 00);
        le1.setDate(date1);
        le2.setDate(date2);
        assertNotEquals(le1, le2);
    }

    @Test
    public void assertNotEqualsNull() {
        var le1 = new LeaderboardEntry("John Doe", 602);
        assertNotEquals(le1, null);
    }


}
