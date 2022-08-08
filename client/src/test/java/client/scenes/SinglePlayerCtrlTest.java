package client.scenes;

import client.utils.ServerUtils;
import commons.Activity;
import commons.OpenQuestion;
import commons.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SinglePlayerCtrlTest {

    private SinglePlayerCtrl single;
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

        single = new SinglePlayerCtrl(server, mc, oq);
    }

    @Test
    public void SinglePlayerCtrl() {
        new SinglePlayerCtrl(new ServerUtils(),
            new MCQuestionCtrl(new IServerUtils(), new MainCtrl()),
            new OQCtrl(new ServerUtils(), new MainCtrl()));
    }

    @Test
    public void getPlayerTest() {
        assertNull(single.getPlayer());
    }

    @Test
    public void setPlayerTest() {
        Player player = new Player("name", 0);
        single.setPlayer(player);
        assertEquals(player, single.getPlayer());
    }

    @Test
    public void getCurrentQuestionTest() {
        assertNull(single.getCurrentQuestion());
    }

    @Test
    public void setCurrentQuestionTest() {
        Activity act = new Activity("id", "title", "source", "image_path",
                                    200);
        OpenQuestion op = new OpenQuestion(act, "prompt");
        single.setCurrentQuestion(op);
        assertEquals(op, single.getCurrentQuestion());
    }

    @Test
    public void getGameIDTest() {
        assertEquals(0, single.getGameID());
    }

    @Test
    public void setGameIDTest() {
        single.setGameID(1);
        assertEquals(1, single.getGameID());
    }

    @Test
    public void getQuestionNumberTest() {
        assertEquals(0, single.getQuestionNumber());
    }

    @Test
    public void setQuestionNumberTest() {
        single.setQuestionNumber(1);
        assertEquals(1, single.getQuestionNumber());
    }

    @Test
    public void ScoreTest() {
        Player player = new Player("name", 200);
        single.setPlayer(player);
        assertEquals(player.getScore(), single.getScore());
    }
}