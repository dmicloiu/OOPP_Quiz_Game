package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TimerTest {

    @Test
    public void constructorTest() {
        Timer t = new Timer(5, 0);
        assertNotNull(t);
    }

    @Test
    void currTimeGetterTest() {
        Timer t = new Timer(5, 0);
        assertEquals("5.0", t.getCurrentTime());
    }

    @Test
    void timeDecreaseTest() {
        Timer t = new Timer(5, 0);
        t.timerDec();
        assertEquals("4.9", t.getCurrentTime());
    }

    @Test
    void timeDecreaseTestMilliseconds() {
        Timer t = new Timer(5, 3);
        t.timerDec();
        assertEquals("5.2", t.getCurrentTime());
    }

    @Test
    void getTimeTest() {
        Timer t = new Timer(5, 0);
        assertEquals(5, t.getTime());
    }

    @Test
    void getMilliSecondsTest() {
        Timer t = new Timer(5, 0);
        assertEquals(0, t.getMilliSeconds());
    }

    @Test
    void timeSetterTest() {
        Timer t = new Timer(5, 0);
        t.setTime(15, 0);
        assertEquals("15.0", t.getCurrentTime());
    }

    @Test
    void halveTimeTest() {
        Timer t = new Timer(8, 4);
        t.halveTime();
        assertEquals((8 / 2), t.getTime());
        assertEquals(( 4 / 2), t.getMilliSeconds());
    }
}
