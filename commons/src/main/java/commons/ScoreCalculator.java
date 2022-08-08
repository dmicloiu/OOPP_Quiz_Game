package commons;

public class ScoreCalculator {

    private OpenQuestion oq;
    private long playerGuess;

    private MCQuestion mcq;
    private Activity playerAnswer;

    private final double secondsNeeded;

    private final boolean isOpen;

    /**
     * Constructor of `ScoreCalculator` class for an open-ended question
     *
     * @param oq,            `OpenQuestion` object related to the question,
     *                       whose score is to be calculated
     * @param playerGuess,   `long` indicating the player's guess
     * @param secondsNeeded, `double` indicating how long the player needed to answer (in seconds)
     */
    public ScoreCalculator(OpenQuestion oq, long playerGuess, double secondsNeeded) {

        this.oq = oq;
        this.isOpen = true;
        this.playerGuess = playerGuess;
        this.secondsNeeded = secondsNeeded;
    }

    /**
     * Constructor of `ScoreCalculator` class for a multiple-choice question
     *
     * @param mcq,           `MCQuestion` object related to the question, whose score is to be
     *                       calculated
     * @param playerAnswer,  `Activity` object indicating the player's answer
     * @param secondsNeeded, `double` indicating how long the player needed to answer (in seconds)
     */
    public ScoreCalculator(MCQuestion mcq, Activity playerAnswer, double secondsNeeded) {

        this.mcq = mcq;
        this.isOpen = false;
        this.playerAnswer = playerAnswer;
        this.secondsNeeded = secondsNeeded;
    }

    /**
     * calcScore() uses 2 helper methods to calculate the player's score to an `MCQuestion` or
     * to an `OpenQuestion`. Depending on how long the user needed to give an answer, the score
     * can be reduced by a percentage amount.
     *
     * @return int, final score assigned to player
     */
    public int calcScore() {

        double score;

        if (this.isOpen) {
            score = calcScoreOpen();
        } else {
            score = calcScoreMC();
        }

        score = calcTimePenalty(score);

        return (int) score;
    }

    /**
     * If the answer is given in the first 20% of `totalTime`, 'full' points are awarded.
     * After that, a percentage of points is subtracted according to how long the person
     * took to answer.
     * @param score, player score after time penalty
     */
    private double calcTimePenalty(double score) {

        int totalTime = 15;
        if (secondsNeeded < totalTime * 0.2) {
            return score;
        } else {
            if (secondsNeeded == totalTime) return score * 0.05;
            double temp = (secondsNeeded - (totalTime * 0.2)) / (totalTime * 0.8);

            temp = 1.0 - temp;

            temp = score * temp;

            return temp;
        }

    }

    /**
     * calcScoreOpen() calculates a player's score, based on their answer to an `OpenQuestion`.
     * @return double, score assigned to player
     */
    private double calcScoreOpen() {
        long upperBound = (long) (1.4 * oq.getCorrect());
        long lowerBound = (long) (0.6 * oq.getCorrect());

        double score;

        /*
        A lower and an upper bound are calculated. If the use guess is too far off the correct
        answer, 0 points are awarded. Else, a formula is used to calculate the points, according
        to how off the guess was.
         */
        if (playerGuess < lowerBound || playerGuess > upperBound) {
            return 0.0;
        } else {

            double corr = oq.getCorrect();

            score = Math.abs(playerGuess - corr);

            score = score / corr;

            score = 100.0 - score * 100.0;

            return score;
        }
    }

    /**
     * calcScoreMC() calculates a player's score, based on their answer to an `MCQuestion`.
     * @return double, score assigned to player
     */
    private double calcScoreMC() {

        if (mcq.getCorrect().equals(playerAnswer)) {
            return 100.0;
        } else {
            return 0.0;
        }
    }
}
