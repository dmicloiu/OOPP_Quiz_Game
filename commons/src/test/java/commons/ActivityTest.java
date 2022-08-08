package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActivityTest {

    @Test
    public void constructorTest() {
        var act = new Activity("abc", "New activity :)", "www.google.com", "image_path", 5600);
        assertNotNull(act);
    }

    @Test
    public void noDataConstructorTest() {
        var act = new Activity();
        assertNotNull(act);
    }

    @Test
    public void idGetterTest() {
        var act = new Activity("abc", "New activity :)", "www.google.com", "image_path", 5600);
        assertEquals("abc", act.getId());
    }

    @Test
    public void idSetterTest() {
        var act = new Activity("abc", "New activity :)", "www.google.com", "image_path", 5600);
        act.setId("def");
        assertEquals("def", act.getId());
    }

    @Test
    public void titleGetterTest() {
        var act = new Activity("abc", "New activity :)", "www.google.com", "image_path", 5600);
        assertEquals("New activity :)", act.getTitle());
    }

    @Test
    public void titleSetterTest() {
        var act = new Activity("abc", "New activity :)", "www.google.com", "image_path", 5600);
        act.setTitle("Another title");
        assertEquals("Another title", act.getTitle());
    }

    @Test
    public void sourceGetterTest() {
        var act = new Activity("abc", "New activity :)", "www.google.com", "image_path", 5600);
        assertEquals("www.google.com", act.getSource());
    }

    @Test
    public void sourceSetterTest() {
        var act = new Activity("abc", "New activity :)", "www.google.com", "image_path", 5600);
        act.setSource("duckduckgo.com");
        assertEquals("duckduckgo.com", act.getSource());
    }

    @Test
    public void imagePathGetterTest() {
        var act = new Activity("abc", "New activity :)", "www.google.com", "image_path", 5600);
        assertEquals("image_path", act.getImage_path());
    }

    @Test
    public void imagePathSetterTest() {
        var act = new Activity("abc", "New activity :)", "www.google.com", "image_path", 5600);
        act.setImage_path("image_path2");
        assertEquals("image_path2", act.getImage_path());
    }

    @Test
    public void consumptionGetterTest() {
        var act = new Activity("abc", "New activity :)", "www.google.com", "image_path", 5600);
        assertEquals(5600, act.getConsumption_in_wh());
    }

    @Test
    public void consumptionSetterTest() {
        var act = new Activity("abc", "New activity :)", "www.google.com", "image_path", 5600);
        act.setConsumption_in_wh(1200);
        assertEquals(1200, act.getConsumption_in_wh());
    }

    @Test
    public void equalsTestSuccess() {
        var act1 = new Activity("abc", "New activity", "www.google.com", "image_path", 5600);
        var act2 = new Activity("abc", "New activity", "www.google.com", "image_path", 5600);
        assertEquals(act1, act2);
    }

    @Test
    public void equalsTestDifferentIdFailure() {
        var act1 = new Activity("abc", "New activity", "www.google.com", "image_path", 5600);
        var act2 = new Activity("def", "New activity", "www.google.com", "image_path", 5600);
        assertNotEquals(act1, act2);
    }

    @Test
    public void equalsTestDifferentTitleFailure() {
        var act1 = new Activity("abc", "New activity", "www.google.com", "image_path", 5600);
        var act2 = new Activity("abc", "Other activity", "www.google.com", "image_path", 5600);
        assertNotEquals(act1, act2);
    }

    @Test
    public void equalsTestDifferentSourceFailure() {
        var act1 = new Activity("abc", "New activity", "www.google.com", "image_path", 5600);
        var act2 = new Activity("abc", "New activity", "www.duckduckgo.com", "image_path", 5600);
        assertNotEquals(act1, act2);
    }

    @Test
    public void equalsTestDifferentImagePathFailure() {
        var act1 = new Activity("abc", "New activity", "www.google.com", "image_path", 5600);
        var act2 = new Activity("abc", "New activity", "www.google.com",
            "another_image_path", 5600);
        assertNotEquals(act1, act2);
    }

    @Test
    public void equalsTestDifferentCorrAnswerFailure() {
        var act1 = new Activity("abc", "New activity", "www.google.com", "image_path", 5600);
        var act2 = new Activity("abc", "New activity", "www.google.com", "image_path", 6500);
        assertNotEquals(act1, act2);
    }

    @Test
    public void hashcodeSameTest() {
        var act1 = new Activity("abc", "New activity", "www.google.com", "image_path", 5600);
        int hash1 = act1.hashCode();
        var act2 = new Activity("abc", "New activity", "www.google.com", "image_path", 5600);
        int hash2 = act2.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    public void hashcodeDifferentCorrectAnsTest() {
        var act1 = new Activity("abc", "New activity", "www.google.com", "image_path", 5600);
        int hash1 = act1.hashCode();
        var act2 = new Activity("abc", "New activity", "www.google.com", "image_path", 1100);
        int hash2 = act2.hashCode();
        assertNotEquals(hash1, hash2);
    }

    @Test
    public void hashcodeDifferentImagePathTest() {
        var act1 = new Activity("abc", "New activity", "www.google.com", "image_path", 5600);
        int hash1 = act1.hashCode();
        var act2 = new Activity("abc", "New activity", "www.google.com", "another_image", 5600);
        int hash2 = act2.hashCode();
        assertNotEquals(hash1, hash2);
    }

    @Test
    public void hashcodeDifferentSourceTest() {
        var act1 = new Activity("abc", "New activity", "www.google.com", "image_path", 5600);
        int hash1 = act1.hashCode();
        var act2 = new Activity("abc", "New activity", "www.duckduckgo.com", "image_path", 5600);
        int hash2 = act2.hashCode();
        assertNotEquals(hash1, hash2);
    }

    @Test
    public void hashcodeDifferentTitleTest() {
        var act1 = new Activity("abc", "New activity", "www.google.com", "image_path", 5600);
        int hash1 = act1.hashCode();
        var act2 = new Activity("abc", "New activities!", "www.google.com", "image_path", 5600);
        int hash2 = act2.hashCode();
        assertNotEquals(hash1, hash2);
    }

    @Test
    public void hashcodeDifferentIdTest() {
        var act1 = new Activity("abc", "New activity", "www.google.com", "image_path", 5600);
        int hash1 = act1.hashCode();
        var act2 = new Activity("abd", "New activity", "www.google.com", "image_path", 5600);
        int hash2 = act2.hashCode();
        assertNotEquals(hash1, hash2);
    }

    @Test
    public void toStringTest() {
        var act = new Activity("abc", "Title", "www.google.com", "the.image.path", 5600);
        var target = "{\n\"title\": \"Title\",\n" +
            "\"source\": \"www.google.com\",\n" +
            "\"image_path\": \"the.image.path\",\n" +
            "\"consumption_in_wh\": 5600\n}";
        assertEquals(target, act.toString());
    }

    @Test
    public void testGetterActivityID() {
        var act = new Activity("abc", "New activity :)", "www.google.com", "image_path", 5600);
        assertEquals(0, act.getActivityID());
    }

    @Test
    public void testSetterActivityID() {
        var act = new Activity("abc", "New activity :)", "www.google.com", "image_path", 5600);
        act.setActivityID(20);
        assertEquals(20, act.activityID);
    }
}
