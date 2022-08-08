package server.api;

import commons.Sendable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * This is the server-side controller for the websockets. As
 * the sendables only exist with websockets, only a websocket method
 * is present here.
 */
@Controller
public class SendableController {

    /**
     * Enables back and forth sending of emojis between the clients and server.
     *
     * @param sendable the emoji sent / to be sent
     */
    @MessageMapping("/sendables/{gameId}") // /app/sendables
    @SendTo("/topic/sendables/{gameId}")
    public Sendable sendSendable(Sendable sendable) {
        return sendable; //make this method only send the pair to a particular player's game
    }

}
