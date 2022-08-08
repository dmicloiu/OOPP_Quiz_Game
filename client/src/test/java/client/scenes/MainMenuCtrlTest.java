package client.scenes;

import client.utils.NameTakenException;
import client.utils.ServerUtils;
import commons.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MainMenuCtrlTest {
    
    private MainMenuCtrl sut;
    
    private IServerUtils serverUtils;
    private MainCtrl mainCtrl;
    
    @BeforeEach
    public void setup() {
        serverUtils = new IServerUtils();
        mainCtrl = new MainCtrl();
        
        sut = new MainMenuCtrl(serverUtils, mainCtrl);
    }
    
    @Test
    public void MainMenuCtrl() {
        new MainMenuCtrl(new ServerUtils(), new MainCtrl());
    }
    
    @Test
    public void setNameEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> {
            List<Player> players = new ArrayList<>();
            sut.setName(players, "");
        });
    }

    @Test
    public void setNameOnlySpaces() {
        assertThrows(IllegalArgumentException.class, () -> {
            List<Player> players = new ArrayList<>();
            sut.setName(players, "   ");
        });
    }

    @Test
    public void setNameTaken() {
        assertThrows(NameTakenException.class, () -> {
            Player player = new Player("Wouter", 0);
            List<Player> players = new ArrayList<>();
            players.add(player);
            sut.setName(players, "Wouter");
        });
    }

    @Test
    public void setNameCount() {
        sut.setName(new ArrayList<>(), "Wouter");
        assertEquals(1, serverUtils.count);
    }

    @Test
    public void setNameContent() {
        sut.setName(new ArrayList<>(), "Wouter");
        
        List<Player> players = new ArrayList<>();
        players.add(new Player("Wouter", 0));
        
        assertEquals(serverUtils.players, players);
    }
    
    @Test
    public void setNameReturn() {
        assertEquals(123, sut.setName(new ArrayList<>(), "Wouter"));
    }
}

class IServerUtils extends ServerUtils {

    public int count = 0;
    public List<Player> players = new ArrayList<>();

    @Override
    public int addPlayer(String playerName) {
        count++;
        Player player = new Player(playerName, 0);
        players.add(player);
        return 123;
    }
}