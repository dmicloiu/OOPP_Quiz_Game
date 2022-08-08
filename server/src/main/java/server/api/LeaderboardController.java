package server.api;

import commons.LeaderboardEntry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.Leaderboard;

import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardController {

    private final Leaderboard leaderboard;

    public LeaderboardController(Leaderboard leaderboard) {
        this.leaderboard = leaderboard;
    }

    /**
     * Gets all entries in the leaderboard
     * 
     * @return a list of all entries in the leaderboard
     */
    @GetMapping(path = { "", "/" })
    public List<LeaderboardEntry> getAll() {
        return leaderboard.findAllByOrderByScoreDescDateAscNameAsc();
    }

    /**
     * Gets an entry by id
     * 
     * @param id the id of the entry
     * @return the entry with the id or null if such an entry does not exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<LeaderboardEntry> getById(@PathVariable("id") long id) {
        return ResponseEntity.ok(leaderboard.findById(id).orElse(null));
    }
    
    /**
     * Gets all entries from the leaderBoard matching a given name.
     * 
     * @param name the value of the 'name' attribute to match
     * @return a list of all entries that match
     */
    @GetMapping("/get/{name}")
    public ResponseEntity<List<LeaderboardEntry>> getByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(leaderboard.findByName(name));
    }

    /**
     * Gets all entries from the leaderBoard matching a given name and score.
     * 
     * @param name the value of the 'name' attribute to match
     * @param score the value of the 'score' attribute to match
     * @return a list of all entries that match
     */
    @GetMapping("/get/{name}/{score}")
    public ResponseEntity<List<LeaderboardEntry>> getByNameAndScore(
            @PathVariable("name") String name,
            @PathVariable("score") int score) {
        return ResponseEntity.ok(leaderboard.findByNameAndScore(name, score));
    }

    @PostMapping(path = { "/add" })
    public ResponseEntity<LeaderboardEntry> add(@RequestBody LeaderboardEntry entry) {
        if (isNullOrEmpty(entry.name) || entry.score < 0) {
            return ResponseEntity.badRequest().build();
        }
        LeaderboardEntry saved = leaderboard.save(entry);
        return ResponseEntity.ok(saved);
    }

    /**
     * Removes an entry by id
     * 
     * @param id the id of the entry
     * @return the entry that was removed
     */
    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable("id") long id) {
        var entry = getById(id).getBody();
        leaderboard.deleteById(id);
        return ResponseEntity.ok(entry);
    }
    
    /**
     * Deletes all entries from the leaderBoard that match a given name.
     * 
     * @param name the value of the 'name' attribute in all entries to delete
     * @return a list of all entries that were deleted
     */
    @DeleteMapping("/delete/{name}")
    public ResponseEntity<List<LeaderboardEntry>> deleteByName(@PathVariable("name") String name) {
        List<LeaderboardEntry> entries = Objects.requireNonNull(getByName(name).getBody());
        leaderboard.deleteAll(entries);
        return ResponseEntity.ok(entries);
    }

    /**
     * Deletes all entries from the leaderBoard that match a given name and score.
     * 
     * @param name the value of the 'name' attribute in all entries to delete
     * @param score the value of the 'score' attribute in all entries to delete
     * @return a list of all entries that were deleted
     */
    @DeleteMapping("/delete/{name}/{score}")
    public ResponseEntity<List<LeaderboardEntry>> deleteByNameAndScore(
            @PathVariable("name") String name, 
            @PathVariable("score") int score) {

        List<LeaderboardEntry> entries = Objects.requireNonNull(
                getByNameAndScore(name, score).getBody());
        
        leaderboard.deleteAll(entries);
        return ResponseEntity.ok(entries);
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }


}
