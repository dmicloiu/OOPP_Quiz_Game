package commons;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class UtilsTest {

    @Test
    public void incorrectEnergyGenTest() {
        long[] incorrectEnergies = Utils.generateIncorrectEnergy(1500);
        assertNotEquals(incorrectEnergies[0], 1500);
        assertNotEquals(incorrectEnergies[1], 1500);
    }

    @Test
    public void innerIncorrectTest() {
        Random rnd = new Random();
        long target1 = Utils.innerIncorrect(rnd, 1500, true);
        long target2 = Utils.innerIncorrect(rnd, 1500, false);
        assertNotEquals(target1, 1500);
        assertNotEquals(target2, 1500);
    }

    // Here a point could be made about the testing method being invalid.
    // Since in the next four method tests all possible prompts are tested,
    // I think they do not need to be checked in this method as well
    @Test
    public void promptGenTest() {
        Activity act = new Activity("id", "title", "source", "image_path", 1500);
        String target = Utils.generatePrompt(Type.STRING, act);

        assertNotNull(target);
    }

    @Test
    public void innerGenIntTest() {
        String target1 = Utils.innerGenerateInteger(0, "desc");
        String target2 = Utils.innerGenerateInteger(1, "desc");
        String target3 = Utils.innerGenerateInteger(2, "desc");

        assertEquals(target1, "How much energy (in Wh) does desc use?");
        assertEquals(target2, "How many Wh of energy does desc need?");
        assertEquals(target3, "If you are desc, how much energy in Wh would you need to use?");
    }

    @Test
    public void innerGenStringTest() {
        String target1 = Utils.innerGenerateString(0);
        String target2 = Utils.innerGenerateString(1);
        String target3 = Utils.innerGenerateString(2);

        assertEquals(target1, "What requires more energy?");
        assertEquals(target2, "What is more expensive?");
        assertEquals(target3,
            "Which of the following activities has the highest energy consumption?");
    }

    @Test
    public void innerGenAsMuchAsTest() {
        String target1 = Utils.innerGenerateAsMuchAs(0, "desc");
        String target2 = Utils.innerGenerateAsMuchAs(1, "desc");
        String target3 = Utils.innerGenerateAsMuchAs(2, "desc");

        assertEquals(target1, "Which of the following uses as much energy as desc?");
        assertEquals(target2, "What needs as many Wh of energy as desc?");
        assertEquals(target3, "If you have exactly as much energy as is used when desc, " +
            "which of the following can you do?");
    }

    @Test
    public void innerGenOpenTest() {
        String target1 = Utils.innerGenerateOpen(0, "desc");
        String target2 = Utils.innerGenerateOpen(1, "desc");
        String target3 = Utils.innerGenerateOpen(2, "desc");

        assertEquals(target1, "How much energy in Wh does desc use? Provide your best guess.");
        assertEquals(target2, "Try to guess how many Wh of energy desc requires.");
        assertEquals(target3,
            "Provide your best estimate of how much energy (in Wh) is used when desc");
    }
}
