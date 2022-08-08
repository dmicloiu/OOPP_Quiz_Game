package server.api;

import commons.Activity;
import commons.MPGame;
import commons.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.database.ActivityRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WaitingRoomControllerTest {

    WaitingRoomController sut;
    LongPollingController con;
    @BeforeEach
    void setup() {
        List<Activity> activities = new ArrayList<>();
        for (int i = 0; i < 81; i++) {
            activities.add(new Activity("" + i, "title", "source", "img_src", 2000));
        }
        con = new LongPollingController();
        sut = new WaitingRoomController(
                new MultiGameController(
                        new QuestionController(
                                new ActivityController(
                                        new Random(), 
                                        new IActivityRepo(activities)
                                )), con), con);
    }

    @AfterEach
    void after() {
        sut.start();
    }

    @Test
    void getAllEmpty() {
        assertEquals(sut.getAll().getBody(), new ArrayList<>());
    }

    @Test
    void startNewGame() {
        MPGame game = sut.getWaitingRoom();
        sut.start();
        assertEquals(game.getAllPlayers(), new ArrayList<>());
    }

    @Test
    void startNoPlayers() {
        sut.start();
        assertEquals(new ArrayList<>(), sut.getWaitingRoom().getAllPlayers());
    }

    @Test
    void addSinglePlayer() {
        Player player = new Player("Wouter", 0);

        sut.add(player.getName());
        assertEquals(List.of(player), sut.getWaitingRoom().getAllPlayers());
    }

    @Test
    void addMultiplePlayer() {
        Player player = new Player("Wouter", 0);
        Player player2 = new Player("Jeroen", 0);

        sut.add(player.getName());
        sut.add(player2.getName());
        assertEquals(List.of(player, player2), sut.getWaitingRoom().getAllPlayers());
    }

    @Test
    void addSetScoreZero() {
        Player player = new Player("Wouter", 5);

        sut.add(player.getName());
        assertEquals(0, sut.getWaitingRoom().getAllPlayers().get(0).getScore());
    }

    @Test
    void addEmptyName() {
        assertEquals(ResponseEntity.badRequest().build(), sut.add(""));
    }

    @Test
    void addOnlySpaces() {
        assertEquals(ResponseEntity.badRequest().build(), sut.add(""));
    }

    @Test
    void deletePlayer() {
        Player player = new Player("Diana", 0);
        sut.add(player.getName());
        assertEquals(HttpStatus.OK, sut.deletePlayer(player).getStatusCode());
    }

}

class IActivityRepo implements ActivityRepo {
    Activity activity = new Activity("0", "title", "source", "img_src", 2000);
    List<Activity> activities;
    int index = 0;
    
    public IActivityRepo(List<Activity> activities) {
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
        return List.of(activity, activity, activity);
    }

    @Override
    public List<Activity> findAll(Sort sort) {
        return List.of(activity);
    }

    @Override
    public Page<Activity> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Activity> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 1;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Activity entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Activity> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Activity> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Activity> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Activity> findById(Long aLong) {
        return Optional.ofNullable(activities.get((index++) % activities.size()));
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
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
        return activity;
    }

    @Override
    public Activity getById(Long aLong) {
        return activity;
    }

    @Override
    public <S extends Activity> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
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
    public <S extends Activity, R> R findBy(
            Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}