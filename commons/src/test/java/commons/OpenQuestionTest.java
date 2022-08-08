package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenQuestionTest {

    //    No-parameter constructor cannot be tested since it's `private`

    @Test
    public void constructorTest() {
        var ba = new Activity("id1", "base activity", "source", "image_path", 1500);
        var pr = "prompt!";
        OpenQuestion oq = new OpenQuestion(ba, pr);

        assertNotNull(oq);
    }

    @Test
    public void baGetterTest() {
        Activity ba = new Activity("id1", "base activity", "source", "image_path", 1500);
        String pr = "prompt!";
        OpenQuestion oq = new OpenQuestion(ba, pr);

        assertEquals(ba, oq.getBaseActivity());
    }

    @Test
    public void baSetterTest() {
        Activity ba = new Activity("id1", "base activity", "source", "image_path", 1500);
        Activity act2 = new Activity("id2", "second activity", "source", "image_path", 3000);
        String pr = "prompt!";
        OpenQuestion oq = new OpenQuestion(ba, pr);

        oq.setBaseActivity(act2);
        assertEquals(act2, oq.getBaseActivity());
    }

    @Test
    public void promptGetterTest() {
        Activity ba = new Activity("id1", "base activity", "source", "image_path", 1500);
        String pr = "prompt!";
        OpenQuestion oq = new OpenQuestion(ba, pr);

        assertEquals(pr, oq.getPrompt());
    }

    @Test
    public void promptSetterTest() {
        Activity ba = new Activity("id1", "base activity", "source", "image_path", 1500);
        String pr = "prompt!";
        OpenQuestion oq = new OpenQuestion(ba, pr);

        oq.setPrompt("prompt!!");
        assertEquals("prompt!!", oq.getPrompt());
    }

    @Test
    public void correctGetterTest() {
        Activity ba = new Activity("id1", "base activity", "source", "image_path", 1500);
        String pr = "prompt!";
        OpenQuestion oq = new OpenQuestion(ba, pr);

        assertEquals(1500, oq.getCorrect());
    }

    @Test
    public void correctSetterTest() {
        Activity ba = new Activity("id1", "base activity", "source", "image_path", 1500);
        String pr = "prompt!";
        OpenQuestion oq = new OpenQuestion(ba, pr);

        oq.setCorrect(3000);
        assertEquals(3000, oq.getCorrect());
    }

    @Test
    public void equalsTestSuccess() {
        Activity ba = new Activity("id1", "base activity", "source", "image_path", 1500);
        Activity ba2 = new Activity("id1", "base activity", "source", "image_path", 1500);
        String pr = "prompt!";
        OpenQuestion oq1 = new OpenQuestion(ba, pr);
        OpenQuestion oq2 = new OpenQuestion(ba2, pr);

        assertEquals(oq1, oq2);
    }

    @Test
    public void equalsTestDifferentBAFailure() {
        Activity ba = new Activity("id1", "base activity", "source", "image_path", 1500);
        Activity ba2 = new Activity("id2", "second activity", "source", "image_path", 1550);
        String pr = "prompt!";
        OpenQuestion oq1 = new OpenQuestion(ba, pr);
        OpenQuestion oq2 = new OpenQuestion(ba2, pr);

        assertNotEquals(oq1, oq2);
    }

    @Test
    public void equalsTestDifferentPromptFailure() {
        Activity ba = new Activity("id1", "base activity", "source", "image_path", 1500);
        String pr = "prompt!";
        String pr2 = "prompt!!";
        OpenQuestion oq1 = new OpenQuestion(ba, pr);
        OpenQuestion oq2 = new OpenQuestion(ba, pr2);

        assertNotEquals(oq1, oq2);
    }

    @Test
    public void hashcodeSameTest() {
        Activity ba = new Activity("id1", "base activity", "source", "image_path", 1500);
        String pr = "prompt!";
        OpenQuestion oq1 = new OpenQuestion(ba, pr);
        OpenQuestion oq2 = new OpenQuestion(ba, pr);

        int hash1 = oq1.hashCode();
        int hash2 = oq2.hashCode();

        assertEquals(hash1, hash2);
    }

    @Test
    public void hashcodeDifferentBATest() {
        // Since the `hashCode()` functionality is sufficiently tested in `ActivityTest`,
        // I don't think it also needs to be extensively tested here
        Activity ba1 = new Activity("id1", "first base activity", "source", "image_path", 1500);
        Activity ba2 = new Activity("id2", "second base activity", "source", "image_path", 1500);
        String pr = "prompt!";
        OpenQuestion oq1 = new OpenQuestion(ba1, pr);
        OpenQuestion oq2 = new OpenQuestion(ba2, pr);

        int hash1 = oq1.hashCode();
        int hash2 = oq2.hashCode();

        assertNotEquals(hash1, hash2);
    }

    @Test
    public void hashcodeDifferentPromptTest() {
        Activity ba = new Activity("id", "base activity", "source", "image_path", 1500);
        String pr1 = "prompt!";
        String pr2 = "prompt!!";
        OpenQuestion oq1 = new OpenQuestion(ba, pr1);
        OpenQuestion oq2 = new OpenQuestion(ba, pr2);

        int hash1 = oq1.hashCode();
        int hash2 = oq2.hashCode();

        assertNotEquals(hash1, hash2);
    }

    @Test
    public void toStringTest() {
        Activity ba = new Activity("id1", "base activity", "source", "image_path", 1500);
        String pr = "prompt!";
        OpenQuestion oq = new OpenQuestion(ba, pr);

        assertNotNull(oq.toString());
    }

    @Test
    public void testExceptionScore() {
        Activity ba = new Activity("id1", "base activity", "source", "image_path", 1500);
        String pr = "prompt!";
        OpenQuestion oq = new OpenQuestion(ba, pr);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            oq.setCorrect(-100);
        });

        assertEquals("Energy consumption cannot be negative or none!", exception.getMessage());
    }
}
