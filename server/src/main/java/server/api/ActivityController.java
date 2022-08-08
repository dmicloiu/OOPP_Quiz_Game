package server.api;

import commons.Activity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.ActivityRepo;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * The class containing all activity-related endpoints and providing a way of accessing
 * the database table containing the activities.
 */
@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    /**
     * Pseudorandom number generator used to select activities from
     * the database randomly. This means that, most likely, any consecutive games
     * will be almost entirely different from each other.
     */
    private final Random random;
    /**
     * The repository containing the activities to be accessed by this controller.
     */
    private final ActivityRepo repo;

    /**
     * Constructor of a new ActivityController object.
     *
     * @param random the pseudorandom number generator to be
     *               used by this controller
     * @param repo the repository that this controller will access
     */
    public ActivityController(Random random, ActivityRepo repo) {
        this.random = random;
        this.repo = repo;
    }

    /**
     * A get endpoint used to fetch all activities from the database.
     *
     * @return the list of all activities stored in the game's database
     */
    @GetMapping(path = {"", "/"})
    public List<Activity> getAll() {
        return repo.findAll();
    }

    /**
     * A get endpoint that returns a response entity containing
     * an activity with a particular id, specified in the url.
     *
     * @param id the id of the requested activity
     * @return a response entity containing the requested activity
     */
    @GetMapping("/{id}")
    public ResponseEntity<Activity> getById(@PathVariable("id") long id) {
        return ResponseEntity.of(repo.findById(id));
    }

    /**
     * Uses the random field of the class to generate a random id of an activity within the database
     * and fetches it, effectively giving the calling method or class a random activity from
     * the repo.
     *
     * @return a random activity from the activity repository
     */
    @GetMapping("/rnd")
    public ResponseEntity<Activity> getRandom() {
        var idx = random.nextInt((int) repo.count());
        return ResponseEntity.of(repo.findById((long) idx));
    }

    /**
     * A get endpoint that returns a response entity containing
     * an activity with an energy value as close as possible
     * to the one specified in the method call.
     *
     * @param energy the energy value by which to fetch activities
     * @return a response entity containing the activity with the most similar
     * value of the energy parameter to the specified one
     */
    @GetMapping("/energy/{energy}")
    public ResponseEntity<Activity> getByEnergy(@PathVariable("energy") long energy) {
        return ResponseEntity.ok(repo.getByEnergy(energy));
    }


    /**
     * A get endpoint for getting e similar Activity
     *
     * @param energy the energy that the activity has
     * @return an Activity with energy close to the one given
     */
    @GetMapping("/energy/similar/{energy}")
    public ResponseEntity<Activity> getSimilarActivity(@PathVariable("energy") long energy) {
        return ResponseEntity.ok(repo.getSimilar(energy));
    }


    /**
     * A post method adding a specified activity to the database.
     *
     * @param activity the activity to be added to the database
     * @return a response entity confirming that the activity had been posted,
     * or one stating that it had failed because the activity was invalid
     */
    @PostMapping(path = {"/add"})
    public ResponseEntity<Activity> add(@RequestBody Activity activity) {
        if (activity == null ||
                activity.getConsumption_in_wh() < 0 ||
                activity.getImage_path() == null ||
                activity.getTitle() == null ||
                activity.getSource() == null ||
                !isValidTitle(activity.getTitle())) return ResponseEntity.badRequest().build();
        Activity saved = repo.save(activity);
        return ResponseEntity.ok(saved);
    }

    /**
     * Delete method which finds the activity with a specific id and deletes it.
     *
     * @param id the id of the activity to be deleted from the database
     * @return a response entity with a message stating that the deletion had been successful
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Activity> deleteById(@PathVariable("id") long id) {
        var act = getById(id).getBody();
        repo.deleteById(id);
        System.out.println("Activity deleted.");
        return ResponseEntity.ok(act);
    }

    /**
     * This function checks if the title is valid
     * the title is valid if it is formed of just one sentence
     *
     * @param title the title of the Activity that needs to be added to the repo
     * @return true if the activity has a one sentence title, with length <= 65
     * false otherwise
     */
    public boolean isValidTitle(String title) {
        int n = title.length(); // the length of the title string

        if (n >= 55) {
            return false;
        }

        Scanner scanner = new Scanner(title).useDelimiter(" ");
        if (!scanner.next().endsWith("ing")) {
            return false;
        }

        return !title.contains(".") && !title.contains("?") && !title.contains("!");
        //the title of the activity is a single sentence without any punctuation
    }

    /**
     * Delete method for deleting all the activities in the database
     * so there are no duplicates when running the server the next time
     *
     * @return a response entity with a message stating that the deletion had been successful
     */
    @DeleteMapping(path = {"", "/"})
    public ResponseEntity<String> deleteActivities() {
        repo.deleteAll();
        return ResponseEntity.ok("The database containing the activities has been reset!");
    }

    /**
     * DELETE api endpoint used for filtering the activities in the json
     * based on some requirements we decided
     * @return a message telling the user that the activities were filtered
     */
    @DeleteMapping(path = {"/filter"})
    public ResponseEntity<String> filterActivities() {
        List<Activity> activities = repo.findAll();
        HashSet<Long> finalList = new HashSet<>();
        
        for (Activity activity : activities) {
            if (!isValidTitle(activity.getTitle())) {
                repo.delete(activity);
            } else if (activity.getConsumption_in_wh() < 0) {
                repo.delete(activity);
            } else if (activity.getConsumption_in_wh() >= Long.MAX_VALUE) {
                repo.delete(activity);
            } else if (finalList.contains(activity.getConsumption_in_wh())) {
                repo.delete(activity);
            } else {
                finalList.add(activity.getConsumption_in_wh());
            }
        }
        return ResponseEntity.ok("Activities filtered successfully!");
    }

}
