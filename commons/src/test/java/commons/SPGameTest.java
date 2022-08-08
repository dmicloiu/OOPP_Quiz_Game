package commons;

import commons.SPGame;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SPGameTest {
    @Test
    public void construct() {
        SPGame t = new SPGame("Pizza");
        assertNotNull(t);
    }

    @Test
    public void getterTest() {
        SPGame t = new SPGame("Pizza");
        assertEquals("Pizza", t.getPlayer().getName());
    }
}