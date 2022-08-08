package server.api;

import commons.Activity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

public class ActivityControllerTest {

    public int nextInt;
    private MyRandom random;
    private TestActivityRepo repo;

    private ActivityController sut;

    @BeforeEach
    public void setup() {
        random = new MyRandom();
        repo = new TestActivityRepo();
        sut = new ActivityController(random, repo);
    }

    @Test
    public void cannotAddInvalidTitle() {
        Activity activity = new Activity("00-test",
                "Testing activity. This one has two sentences",
                "testing class", "no_image", 100);
        var actual = sut.add(activity);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void cannotAddInvalidTitleQuestion() {
        Activity activity = new Activity("00-test",
                "Testing activity? This one has two sentences",
                "testing class", "no_image", 100);
        var actual = sut.add(activity);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void cannotAddInvalidTitleExclamation() {
        Activity activity = new Activity("00-test",
                "Testing activity! This one has two sentences",
                "testing class", "no_image", 100);
        var actual = sut.add(activity);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void cannotAddNullTitle() {
        Activity activity = new Activity("00-test",
                null, "testing class", "no_image", 100);
        var actual = sut.add(activity);
        assertEquals(BAD_REQUEST, actual.getStatusCode());

    }

    @Test
    public void testAddActivity() {
        Activity activity = new Activity("00-test",
                "Testing activity",
                "testing class", "no_image", 100);
        var actual = sut.add(activity);
        assertEquals(OK, actual.getStatusCode());
        assertEquals(activity, actual.getBody());
    }

    @Test
    public void testAddActivityNull() {
        var actual = sut.add(null);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void testAddActivityInvalidConsumption() {
        Activity activity = new Activity("00-test",
                "Testing activity",
                "testing class", "no_image", -100);
        var actual = sut.add(activity);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void testAddActivityInvalidSource() {
        Activity activity = new Activity("00-test",
                "Testing activity",
                null, "no_image", -100);
        var actual = sut.add(activity);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void testAddActivityInvalidImagePath() {
        Activity activity = new Activity("00-test",
                "Testing activity",
                "testing class", null, -100);
        var actual = sut.add(activity);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void testGetAll() {
        Activity activity = new Activity("00-test",
                "Testing activity",
                "testing class", "no_image", 100);
        sut.add(activity);
        List<Activity> activities = new ArrayList<>();
        activities.add(activity);
        assertEquals(activities, sut.getAll());
    }

    @Test
    public void testDeleteActivities() {
        Activity activity = new Activity("00-test",
                "Testing activity",
                "testing class", "no_image", 100);
        var add = sut.add(activity);
        var actual = sut.deleteActivities();
        assertEquals(OK, actual.getStatusCode());
    }

    @Test
    public void testDeleteById() {
        Activity activity = new Activity("00-test",
                "Testing activity",
                "testing class", "no_image", 100);
        var add = sut.add(activity);
        var actual = sut.deleteById(0);
        assertEquals(OK, actual.getStatusCode());
        assertEquals(activity, actual.getBody());
    }

    @Test
    public void testIsValidTitleMoreCharacters() {
        String title = "Testing activity that has more " +
                "than 55 characters to invalidate it in order to verify the method";
        assertFalse(sut.isValidTitle(title));
    }

    @Test
    public void testIsValidTitleIng() {
        String title = "Test activity";
        assertFalse(sut.isValidTitle(title));
    }

    @Test
    public void testIsValidTitlePunctuation() {
        String title = "Testing activity.";
        assertFalse(sut.isValidTitle(title));

        title = "Testing activity?";
        assertFalse(sut.isValidTitle(title));

        title = "Testing activity!";
        assertFalse(sut.isValidTitle(title));
    }

    @Test
    public void testGetById() {
        Activity activity = new Activity("00-test",
                "Testing activity",
                "testing class", "no_image", 100);
        var add = sut.add(activity);
        var actual = sut.getById(0);
        assertEquals(activity, actual.getBody());
    }

    @Test
    public void testFilterActivities() {
        Activity activity = new Activity("00-test",
                "Testing activity",
                "testing class", "no_image", 100);
        var add = sut.add(activity);
        var actual = sut.filterActivities();
        assertEquals(OK, actual.getStatusCode());
    }

    
    @SuppressWarnings("serial")
    public class MyRandom extends Random {

        public boolean wasCalled = false;

        @Override
        public int nextInt(int bound) {
            wasCalled = true;
            return nextInt;
        }
    }
}
