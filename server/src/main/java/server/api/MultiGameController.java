package server.api;

import commons.MPGame;
import commons.Player;
import commons.PlayerUpdate;
import commons.Question;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/game")
public class MultiGameController {

    private final QuestionController controller;
    private static final List<MPGame> games = new ArrayList<>();
    private List<Question> nextQuestions;
    private final LongPollingController longController;

    /**
     * Constructor for the MultiGameController
     * @param controller the question controller needed
     */
    public MultiGameController(QuestionController controller,
                               LongPollingController longPollingController) {
        this.controller = controller;
        this.nextQuestions = new ArrayList<>();
        this.longController = longPollingController;
    }

    /**
     * GET api endpoint for getting the next question in a multiplayer game
     * @param gameID the id of the game played
     * @param questionNumber the index of the next question
     * @return the next question to be sent to all the clients
     */
    @GetMapping(path = {"/{gameID}/{questionNumber}"})
    public ResponseEntity<Question> getQuestionGame(@PathVariable int gameID,
                                                    @PathVariable int questionNumber) {
        List<Question> questions = games.get(gameID).getQuestions();
        if (questionNumber >= 20) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(questions.get(questionNumber));
        }
    }

    @GetMapping(path = {"/{gameID}"})
    public ResponseEntity<Integer> getNumberOfPlayers(@PathVariable int gameID) {
        return ResponseEntity.ok(games.get(gameID).getAllPlayers().size());
    }
    /**
     * Method for adding a new MPGame to the list of current multiplayer games
     * The method is used right after one of the players presses the start button
     * @param mpGame the new MPGame added from the WaitingRoomCtrl - Server side
     */
    public void addNewGame(MPGame mpGame) {
        nextQuestions = controller.getAll();
        games.add(new MPGame(mpGame.getAllPlayers(), nextQuestions));
    }

    /**
     * Method for checking of the list of multiplayer games is empty.
     * @return - returns a boolean that declares if the list is empty.
     */
    @GetMapping(path = {"/isEmpty"})
    public ResponseEntity<Boolean> isEmpty() {
        return ResponseEntity.ok(games.isEmpty());
    }

    /**
     * Method for getting a MPGame that corresponds to the given gameID
     * @param gameID the id of the game in the list.
     * @return  - returns a MPGame with the same gameID.
     */
    @GetMapping(path = {"/unique/{gameID}"})
    public ResponseEntity<MPGame> getGameById(@PathVariable int gameID) {
        return ResponseEntity.ok(games.get(gameID));
    }

    /**
     * Adds a player with an updated score to the halfLeaderboard list of the corresponding MPGame.
     * @param gameID the id of the game in the list.
     * @param player the player that needs to be added to the halfLeaderboard list.
     * @return - returns the player that was added.
     */
    @PostMapping(path = { "/add/{gameID}"})
    public ResponseEntity<Player> addScore(@PathVariable int gameID,
                                                        @RequestBody Player player) {
        games.get(gameID).addLeaderBoardEntry(player);
        return ResponseEntity.ok(player);
    }

    /**
     * Adds a player with an updated score to the finalLeaderboard list of the corresponding MPGame.
     * @param gameID the id of the game in the list.
     * @param player the player that needs to be added to the finalLeaderboard list.
     * @return - returns the player that was added.
     */
    @PostMapping(path = {"/update/{gameID}"})
    public ResponseEntity<Player> updateScore(@PathVariable int gameID,
                                                        @RequestBody Player player) {
        games.get(gameID).updateEntry(player);
        return ResponseEntity.ok(player);
    }
    
    /**
     * Updates the time to a half of the current time for the players
     * in the given gameID except for the given player
     *
     * @param gameID - the gameID for which the decrease-time Joker was clicked
     * @param player - the player that clicked the specific Joker
     */
    @PutMapping(path = {"/{gameID}"})
    public void halveTime(@PathVariable int gameID, @RequestBody Player player) {
        longController.listeners.forEach((k, l) -> l.accept(new PlayerUpdate(player, 3 + gameID)));
    }
}
