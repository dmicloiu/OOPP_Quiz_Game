package server.api;

import commons.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;


@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final ActivityController controller;
    private final HashMap<Integer, List<Question>> singlePlayerGame = new HashMap<>();

    public QuestionController(ActivityController controller) {
        this.controller = controller;
    }


    /**
     * Method for generating all the questions in the game instance
     */
    public List<Question> generateQuestions() {
        List<Question> questions = new ArrayList<>();
        Set<Activity> baseActivities = new HashSet<>();

        // selecting the 20 baseActivities of the questions
        for (int i = 0; i < 20; i++) {
            Activity activity = controller.getRandom().getBody();
            while (baseActivities.contains(activity)) {
                activity = controller.getRandom().getBody();
            }
            baseActivities.add(activity);
        }


        for (Activity activity : baseActivities) {
            Random random = new Random();
            int chooseType = random.nextInt(4);

            switch (chooseType) {
                case 0:
                    questions.add(generateOpenQuestion(activity));
                    break;
                case 1:
                    questions.add(generateMCIntegers(activity));
                    break;
                case 2:
                    questions.add(generateAsMuchAs(activity));
                    break;
                default:
                    questions.add(generateMCString(activity));
                    break;
            }
        }
        return questions;
    }

    /**
     * This method is called by the multiple-choice question generators to ensure
     * that all answers to these questions are unique. In case any repeat, one of them
     * will be rerolled until it is finally distinct.
     *
     * @param correctActivity the correct activity for the given question
     * @param activity1 the first incorrect activity generated automatically
     * @param activity2 the second incorrect activity generated automatically
     *
     * @return a list of answers to the given question
     */
    protected List<Activity> uniqueifyAnswers(Activity correctActivity,
                                    Activity activity1,
                                    Activity activity2) {
        Random random = new Random();
        int control = 0;
        while (correctActivity.equals(activity1)
            || activity1.equals(activity2)) {
            if (control >= 5) {
                while (correctActivity.equals(activity1))
                    activity1 = controller.getRandom().getBody();
            }
            if (control >= 10) break;
            boolean sign = random.nextBoolean();
            long newIncorrect = Utils.innerIncorrect(random,
                correctActivity.getConsumption_in_wh(),
                sign);
            activity1 = controller.getByEnergy(newIncorrect).getBody();
            control++;
        }
        control = 0;
        while (correctActivity.equals(activity2)
            || activity1.equals(activity2)) {
            if (control >= 5) {
                while (correctActivity.equals(activity2))
                    activity2 = controller.getRandom().getBody();
            }
            if (control >= 10) break;
            boolean sign = random.nextBoolean();
            long newIncorrect = Utils.innerIncorrect(random,
                correctActivity.getConsumption_in_wh(),
                sign);
            activity2 = controller.getByEnergy(newIncorrect).getBody();
            control++;
        }
        control = 0;
        while (activity1.equals(activity2)
            || correctActivity.equals(activity2)) {
            if (control >= 5) {
                while (activity1.equals(activity2))
                    activity2 = controller.getRandom().getBody();
            }
            if (control >= 10) break;
            boolean sign = random.nextBoolean();
            long newIncorrect = Utils.innerIncorrect(random,
                correctActivity.getConsumption_in_wh(),
                sign);
            activity2 = controller.getByEnergy(newIncorrect).getBody();
            control++;
        }
        List<Activity> ans = new ArrayList<>();
        ans.add(correctActivity);
        ans.add(activity1);
        ans.add(activity2);
        return ans;
    }

    /**
     * Method for generating a new MCQuestion of type ASMUCHAS
     *
     * @param activity the base activity
     * @return a new MCQuestion of type ASMUCHAS
     */
    public Question generateAsMuchAs(Activity activity) {
        // generating the correct answer for the ASMUCHAS type of question
        Activity correctActivity = controller.getSimilarActivity(activity
                .getConsumption_in_wh()).getBody();

        // generating the two wrong answers using Utils.commons
        long[] twoOptions = Utils.generateIncorrectEnergy(correctActivity
                .getConsumption_in_wh());
        Activity activity1 = controller.getByEnergy(twoOptions[0]).getBody();
        Activity activity2 = controller.getByEnergy(twoOptions[1]).getBody();

        // generating the prompt
        String prompt = Utils.generatePrompt(Type.ASMUCHAS, activity);

        // putting all the activities in an ArrayList and shuffling them
        // so they wouldn't end up in the same place within the question every time
        List<Activity> activities = uniqueifyAnswers(correctActivity, activity1, activity2);
        Collections.shuffle(activities);

        return new MCQuestion(activity, prompt, activities.toArray(new Activity[3]),
                correctActivity, Type.ASMUCHAS);

    }

    /**
     * Method for generating a new MCQuestion type STRING
     *
     * @param activity the base activity
     * @return a new MCQuestion type STRING
     */
    public Question generateMCString(Activity activity) {
        // generating the two wrong answers using Utils.commons
        long[] twoOptions = Utils.generateIncorrectEnergy(activity.getConsumption_in_wh());
        Activity activity1 = controller.getByEnergy(twoOptions[0]).getBody();
        Activity activity2 = controller.getByEnergy(twoOptions[1]).getBody();

        // generating the prompt
        String prompt = Utils.generatePrompt(Type.STRING, activity);

        // putting all the activities in an ArrayList and shuffling them
        // so they wouldn't end up in the same place within the question every time
        List<Activity> activities = uniqueifyAnswers(activity, activity1, activity2);
        Collections.shuffle(activities);

        return new MCQuestion(activity, prompt,
                activities.toArray(new Activity[3]),
                findCorrectActivity(activities), Type.STRING);
    }

    /**
     * Method for finding the correct answer of the MCQuestion
     *
     * @param activities the list of activities/answers
     * @return the Activity with the highest energy consumption
     */
    public Activity findCorrectActivity(List<Activity> activities) {
        Long maximum = Long.MIN_VALUE;
        Activity activity = null;
        for (int i = 0; i < 3; i++) {
            if (activities.get(i).getConsumption_in_wh() > maximum) {
                maximum = activities.get(i).getConsumption_in_wh();
                activity = activities.get(i);
            }
        }
        return activity;
    }


    /**
     * Method for generating a new MCQuestion of type INTEGER
     *
     * @param activity the base activity
     * @return a new MCQuestion of type INTEGER
     */
    public Question generateMCIntegers(Activity activity) {
        // generating the two wrong answers using Utils.commons
        long[] twoOptions = Utils.generateIncorrectEnergy(activity.getConsumption_in_wh());
        Activity activity1 = controller.getByEnergy(twoOptions[0]).getBody();
        Activity activity2 = controller.getByEnergy(twoOptions[1]).getBody();

        // generating the prompt
        String prompt = Utils.generatePrompt(Type.INTEGER, activity);

        // putting all the activities in an ArrayList and shuffling them
        // so they wouldn't end up in the same place within the question every time
        List<Activity> activities = uniqueifyAnswers(activity, activity1, activity2);
        Collections.shuffle(activities);

        return new MCQuestion(activity, prompt,
                activities.toArray(new Activity[3]), activity, Type.INTEGER);
    }


    /**
     * Method for generating a new Open Question
     *
     * @param activity the base activity
     * @return a new Open Question
     */
    public Question generateOpenQuestion(Activity activity) {
        String prompt = Utils.generatePrompt(Type.OPEN, activity);
        return new OpenQuestion(activity, prompt);
    }

    /**
     * Get api endpoint for getting all the questions generated for a game
     *
     * @return all the questions generated
     */
    @GetMapping(path = {"", "/"})
    public List<Question> getAll() {
        // we generate the questions before fetching them to the client
        return generateQuestions();
    }


    /**
     * Get api endpoint for searching for a certain question in the list of questions
     * This method is supposed to be used after calling the getAll()
     *
     * @param index the index f the wanted question
     * @return the question
     */
    @GetMapping(path = {"/{gameID}/{index}"})
    public ResponseEntity<Question> getByIndex(@PathVariable int gameID,
                                               @PathVariable int index) {
        if (index == 0) {
            singlePlayerGame.put(gameID, generateQuestions());
        }
        if (index >= 20) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(singlePlayerGame.get(gameID).get(index));
    }


}
