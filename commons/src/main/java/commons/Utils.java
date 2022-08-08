package commons;

import java.util.Random;

/**
 * This class contains methods that are used in question generation.
 */
public class Utils {

    /**
     * This method is called either directly by an activity subclass
     * constructor or by the QuestionGenerator. Its purpose is to take
     * the energy value assigned to an activity, and create a set
     * of two random (but within a reasonable range) incorrect answers
     * to be used either directly as answers or as bases for incorrect
     * answers.
     *
     * @param correct the correct amount of energy used by the calling activity.
     * @return a two-valued array of longs, containing incorrect energy values
     * for a given activity for which this method is called
     */
    public static long[] generateIncorrectEnergy(long correct) {
        Random random = new Random();
        int combination = random.nextInt(3);
        boolean first, second;
        switch (combination) {
            case 0:
                first = true;
                second = true;
                break;
            case 1:
                first = true;
                second = false;
                break;
            case 2:
                first = false;
                second = false;
                break;
            default: //because intellij was complaining
                first = random.nextBoolean();
                second = random.nextBoolean();
        }
        //generate first incorrect answer
        long inc1 = innerIncorrect(random, correct, first);
        //generate second incorrect answer
        long inc2 = innerIncorrect(random, correct, second);
        return new long[]{inc1, inc2};
    }

    /**
     * This method does the necessary calculations for the generateIncorrectEnergy
     * method to create two incorrect energy values to be used either as direct
     * incorrect answers (in MCQuestionInteger) or as a basis for incorrect answers
     * (in MCQuestionString or AsMuchAsQuestion)
     *
     * @param random  a pseudorandom number generator used by this inner
     *                method to create an incorrect (integer) energy value
     *                for a given activity
     * @param correct the correct amount of energy required for the given activity.
     *                The long returned by this method should never be equal to this param.
     * @return the newly randomly generated incorrect amount of energy to be fed into an
     * appropriate description
     */
    public static long innerIncorrect(Random random, long correct, boolean isGreater) {
        long inc;
        if (isGreater) {
            double factor = (random.nextInt(300) / 100.0) + 0.01;
            inc = (long) (correct * (1.0 + factor));
        } else {
            double factor = (random.nextInt(75) / 100.0) + 0.01;
            inc = (long) (correct * (1.0 - factor));
        }
        if (inc == correct) inc = (long) (1.1 * correct);
        if (inc == 0) inc++;
        return inc;
    }

    /**
     * This method is to be used when generating questions. We first randomize a type
     * (one of the four in the enum Type.) Then we generate the prompt using this method.
     * A pseudorandom number generator is used to randomize the structure of the prompt,
     * which additionally depends on the type of question. The prompt and type are then both
     * given to the appropriate question subclass constructor.
     * Please note that an open question constructor does not use a type param, as there is
     * only one type available to it.
     *
     * @param type     the type of the question for which the prompt is being generated
     * @param activity the activity for which this question is being generated
     *
     * @return a string prompt for the question
     */
    public static String generatePrompt(Type type, Activity activity) {
        Random random = new Random();
        int syntax = random.nextInt(3);
        String prompt = "";
        char[] chars = activity.getTitle().toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        String title = String.valueOf(chars);
        switch (type) {
            case INTEGER:
                prompt = innerGenerateInteger(syntax, title);
                break;
            case STRING:
                prompt = innerGenerateString(syntax);
                break;
            case ASMUCHAS:
                prompt = innerGenerateAsMuchAs(syntax, title);
                break;
            case OPEN:
                prompt = innerGenerateOpen(syntax, title);
        }
        return prompt;
    }

    /**
     * This method is called by the generatePrompt method to generate a prompt
     * from the base activity's description using a syntax selected randomly in
     * generatePrompt. Used by multiple-choice questions of type INTEGER.
     *
     * @param syntax the type of syntax of the generated question
     * @param activityDescription the description of the base activity of the
     *                            question
     *
     * @return a string prompt for the generated question
     */
    protected static String innerGenerateInteger(int syntax, String activityDescription) {
        String prompt = "";
        switch (syntax) {
            case 0:
                prompt =
                        "How much energy (in Wh) does "
                                + activityDescription +
                                " use?";
                break;
            case 1:
                prompt =
                        "How many Wh of energy does "
                                + activityDescription +
                                " need?";
                break;
            case 2:
                prompt =
                        "If you are "
                                + activityDescription +
                                ", how much energy in Wh would you need to use?";
                break;
        }
        return prompt;
    }

    /**
     * This method is called by the generatePrompt method to generate a prompt
     * using a syntax selected randomly in generatePrompt. Used by multiple-choice
     * questions of type STRING.
     *
     * @param syntax the type of syntax of the generated question
     *
     * @return a string prompt for the generated question
     */
    protected static String innerGenerateString(int syntax) {
        String prompt = "";
        switch (syntax) {
            case 0:
                prompt =
                        "What requires more energy?";
                break;
            case 1:
                prompt =
                        "What is more expensive?";
                break;
            case 2:
                prompt =
                        "Which of the following activities has the highest energy consumption?";
                break;
        }
        return prompt;
    }

    /**
     * This method is called by the generatePrompt method to generate a prompt
     * from the base activity's description using a syntax selected randomly in
     * generatePrompt. Used by multiple-choice questions of type ASMUCHAS.
     *
     * @param syntax the type of syntax of the generated question
     * @param activityDescription the description of the base activity of the
     *                            question
     *
     * @return a string prompt for the generated question
     */
    protected static String innerGenerateAsMuchAs(int syntax, String activityDescription) {
        String prompt = "";
        switch (syntax) {
            case 0:
                prompt =
                        "Which of the following uses as much energy as "
                                + activityDescription +
                                "?";
                break;
            case 1:
                prompt =
                        "What needs as many Wh of energy as "
                                + activityDescription +
                                "?";
                break;
            case 2:
                prompt =
                        "If you have exactly as much energy as is used when "
                                + activityDescription +
                                ", which of the following can you do?";
                break;
        }
        return prompt;
    }

    /**
     * This method is called by the generatePrompt method to generate a prompt
     * from the base activity's description using a syntax selected randomly in
     * generatePrompt. Used by open questions/questions of type OPEN.
     *
     * @param syntax the type of syntax of the generated question
     * @param activityDescription the description of the base activity of the
     *                            question
     *
     * @return a string prompt for the generated question
     */
    protected static String innerGenerateOpen(int syntax, String activityDescription) {
        String prompt = "";
        switch (syntax) {
            case 0:
                prompt =
                        "How much energy in Wh does "
                                + activityDescription +
                                " use? Provide your best guess.";
                break;
            case 1:
                prompt = "Try to guess how many Wh of energy "
                        + activityDescription +
                        " requires.";
                break;
            case 2:
                prompt =
                        "Provide your best estimate of how much energy (in Wh) is used when "
                                + activityDescription;
                break;
        }
        return prompt;
    }

}
