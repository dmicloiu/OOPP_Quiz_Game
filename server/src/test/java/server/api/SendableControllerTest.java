package server.api;

import commons.Player;
import commons.Sendable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SendableControllerTest {

    private final SendableController sut = new SendableController();
    private Sendable sendable = new Sendable(new Player("Diana", 0), 2);

    @Test
    public void testSendSendable() {
        var actual = sut.sendSendable(sendable);
        assertEquals(sendable, actual);
    }
}
