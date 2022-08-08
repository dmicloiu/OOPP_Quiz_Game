package server.api;

import commons.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.ActivityRepo;

import java.util.*;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class QuestionControllerTest {

    public int nextInt;
    private ActivityController controller;
    private MyRandom random;
    private QuestionActivityRepo repo;

    private QuestionController sut;
    private Activity activity;

    @BeforeEach
    public void setup() {
        List<Activity> activities = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            activities.add(new Activity("01-test",
                    "Testing activity!",
                    "testing class", "no_image", (10 * i + 1)));
        }
        random = new MyRandom();
        repo = new QuestionActivityRepo(activities);
        controller = new ActivityController(random, repo);
        sut = new QuestionController(controller);
        activity = new Activity("00-test",
                "Testing activity!",
                "testing class", "no_image", 100);
    }


    @Test
    public void generateOpenQuestionTest() {
        assertEquals(activity, sut.generateOpenQuestion(activity).getBaseActivity());
    }

    @Test
    public void generateMCQITest() {
        MCQuestion mcQuestion = (MCQuestion) sut.generateMCIntegers(activity);
        assertEquals(Type.INTEGER, mcQuestion.getType());
        assertEquals(activity, mcQuestion.getCorrect());
    }

    @Test
    public void generateMCQIDifferentTest() {
        MCQuestion mcQuestion = (MCQuestion) sut.generateMCIntegers(activity);
        Activity[] activities = mcQuestion.getAnswers();
        if (activity.equals(activities[0])) {
            assertNotEquals(activity, activities[1]);
            assertNotEquals(activity, activities[2]);
        } else if (activity.equals(activities[1])) {
            assertNotEquals(activity, activities[0]);
            assertNotEquals(activity, activities[2]);
        } else {
            assertNotEquals(activity, activities[0]);
            assertNotEquals(activity, activities[1]);
        }
    }

    @Test
    public void generateMCQSTest() {
        MCQuestion mcQuestion = (MCQuestion) sut.generateMCString(activity);
        assertEquals(Type.STRING, mcQuestion.getType());
    }


    @Test
    public void generateMCQSDifferentTest() {
        MCQuestion mcQuestion = (MCQuestion) sut.generateMCString(activity);
        Activity[] activities = mcQuestion.getAnswers();
        if (activity.equals(activities[0])) {
            assertNotEquals(activities[1], activities[2]);
        } else if (activity.equals(activities[1])) {
            assertNotEquals(activities[0], activities[2]);
        } else {
            assertNotEquals(activities[0], activities[1]);
        }
    }

    @Test
    public void findCorrectActivityTest() {
        List<Activity> activities = new ArrayList<>();
        activities.add(activity);
        activities.add(new Activity("01-test",
                "Testing activity!",
                "testing class", "no_image", 76));
        activities.add(new Activity("02-test",
                "Testing activity!",
                "testing class", "no_image", 22));
        activities.add(new Activity("03-test",
                "Testing activity!",
                "testing class", "no_image", 99));

        assertEquals(activity, sut.findCorrectActivity(activities));
    }

    @Test
    public void generateASMUCHASDifferent() {
        MCQuestion mcQuestion = (MCQuestion) sut.generateAsMuchAs(activity);

        Activity[] activities = mcQuestion.getAnswers();
        assertNotEquals(activities[0], activities[1]);
        assertNotEquals(activities[1], activities[2]);
    }

    @Test
    public void getByIndexTestIndexOutOfBounds() {
        var actual = sut.getByIndex(0, 21);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
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

class QuestionActivityRepo implements ActivityRepo {
    Activity activity = new Activity("00-test",
            "Testing activity!",
            "testing class", "no_image", 100);
    List<Activity> activities;
    int index = 0;

    public QuestionActivityRepo(List<Activity> activities) {
        this.activities = activities;
    }

    @Override
    public Activity getByEnergy(long energy) {
        return activities.get((index++) % activities.size());
    }

    @Override
    public Activity getSimilar(long energy) {
        return activity;
    }

    @Override
    public List<Activity> findAll() {
        return activities;
    }

    @Override
    public List<Activity> findAll(Sort sort) {
        return null;
    }

    @Override
    public List<Activity> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public <S extends Activity> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Activity> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Activity> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Activity> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Activity getOne(Long aLong) {
        return null;
    }

    @Override
    public Activity getById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Activity> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Activity> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public Page<Activity> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Activity> S save(S entity) {
        return null;
    }

    @Override
    public Optional<Activity> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public long count() {
        return activities.size();
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Activity entity) {
        activities.remove(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Activity> entities) {

    }

    @Override
    public void deleteAll() {
        activities = new ArrayList<>();
    }

    @Override
    public <S extends Activity> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Activity> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Activity> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Activity> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Activity, R> R findBy(Example<S> example,
                                            Function<FluentQuery.FetchableFluentQuery<S>,
                                                    R> queryFunction) {
        return null;
    }
}

