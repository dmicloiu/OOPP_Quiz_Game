package client.scenes;

import client.utils.ServerUtils;
import commons.Activity;
import commons.OpenQuestion;
import commons.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MultiPlayerCtrlTest {

    private MultiPlayerCtrl multi;
    private MCQuestionCtrl mc;
    private OQCtrl oq;

    private ServerUtils server;
    private MainCtrl main;

    @BeforeEach
    public void setup() {
        server = new ServerUtils();
        main = new MainCtrl();
        mc = new MCQuestionCtrl(server, main);
        oq = new OQCtrl(server, main);

        multi = new MultiPlayerCtrl(server, mc, oq);
    }

    @Test
    public void MultiPlayerCtrl() {
        new MultiPlayerCtrl(new ServerUtils(),
            new MCQuestionCtrl(new IServerUtils(), new MainCtrl()),
            new OQCtrl(new ServerUtils(), new MainCtrl()));
    }

    @Test
    public void getPlayerTest() {
        assertNull(multi.getPlayer());
    }

    @Test
    public void setPlayerTest() {
        Player player = new Player("name", 0);
        multi.setPlayer(player);
        assertEquals(player, multi.getPlayer());
    }

    @Test
    public void getCurrentQuestionTest() {
        assertNull(multi.getCurrentQuestion());
    }

    @Test
    public void setCurrentQuestionTest() {
        Activity act = new Activity("id", "title", "source", "image_path",
            200);
        OpenQuestion op = new OpenQuestion(act, "prompt");
        multi.setCurrentQuestion(op);
        assertEquals(op, multi.getCurrentQuestion());
    }

    @Test
    public void getGameIDTest() {
        assertEquals(0, multi.getGameID());
    }

    @Test
    public void setGameIDTest() {
        multi.setGameID(1);
        assertEquals(1, multi.getGameID());
    }

    @Test
    public void getQuestionNumberTest() {
        assertEquals(0, multi.getQuestionNumber());
    }

    @Test
    public void setQuestionNumberTest() {
        multi.setQuestionNumber(1);
        assertEquals(1, multi.getQuestionNumber());
    }

    @Test
    public void ScoreTest() {
        Player player = new Player("name", 200);
        multi.setPlayer(player);
        assertEquals(player.getScore(), multi.getScore());
    }
}