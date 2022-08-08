package server.api;

import commons.LeaderboardEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.Leaderboard;

import java.util.*;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

public class LeaderboardControllerTest {

    private LeaderboardController sut;

    @BeforeEach
    void setUp() {
        List<LeaderboardEntry> leaderboard = new ArrayList<>();
        leaderboard.add(new LeaderboardEntry("Diana", 12));
        leaderboard.add(new LeaderboardEntry("Mara", 8));
        sut = new LeaderboardController(new LeaderboardTest(leaderboard));
    }

    @Test
    public void testGetAll() {
        var actual = sut.getAll();
        assertNotNull(actual);
    }

    @Test
    public void testGetById() {
        var actual = sut.getById(0);
        assertEquals(OK, actual.getStatusCode());
    }

    @Test
    public void testGetByName() {
        var actual = sut.getByName("Diana");
        assertEquals(OK, actual.getStatusCode());
    }

    @Test
    public void testGetByNameAndScore() {
        var actual = sut.getByNameAndScore("Diana", 12);
        assertEquals(OK, actual.getStatusCode());
    }

    @Test
    public void testAddNullName() {
        var actual = sut.add(new LeaderboardEntry(null, 2));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void testAddEmptyName() {
        var actual = sut.add(new LeaderboardEntry("", 2));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void testAddWrongScore() {
        var actual = sut.add(new LeaderboardEntry("Diana", -100));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void testAdd() {
        var actual = sut.add(new LeaderboardEntry("Diana", 12));
        assertEquals(OK, actual.getStatusCode());
    }

    @Test
    public void testDeleteByID() {
        var actual = sut.deleteById(1);
        assertEquals(OK, actual.getStatusCode());
    }

    @Test
    public void testDeleteByName() {
        var actual = sut.deleteByName("Diana");
        assertEquals(OK, actual.getStatusCode());
    }

    @Test
    public void testDeleteByNameAndScore() {
        var actual = sut.deleteByNameAndScore("Diana", 12);
        assertEquals(OK, actual.getStatusCode());
    }


}

class LeaderboardTest implements Leaderboard {

    LeaderboardEntry entry = new LeaderboardEntry("Diana", 12);
    LeaderboardEntry otherEntry = new LeaderboardEntry("Mara", 8);
    List<LeaderboardEntry> leaderboard;

    public LeaderboardTest(List<LeaderboardEntry> leaderboard) {
        this.leaderboard = leaderboard;
    }

    @Override
    public List<LeaderboardEntry> findAllByOrderByScoreDescDateAscNameAsc() {
        return leaderboard;
    }

    @Override
    public List<LeaderboardEntry> findByName(String name) {
        List<LeaderboardEntry> answers = new ArrayList<>();
        answers.add(entry);
        return answers;
    }

    @Override
    public List<LeaderboardEntry> findByNameAndScore(String name, int score) {
        List<LeaderboardEntry> answers = new ArrayList<>();
        answers.add(entry);
        return answers;
    }

    @Override
    public List<LeaderboardEntry> findAll() {
        return null;
    }

    @Override
    public List<LeaderboardEntry> findAll(Sort sort) {
        return null;
    }

    @Override
    public List<LeaderboardEntry> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public <S extends LeaderboardEntry> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends LeaderboardEntry> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends LeaderboardEntry> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<LeaderboardEntry> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public LeaderboardEntry getOne(Long aLong) {
        return null;
    }

    @Override
    public LeaderboardEntry getById(Long aLong) {
        return otherEntry;
    }

    @Override
    public <S extends LeaderboardEntry> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends LeaderboardEntry> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public Page<LeaderboardEntry> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends LeaderboardEntry> S save(S entity) {
        return null;
    }

    @Override
    public Optional<LeaderboardEntry> findById(Long aLong) {
        return Optional.of(entry);
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(LeaderboardEntry entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends LeaderboardEntry> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends LeaderboardEntry> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends LeaderboardEntry> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends LeaderboardEntry> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends LeaderboardEntry> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends LeaderboardEntry,
            R> R findBy(Example<S> example,
                        Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

}
