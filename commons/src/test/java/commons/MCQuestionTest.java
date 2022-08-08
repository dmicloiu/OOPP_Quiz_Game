package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MCQuestionTest {

//    No-parameter constructor cannot be tested since it's `private`

    @Test
    public void asMuchAsConstructorTest() {
        Activity ba = new Activity("id1", "base activity", "source", "image_path", 1500);
        Activity wa1 = new Activity("id2", "wrong activity1", "source", "image_path", 1300);
        Activity wa2 = new Activity("id3", "wrong activity2", "source", "image_path", 1700);
        String pr = "prompt!";
        Activity[] answers = {ba, wa1, wa2};
        Type t = Type.ASMUCHAS;
        MCQuestion mcq = new MCQuestion(ba, pr, answers, ba, t);
        assertNotNull(mcq);
    }

    @Test
    public void stringConstructorTest() {
        Activity ba = new Activity("id1", "base activity", "source", "image_path", 1500);
        Activity wa1 = new Activity("id2", "wrong activity1", "source", "image_path", 1300);
        Activity wa2 = new Activity("id3", "wrong activity2", "source", "image_path", 1700);
        String pr = "prompt!";
        Activity[] answers = {ba, wa1, wa2};
        Type t = Type.STRING;
        MCQuestion mcq = new MCQuestion(ba, pr, answers, ba, t);
        assertNotNull(mcq);
    }

    @Test
    public void integerConstructorTest() {
        Activity ba = new Activity("id1", "base activity", "source", "image_path", 1500);
        Activity wa1 = new Activity("id2", "wrong activity1", "source", "image_path", 1300);
        Activity wa2 = new Activity("id3", "wrong activity2", "source", "image_path", 1700);
        String pr = "prompt!";
        Activity[] answers = {ba, wa1, wa2};
        Type t = Type.INTEGER;
        MCQuestion mcq = new MCQuestion(ba, pr, answers, ba, t);
        assertNotNull(mcq);
    }

    @Test
    public void invalidOpenConstructorTest() {
        Activity ba = new Activity("id1", "base activity", "source", "image_path", 1500);
        Activity wa1 = new Activity("id2", "wrong activity1", "source", "image_path", 1300);
        Activity wa2 = new Activity("id3", "wrong activity2", "source", "image_path", 1700);
        String pr = "prompt!";
        Activity[] answers = {ba, wa1, wa2};
        Type t = Type.OPEN;
        assertThrows(IllegalArgumentException.class, () -> {
            var mcq = new MCQuestion(ba, pr, answers, ba, t);
        });
    }

    @Test
    public void invalidLengthConstructorTest() {
        Activity ba = new Activity("id1", "base activity", "source", "image_path", 1500);
        Activity wa1 = new Activity("id2", "wrong activity1", "source", "image_path", 1300);
        Activity wa2 = new Activity("id3", "wrong activity2", "source", "image_path", 1700);
        Activity wa3 = new Activity("id4", "wrong activity2", "source", "image_path", 2700);
        String pr = "prompt!";
        Activity[] answers = {ba, wa1, wa2, wa3};
        Type t = Type.STRING;
        assertThrows(IllegalArgumentException.class, () -> {
            var mcq = new MCQuestion(ba, pr, answers, ba, t);
        });
    }

    @Test
    public void baGetterTest() {
        Activity ba = new Activity("id1", "base activity", "source", "image_path", 1500);
        Activity wa1 = new Activity("id2", "wrong activity1", "source", "image_path", 1300);
        Activity wa2 = new Activity("id3", "wrong activity2", "source", "image_path", 1700);
        String pr = "prompt!";
        Activity[] answers = {ba, wa1, wa2};
        Type t = Type.INTEGER;
        MCQuestion mcq = new MCQuestion(ba, pr, answers, ba, t);

        assertEquals(ba, mcq.getBaseActivity());
    }

    @Test
    public void baSetterTest() {
        Activity ba = new Activity("id1", "base activity", "source", "image_path", 1500);
        Activity wa1 = new Activity("id2", "wrong activity1", "source", "image_path", 1300);
        Activity wa2 = new Activity("id3", "wrong activity2", "source", "image_path", 1700);
        String pr = "prompt!";
        Activity[] answers = {ba, wa1, wa2};
        Type t = Type.INTEGER;
        MCQuestion mcq = new MCQuestion(ba, pr, answers, ba, t);

        mcq.setBaseActivity(wa1);
        assertEquals(wa1, mcq.getBaseActivity());
    }

    @Test
    public void promptGetterTest() {
        Activity ba = new Activity("id1", "base activity", "source", "image_path", 1500);
        Activity wa1 = new Activity("id2", "wrong activity1", "source", "image_path", 1300);
        Activity wa2 = new Activity("id3", "wrong activity2", "source", "image_path", 1700);
        String pr = "prompt!";
        Activity[] answers = {ba, wa1, wa2};
        Type t = Type.INTEGER;
        MCQuestion mcq = new MCQuestion(ba, pr, answers, ba, t);

        assertEquals("prompt!", mcq.getPrompt());
    }

    @Test
    public void promptSetterTest() {
        Activity ba = new Activity("id1", "base activity", "source", "image_path", 1500);
        Activity wa1 = new Activity("id2", "wrong activity1", "source", "image_path", 1300);
        Activity wa2 = new Activity("id3", "wrong activity2", "source", "image_path", 1700);
        String pr = "prompt!";
        Activity[] answers = {ba, wa1, wa2};
        Type t = Type.INTEGER;
        MCQuestion mcq = new MCQuestion(ba, pr, answers, ba, t);

        mcq.setPrompt("prompt!!");
        assertEquals("prompt!!", mcq.getPrompt());
    }

    @Test
    public void answersGetterTest() {
        Activity ba = new Activity("id1", "base activity", "source", "image_path", 1500);
        Activity wa1 = new Activity("id2", "wrong activity1", "source", "image_path", 1300);
        Activity wa2 = new Activity("id3", "wrong activity2", "source", "image_path", 1700);
        String pr = "prompt!";
        Activity[] answers = {ba, wa1, wa2};
        Type t = Type.INTEGER;
        MCQuestion mcq = new MCQuestion(ba, pr, answers, ba, t);

        assertEquals(answers, mcq.getAnswers());
    }

    @Test
    public void answersSetterTest() {
        Activity ba = new Activity("id1", "base activity", "source", "image_path", 1500);
        Activity wa1 = new Activity("id2", "wrong activity1", "source", "image_path", 1300);
        Activity wa2 = new Activity("id3", "wrong activity2", "source", "image_path", 1700);
        Activity wa3 = new Activity("id4", "wrong activity3", "source", "image_path", 2000);
        String pr = "prompt!";
        Activity[] answers1 = {ba, wa1, wa2};
        Activity[] answers2 = {ba, wa1, wa3};
        Type t = Type.INTEGER;
        MCQuestion mcq = new MCQuestion(ba, pr, answers1, ba, t);

        mcq.setAnswers(answers2);
        assertEquals(answers2, mcq.getAnswers());
    }

    @Test
    public void answersSetterInvalidLengthTest() {
        Activity ba = new Activity("id1", "base activity", "source", "image_path", 1500);
        Activity wa1 = new Activity("id2", "wrong activity1", "source", "image_path", 1300);
        Activity wa2 = new Activity("id3", "wrong activity2", "source", "image_path", 1700);
        Activity wa3 = new Activity("id4", "wrong activity3", "source", "image_path", 2000);
        String pr = "prompt!";
        Activity[] answers = {ba, wa1, wa2, wa3};
        Type t = Type.INTEGER;
        assertThrows(IllegalArgumentException.class, () -> {
            var mcq = new MCQuestion(ba, pr, answers, ba, t);
        });
    }

    @Test
    public void correctGetterTest() {
        Activity ba = new Activity("id1", "base activity", "source", "image_path", 1500);
        Activity wa1 = new Activity("id2", "wrong activity1", "source", "image_path", 1300);
        Activity wa2 = new Activity("id3", "wrong activity2", "source", "image_path", 1700);
        String pr = "prompt!";
        Activity[] answers = {ba, wa1, wa2};
        Type t = Type.INTEGER;
        MCQuestion mcq = new MCQuestion(ba, pr, answers, ba, t);

        assertEquals(ba, mcq.getCorrect());
    }

    @Test
    public void correctSetterTest() {
        Activity ba = new Activity("id1", "base activity", "source", "image_path", 1500);
        Activity wa1 = new Activity("id2", "wrong activity1", "source", "image_path", 1300);
        Activity wa2 = new Activity("id3", "wrong activity2", "source", "image_path", 1700);
        String pr = "prompt!";
        Activity[] answers = {ba, wa1, wa2};
        Type t = Type.INTEGER;
        MCQuestion mcq = new MCQuestion(ba, pr, answers, ba, t);

        mcq.setCorrect(wa1);
        assertEquals(wa1, mcq.getCorrect());
    }

    @Test
    public void typeGetterTest() {
        Activity ba = new Activity("id1", "base activity", "source", "image_path", 1500);
        Activity wa1 = new Activity("id2", "wrong activity1", "source", "image_path", 1300);
        Activity wa2 = new Activity("id3", "wrong activity2", "source", "image_path", 1700);
        String pr = "prompt!";
        Activity[] answers = {ba, wa1, wa2};
        Type t = Type.INTEGER;
        MCQuestion mcq = new MCQuestion(ba, pr, answers, ba, t);

        assertEquals(Type.INTEGER, mcq.getType());
    }

    @Test
    public void typeSetterTest() {
        Activity ba = new Activity("id1", "base activity", "source", "image_path", 1500);
        Activity wa1 = new Activity("id2", "wrong activity1", "source", "image_path", 1300);
        Activity wa2 = new Activity("id3", "wrong activity2", "source", "image_path", 1700);
        String pr = "prompt!";
        Activity[] answers = {ba, wa1, wa2};
        Type t = Type.STRING;
        MCQuestion mcq = new MCQuestion(ba, pr, answers, ba, t);

        mcq.setType(Type.INTEGER);
        assertEquals(Type.INTEGER, mcq.getType());
    }

    @Test
    public void typeSetterInvalidTypeTest() {
        Activity ba = new Activity("id1", "base activity", "source", "image_path", 1500);
        Activity wa1 = new Activity("id2", "wrong activity1", "source", "image_path", 1300);
        Activity wa2 = new Activity("id3", "wrong activity2", "source", "image_path", 1700);
        String pr = "prompt!";
        Activity[] answers = {ba, wa1, wa2};
        Type t = Type.STRING;
        MCQuestion mcq = new MCQuestion(ba, pr, answers, ba, t);

        assertThrows(IllegalArgumentException.class, () -> {
            mcq.setType(Type.OPEN);
        });
    }

    @Test
    public void equalsTestSuccess() {
        Activity ba = new Activity("id1", "base activity", "source", "image_path", 1500);
        Activity wa1 = new Activity("id2", "wrong activity1", "source", "image_path", 1300);
        Activity wa2 = new Activity("id3", "wrong activity2", "source", "image_path", 1700);
        String pr = "prompt!";
        Activity[] answers = {ba, wa1, wa2};
        Type t = Type.STRING;
        MCQuestion mcq1 = new MCQuestion(ba, pr, answers, ba, t);
        MCQuestion mcq2 = new MCQuestion(ba, pr, answers, ba, t);

        assertEquals(mcq1, mcq2);
    }

    @Test
    public void equalsTestDifferentBAFailure() {
        Activity ba = new Activity("id1", "base activity", "source", "image_path", 1500);
        Activity wa1 = new Activity("id2", "wrong activity1", "source", "image_path", 1300);
        Activity wa2 = new Activity("id3", "wrong activity2", "source", "image_path", 1700);
        String pr = "prompt!";
        Activity[] answers = {ba, wa1, wa2};
        Type t = Type.STRING;
        MCQuestion mcq1 = new MCQuestion(ba, pr, answers, ba, t);
        MCQuestion mcq2 = new MCQuestion(wa1, pr, answers, ba, t);

        assertNotEquals(mcq1, mcq2);
    }

    @Test
    public void equalsTestDifferentPromptFailure() {
        Activity ba = new Activity("id1", "base activity", "source", "image_path", 1500);
        Activity wa1 = new Activity("id2", "wrong activity1", "source", "image_path", 1300);
        Activity wa2 = new Activity("id3", "wrong activity2", "source", "image_path", 1700);
        String pr = "prompt!";
        String pr2 = "prompt!!";
        Activity[] answers = {ba, wa1, wa2};
        Type t = Type.STRING;
        MCQuestion mcq1 = new MCQuestion(ba, pr, answers, ba, t);
        MCQuestion mcq2 = new MCQuestion(ba, pr2, answers, ba, t);

        assertNotEquals(mcq1, mcq2);
    }

    @Test
    public void equalsTestDifferentAnswersArrayFailure() {
        Activity ba = new Activity("id1", "base activity", "source", "image_path", 1500);
        Activity wa1 = new Activity("id2", "wrong activity1", "source", "image_path", 1300);
        Activity wa2 = new Activity("id3", "wrong activity2", "source", "image_path", 1700);
        Activity wa3 = new Activity("id4", "wrong activity3", "source", "image_path", 2000);
        String pr = "prompt!";
        Activity[] answers = {ba, wa1, wa2};
        Activity[] answers2 = {ba, wa1, wa3};
        Type t = Type.STRING;
        MCQuestion mcq1 = new MCQuestion(ba, pr, answers, ba, t);
        MCQuestion mcq2 = new MCQuestion(ba, pr, answers2, ba, t);

        assertNotEquals(mcq1, mcq2);
    }

    @Test
    public void equalsTestDifferentCorrectAnswerFailure() {
        Activity ba = new Activity("id1", "base activity", "source", "image_path", 1500);
        Activity wa1 = new Activity("id2", "wrong activity1", "source", "image_path", 1300);
        Activity wa2 = new Activity("id3", "wrong activity2", "source", "image_path", 1700);
        String pr = "prompt!";
        Activity[] answers = {ba, wa1, wa2};
        Type t = Type.STRING;
        MCQuestion mcq1 = new MCQuestion(ba, pr, answers, ba, t);
        MCQuestion mcq2 = new MCQuestion(ba, pr, answers, wa1, t);

        assertNotEquals(mcq1, mcq2);
    }

    @Test
    public void equalsTestDifferentTypeFailure() {
        Activity ba = new Activity("id1", "base activity", "source", "image_path", 1500);
        Activity wa1 = new Activity("id2", "wrong activity1", "source", "image_path", 1300);
        Activity wa2 = new Activity("id3", "wrong activity2", "source", "image_path", 1700);
        String pr = "prompt!";
        Activity[] answers = {ba, wa1, wa2};
        Type t = Type.STRING;
        MCQuestion mcq1 = new MCQuestion(ba, pr, answers, ba, t);
        MCQuestion mcq2 = new MCQuestion(ba, pr, answers, wa1, Type.INTEGER);

        assertNotEquals(mcq1, mcq2);
    }

    @Test
    public void hashcodeSameTest() {
        Activity ba = new Activity("id1", "base activity", "source", "image_path", 1500);
        Activity wa1 = new Activity("id2", "wrong activity1", "source", "image_path", 1300);
        Activity wa2 = new Activity("id3", "wrong activity2", "source", "image_path", 1700);
        String pr = "prompt!";
        Activity[] answers = {ba, wa1, wa2};
        Type t = Type.STRING;
        MCQuestion mcq1 = new MCQuestion(ba, pr, answers, ba, t);
        MCQuestion mcq2 = new MCQuestion(ba, pr, answers, ba, t);

        int hash1 = mcq1.hashCode();
        int hash2 = mcq2.hashCode();

        assertEquals(hash1, hash2);
    }

    @Test
    public void hashcodeDifferentBATest() {
        // Since the `hashCode()` functionality is sufficiently tested in `ActivityTest`,
        // I don't think it also needs to be extensively tested here
        Activity ba1 = new Activity("id1", "base1 activity", "source", "image_path", 1500);
        Activity ba2 = new Activity("id1", "base2 activity", "source", "image_path", 1500);
        Activity wa1 = new Activity("id2", "wrong activity1", "source", "image_path", 1300);
        Activity wa2 = new Activity("id3", "wrong activity2", "source", "image_path", 1700);
        String pr = "prompt!";
        Activity[] answers = {ba1, wa1, wa2};
        Type t = Type.STRING;
        MCQuestion mcq1 = new MCQuestion(ba1, pr, answers, ba1, t);
        MCQuestion mcq2 = new MCQuestion(ba2, pr, answers, ba2, t);

        int hash1 = mcq1.hashCode();
        int hash2 = mcq2.hashCode();

        assertNotEquals(hash1, hash2);
    }

    @Test
    public void hashcodeDifferentPromptTest() {
        Activity ba = new Activity("id1", "base activity", "source", "image_path", 1500);
        Activity wa1 = new Activity("id2", "wrong activity1", "source", "image_path", 1300);
        Activity wa2 = new Activity("id3", "wrong activity2", "source", "image_path", 1700);
        String pr1 = "prompt!";
        String pr2 = "prompt!!";
        Activity[] answers = {ba, wa1, wa2};
        Type t = Type.STRING;
        MCQuestion mcq1 = new MCQuestion(ba, pr1, answers, ba, t);
        MCQuestion mcq2 = new MCQuestion(ba, pr2, answers, ba, t);

        int hash1 = mcq1.hashCode();
        int hash2 = mcq2.hashCode();

        assertNotEquals(hash1, hash2);
    }

    @Test
    public void hashcodeDifferentAnswersArrayTest() {
        Activity ba = new Activity("id1", "base activity", "source", "image_path", 1500);
        Activity wa1 = new Activity("id2", "wrong activity1", "source", "image_path", 1300);
        Activity wa2 = new Activity("id3", "wrong activity2", "source", "image_path", 1700);
        Activity wa3 = new Activity("id4", "wrong activity3", "source", "image_path", 2000);
        String pr = "prompt!";
        Activity[] answers1 = {ba, wa1, wa2};
        Activity[] answers2 = {ba, wa1, wa3};
        Type t = Type.STRING;
        MCQuestion mcq1 = new MCQuestion(ba, pr, answers1, ba, t);
        MCQuestion mcq2 = new MCQuestion(ba, pr, answers2, ba, t);

        int hash1 = mcq1.hashCode();
        int hash2 = mcq2.hashCode();

        assertNotEquals(hash1, hash2);
    }

    @Test
    public void hashcodeDifferentCorrActTest() {
        Activity ba = new Activity("id1", "base activity", "source", "image_path", 1500);
        Activity wa1 = new Activity("id2", "wrong activity1", "source", "image_path", 1300);
        Activity wa2 = new Activity("id3", "wrong activity2", "source", "image_path", 1700);
        String pr = "prompt!";
        Activity[] answers = {ba, wa1, wa2};
        Type t = Type.STRING;
        MCQuestion mcq1 = new MCQuestion(ba, pr, answers, ba, t);
        MCQuestion mcq2 = new MCQuestion(ba, pr, answers, wa1, t);

        int hash1 = mcq1.hashCode();
        int hash2 = mcq2.hashCode();

        assertNotEquals(hash1, hash2);
    }

    @Test
    public void hashcodeDifferentTypeTest() {
        Activity ba = new Activity("id1", "base activity", "source", "image_path", 1500);
        Activity wa1 = new Activity("id2", "wrong activity1", "source", "image_path", 1300);
        Activity wa2 = new Activity("id3", "wrong activity2", "source", "image_path", 1700);
        String pr = "prompt!";
        Activity[] answers = {ba, wa1, wa2};
        MCQuestion mcq1 = new MCQuestion(ba, pr, answers, ba, Type.INTEGER);
        MCQuestion mcq2 = new MCQuestion(ba, pr, answers, wa1, Type.ASMUCHAS);

        int hash1 = mcq1.hashCode();
        int hash2 = mcq2.hashCode();

        assertNotEquals(hash1, hash2);
    }

    @Test
    public void toStringTest() {
        Activity ba = new Activity("id1", "base activity", "source", "image_path", 1500);
        Activity wa1 = new Activity("id2", "wrong activity1", "source", "image_path", 1300);
        Activity wa2 = new Activity("id3", "wrong activity2", "source", "image_path", 1700);
        String pr = "prompt!";
        Activity[] answers = {ba, wa1, wa2};
        Type t = Type.STRING;
        MCQuestion mcq1 = new MCQuestion(ba, pr, answers, ba, t);

        assertNotNull(mcq1.toString());
    }

    @Test
    public void testException() {
        Activity ba = new Activity("id1", "base activity", "source", "image_path", 1500);
        Activity wa1 = new Activity("id2", "wrong activity1", "source", "image_path", 1300);
        Activity wa2 = new Activity("id3", "wrong activity2", "source", "image_path", 1700);
        String pr = "prompt!";
        Activity[] answers = {ba, wa1, wa2};
        Type t = Type.STRING;
        MCQuestion mcq1 = new MCQuestion(ba, pr, answers, ba, t);
        Activity[] answersWrong = {ba, wa1};

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            mcq1.setAnswers(answersWrong);
        });

        assertEquals("Always three answers, there are, no more, no less.", exception.getMessage());
    }
}
