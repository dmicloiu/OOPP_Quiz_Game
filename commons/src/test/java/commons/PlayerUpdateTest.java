package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerUpdateTest {

    public PlayerUpdate ups;

    @BeforeEach
    void init() {
        ups = new PlayerUpdate(new Player("pizza", Integer.MIN_VALUE), 0);
    }

    @Test
    void constructorTest() {
        assertNotNull(ups);
    }

    @Test
    void getPlayer() {
        assertEquals(new Player("pizza", Integer.MIN_VALUE), ups.getPlayer());
    }

    @Test
    void getAddition() {
        assertEquals(ups.getType(), 0);
    }

    @Test
    void constructorEmpty() {
        PlayerUpdate empty = new PlayerUpdate();
        assertNotNull(empty);
    }
}