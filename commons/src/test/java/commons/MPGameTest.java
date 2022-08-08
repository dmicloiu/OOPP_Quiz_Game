package commons;

import commons.MPGame;
import commons.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class MPGameTest {
    MPGame g;
    List<Player> players;
    Player d;

    @BeforeEach
    public void init() {
        g = new MPGame();
        players = new ArrayList<>();
        d = new Player("David", 0);
        players.add(d);
    }

    @Test
    public void construct() {
        assertNotNull(g);
    }

    @Test
    public void removeAllPlayers() {
        g.removeAllPlayers();
        assertEquals(new ArrayList<>(), g.getAllPlayers());
    }

    @Test
    public void addPlayerTest() {
        g.addPlayer(new Player("David", 0));
        assertTrue(Objects.equals(players, g.getAllPlayers()));
    }

    @Test
    public void deletePlayerTest() {
        g.deletePlayer(d);
        assertEquals(new ArrayList<>(), g.getAllPlayers());
    }

    @Test
    public void getQuestionsTest() {
        var players = new ArrayList<Player>();
        var questions = new ArrayList<Question>();
        var game = new MPGame(players, questions);
        assertEquals(new ArrayList<Question>(), game.getQuestions());
        questions.add(new MCQuestion());
        var newGame = new MPGame(players, questions);
        assertEquals(1, newGame.getQuestions().size());
        assertEquals(newGame.getQuestions(), questions);
    }

    @Test
    public void getPlayerTest() {
        assertNotNull(g.getAllPlayers());
    }

    @Test
    public void getLeaderBoardTest() {
        assertNotNull(g.getLeaderBoard());
    }

    @Test
    public void getHalfLeaderBoardTest() {
        assertNotNull(g.getHalfLeaderBoard());
    }

    @Test
    public void addLeaderBoardEntryTest() {
        Player player = new Player("name", 200);
        g.addLeaderBoardEntry(player);
        g.addLeaderBoardEntry(new Player("name", 4));
        List<Player> test = new ArrayList<>();
        test.add(player);
        assertEquals(1, g.getHalfLeaderBoard().size());
        assertEquals(test, g.getHalfLeaderBoard());
    }

    @Test
    public void updateEntryTest() {
        Player player = new Player("name", 200);
        g.updateEntry(player);
        g.updateEntry(new Player("name", 4));
        List<Player> test = new ArrayList<>();
        test.add(player);
        assertEquals(1, g.getLeaderBoard().size());
        assertEquals(test, g.getLeaderBoard());
    }

    @Test
    public void testGetterQuestions() {
        Activity activity = new Activity("Test", "New activity",
                "www.google.com", "image_path", 1000);
        Question question = new OpenQuestion(activity, "Prompt");
        List<Question> questions = new ArrayList<>();
        questions.add(question);
        MPGame mpGame = new MPGame(players, questions);
        assertNotNull(mpGame);
        assertEquals(questions, mpGame.getQuestions());
    }
}
