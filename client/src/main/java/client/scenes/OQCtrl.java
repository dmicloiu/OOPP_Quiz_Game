package client.scenes;

import client.utils.ServerUtils;
import commons.*;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class OQCtrl extends QuestionCtrl {

    @FXML
    private Text question;
    @FXML
    private ImageView image;
    @FXML
    private TextField answerField;
    @FXML
    private Button submit;
    @FXML
    private Text warning;

    private long answer;

    /**
     * Constructs and initializes an OQCtrl with the given attributes
     *
     * @param server - the server for a corresponding game
     * @param mainCtrl - the MainCtrl for a corresponding game
     */
    @Inject
    public OQCtrl(ServerUtils server, MainCtrl mainCtrl) {
        super(server, mainCtrl);
    }

    /**
     * This method shows the Question by setting the prompt of the question
     * it also calls methods for setting the answers
     */
    public void showQuestion() {
        try {
            refresh();
            super.showQuestion();
            wrongAnsJoker.setDisable(true);
            OpenQuestion currentQuestion = (OpenQuestion) playerCtrl.getCurrentQuestion();
            question.setText(currentQuestion.getPrompt());
            image.setImage(new Image(server
                    .getImage(currentQuestion.getBaseActivity().getImage_path())));
            super.currentQuestion = question.getText();
        } catch (Exception e) { }
    }

    /**
     * Method called when showing a new Question to reset the answerField
     * and to set the ending and warning not visible
     */
    public void refresh() {
        ending.setVisible(false);
        warning.setVisible(false);
        answerField.setPromptText("ANSWER GOES HERE");
        answerField.setText(null);
    }

    /**
     * Method called when the player pressed the submit button
     */
    public void submit() {
        try {
            answer = Integer.parseInt(answerField.getText());
            warning.setVisible(false);
            submit.setDisable(true);
            optionSelected();
        } catch (NumberFormatException e) {
            warning.setVisible(true);
        }
    }

    /**
     * Stops the first timeline after it is finished and starts the second timeline. It also
     * disables the player form entering an answer in this time.
     */
    public void startSecondTimer() {
        super.startSecondTimer();
        transitionBetweenQuestions();
    }
    
    /**
     * Method responsible for the transition between questions
     * - it disables all the buttons
     * - it calls the method for showing the correct and incorrect answers
     * - updates the score of the player accordingly
     */
    public void transitionBetweenQuestions() {
        disable();
        showEnding();
        awardPoints();
        isTransition = true;
    }
    /**
     * Awards points based on the answers the player had
     */
    @Override
    public void awardPoints() {
        if (answer > 0) {
            ScoreCalculator calc = new ScoreCalculator(
                (OpenQuestion) playerCtrl.getCurrentQuestion(), answer, timeTaken);
            currentQuestionScore = calc.calcScore();
            if (doublePoints) currentQuestionScore *= 2;
        } else {
            currentQuestionScore = 0;
        }
        super.awardPoints();
    }

    /**
     * Shows the ending message based on the answer the player had (correct/wrong)
     */
    private void showEnding() {
        OpenQuestion currentQuestion = (OpenQuestion) playerCtrl.getCurrentQuestion();
        long correctAnswer = currentQuestion.getCorrect();
        if (answer == correctAnswer) {
            ending.setText("You were right! Good job!");
            ending.setFill(Color.GREEN);
            ending.setVisible(true);
        } else {
            ending.setText("The correct answer was " + correctAnswer + "!");
            ending.setFill(Color.RED);
            ending.setVisible(true);
        }
        awardPoints();
    }

    /**
     * Disables everything on screen to be clickable except the go back button,
     * the emojis and the jokers.
     */
    public void disable() {
        warning.setVisible(false);
        answerField.setDisable(true);
        submit.setDisable(true);
    }

    /**
     * Enables everything on screen to be clickable.
     */
    public void enable() {
        answerField.setDisable(false);
        submit.setDisable(false);
        super.enable();
    }

    /**
     * Called when a key is pressed
     *
     * @param e - the key that was pressed
     */
    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ESCAPE:
                showPopUp();
                break;
            case ENTER:
                submit();
            default:
                break;
        }
    }
}
