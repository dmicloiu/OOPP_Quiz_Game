package server.api;

import commons.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/waitingRoom")
public class WaitingRoomController {

    private final MultiGameController multiGameController;
    public static MPGame waitingRoom = new MPGame();
    private final LongPollingController longPollingController;

    public WaitingRoomController(MultiGameController multiGameController,
                                 LongPollingController longPollingController) {
        this.multiGameController = multiGameController;
        this.longPollingController = longPollingController;
    }

    /**
     * Gets all the players from the waiting room
     * @return a ResponseEntity containing a list of all the players in the waiting room
     */
    @GetMapping(path = { "", "/" })
    public ResponseEntity<List<Player>> getAll() {
        return  ResponseEntity.ok(waitingRoom.getAllPlayers());
    }

    /**
     * Starts the game
     * @return "Game started" if no error occurred
     */
    @GetMapping("/start")
    public ResponseEntity<String> start() {
        List<Player>players = waitingRoom.getAllPlayers();
        multiGameController.addNewGame(waitingRoom);
        for (int i = 0; i < players.size(); i++) {
            Player t = players.get(i);
            longPollingController.listeners.forEach((k, l) -> l.accept(new PlayerUpdate(t, 2)));
        }
        waitingRoom = new MPGame();
        return ResponseEntity.ok("Game started");
    }

    /**
     * POST api endpoint for adding a new player to the waiting room
     * @param name the name of the player
     * @return ResponseEntity with the id of the corresponding GameID
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<Integer> add(@RequestBody String name) {
        if (name.isBlank()) return ResponseEntity.badRequest().build();
        Player player = new Player(name, 0);
        waitingRoom.addPlayer(player);
        longPollingController.listeners.forEach((k, l) -> l.accept(new PlayerUpdate(player, 0)));
        return ResponseEntity.ok(waitingRoom.getGameID());
    }

    /**
     * Delete a player from the waiting room
     *
     * @param player - the player to be deleted
     * @return an OK HTTP response
     */
    @PutMapping(path = {"", "/"})
    public ResponseEntity<Object> deletePlayer(@RequestBody Player player) {
        waitingRoom.deletePlayer(player);
        longPollingController.listeners.forEach((k, l) -> l.accept(new PlayerUpdate(player, 1)));
        return ResponseEntity.ok(null);
    }



    /**
     * Getter for the waiting room instance
     * @return the MPGame representing the waiting room
     */
    public MPGame getWaitingRoom() {
        return waitingRoom;
    }
}
