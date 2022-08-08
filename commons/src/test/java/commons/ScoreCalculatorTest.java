package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class ScoreCalculatorTest {

    private Activity activity;
    private OpenQuestion openQuestion;
    private MCQuestion mcQuestion;
    private Activity[] answersMC;
    private Activity activityHelper;

    @BeforeEach
    public void init() {
        activity = new Activity("Test", "New activity :)",
                "www.google.com", "image_path", 100);
        activityHelper = new Activity("Test 1", "New activity :)",
                "www.google.com", "image_path", 56);
        openQuestion = new OpenQuestion(activity, Utils.generatePrompt(Type.OPEN, activity));
        answersMC = new Activity[3];
        answersMC[0] = activity;
        answersMC[1] = activityHelper;
        answersMC[2] = new Activity("Test 2", "New activity :)",
                "www.google.com", "image_path", 78);
        mcQuestion = new MCQuestion(activity, Utils.generatePrompt(Type.OPEN, activity),
                answersMC, activity, Type.STRING);
    }

    @Test
    public void testConstructorMCQuestion() {
        ScoreCalculator scoreCalculator = new ScoreCalculator(mcQuestion, activity, 3);
        assertNotNull(scoreCalculator);
    }

    @Test
    public void testConstructorOpenQuestion() {
        ScoreCalculator scoreCalculator = new ScoreCalculator(openQuestion, 120, 3);
        assertNotNull(scoreCalculator);
    }

    @Test
    public void testOpenScore() {
        ScoreCalculator scoreCalculator = new ScoreCalculator(openQuestion, 120, 3);
        assertEquals(80, scoreCalculator.calcScore());
    }

    @Test
    public void testOpenZero() {
        ScoreCalculator scoreCalculator = new ScoreCalculator(openQuestion, 1000, 3);
        assertEquals(0, scoreCalculator.calcScore());
    }

    @Test
    public void testOpenScoreTotalPenalty() {
        ScoreCalculator scoreCalculator = new ScoreCalculator(openQuestion, 120, 15);
        assertEquals(4, scoreCalculator.calcScore());
    }

    @Test
    public void testOpenScorePenalty() {
        ScoreCalculator scoreCalculator = new ScoreCalculator(openQuestion, 120, 9);
        assertEquals(40, scoreCalculator.calcScore());
    }

    @Test
    public void testMCScoreWrong() {
        ScoreCalculator scoreCalculator = new ScoreCalculator(mcQuestion, activityHelper, 3);
        assertEquals(0, scoreCalculator.calcScore());
    }

    @Test
    public void testMCScoreRight() {
        ScoreCalculator scoreCalculator = new ScoreCalculator(mcQuestion, activity, 2);
        assertEquals(100, scoreCalculator.calcScore());
    }

    @Test
    public void testMCPenalty() {
        ScoreCalculator scoreCalculator = new ScoreCalculator(mcQuestion, activity, 12);
        assertEquals(25, scoreCalculator.calcScore());
    }

    @Test
    public void testMCTotalPenalty() {
        ScoreCalculator scoreCalculator = new ScoreCalculator(mcQuestion, activity, 15);
        assertEquals(5, scoreCalculator.calcScore());
    }
}
