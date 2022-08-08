package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.*;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;

import java.util.Objects;
import java.util.Random;

/**
 * A controller for the multiple choice question scene.
 */

public class MCQuestionCtrl extends QuestionCtrl {

    @FXML
    private ImageView imageA;
    @FXML
    private ImageView imageB;
    @FXML
    private ImageView imageC;

    @FXML
    private Button optionA;
    @FXML
    private Button optionB;
    @FXML
    private Button optionC;
    private int selected = 0; // a way to find out which button was selected

    private MCQuestion currentQuestion;
    private Activity[] activities;
    private Activity correct;
    private Random rand = new Random();

    /**
     * Constructs and initializes a MCQuestionCtrl
     *
     * @param server - the server
     * @param mainCtrl - the main controller of the JavaFX application
     */
    @Inject
    public MCQuestionCtrl(ServerUtils server, MainCtrl mainCtrl) {
        super(server, mainCtrl);
    }

    /**
     * Method for setting the answers of a MCQuestion
     * ONLY used for Type STRING and ASMUCHAS
     *
     * @param activities activities corresponding to the answers of the question
     */
    private void setAnswersStrings(Activity[] activities) {
        optionA.setText(activities[0].getTitle());
        optionB.setText(activities[1].getTitle());
        optionC.setText(activities[2].getTitle());

        imageA.setImage(new Image(server.getImage(activities[0].getImage_path())));
        imageB.setImage(new Image(server.getImage(activities[1].getImage_path())));
        imageC.setImage(new Image(server.getImage(activities[2].getImage_path())));
    }

    /**
     * Stops the first timeline after it is finished and starts the second timeline. It also
     * disables the player form entering an answer in this time.
     */
    public void startSecondTimer() {
        transitionBetweenQuestions();
        super.startSecondTimer();
    }

    /**
     * This method shows the MCQuestion by setting the prompt of the question
     * it also calls methods for setting the answers
     */
    public void showQuestion() {
        refresh();
        super.showQuestion();
        try {
            MCQuestion currentQuestion = (MCQuestion) playerCtrl.getCurrentQuestion();
            Activity[] answers = currentQuestion.getAnswers();
            question.setText(currentQuestion.getPrompt());
            if (currentQuestion.getType() == Type.INTEGER) {
                setAnswersInteger(answers);
            } else {
                setAnswersStrings(answers);
            }
            super.currentQuestion = question.getText();
        } catch (Exception e) { }
    }

    /**
     * Setter for the answer selected by the player
     * chosen = 0 if the player did not press any answer
     * chosen = 1 if the player choose optionA button
     * chosen = 2 if the player choose optionB button
     * chosen = 3 if the player choose optionC button
     * @param chosen the chosen answer
     */
    public void setChosen(int chosen) {
        this.selected = chosen;
    }
    /**
     * Method for setting the answers of a MCQuestion
     * ONLY used for Type INTEGER
     *
     * @param activities activities corresponding to the answers of the question
     */
    private void setAnswersInteger(Activity[] activities) {
        optionA.setText(String.valueOf(activities[0].getConsumption_in_wh()));
        optionB.setText(String.valueOf(activities[1].getConsumption_in_wh()));
        optionC.setText(String.valueOf(activities[2].getConsumption_in_wh()));
        Image icon = new Image(Objects
                .requireNonNull(getClass()
                        .getResourceAsStream("/imgs/questionmark.png")));
        imageA.setImage(icon);
        imageB.setImage(icon);
        imageC.setImage(icon);
    }

    /**
     * Enables everything on screen to be clickable.
     */
    public void enable() {
        optionA.setDisable(false);
        optionB.setDisable(false);
        optionC.setDisable(false);
        super.enable();
    }

    /**
     * Disables everything on screen to be clickable except the go back button,
     * the emojis and the jokers.
     */
    public void disable() {
        optionA.setDisable(true);
        optionB.setDisable(true);
        optionC.setDisable(true);
        wrongAnsJoker.setDisable(true);
    }

    /**
     * Sets the buttons to the normal colors and makes the ending not visible.
     * It is called when showing a new question
     */
    public void refresh() {
        ending.setVisible(false);
        optionA.setStyle("-fx-background-color: #FFFFFFFF; ");
        optionB.setStyle("-fx-background-color: #FFFFFFFF; ");
        optionC.setStyle("-fx-background-color: #FFFFFFFF; ");
    }

    /**
     * Method called when the player pressed button optionA
     */
    public void optionASelected() {
        disable();
        optionSelected();
        optionA.setStyle("-fx-background-color: #72b4ff; ");
        setChosen(1);
    }

    /**
     * Method called when the player pressed button optionB
     */
    public void optionBSelected() {
        disable();
        optionSelected();
        optionB.setStyle("-fx-background-color: #72b4ff; ");
        setChosen(2);
    }

    /**
     * Method called when the player pressed button optionC
     */
    public void optionCSelected() {
        disable();
        optionSelected();
        optionC.setStyle("-fx-background-color: #72b4ff; ");
        setChosen(3);
    }

    /**
     * Method responsible for the transition between questions
     * - it disables all the buttons
     * - it calls the method for showing the correct and incorrect answers
     * - updates the score of the player accordingly
     */
    public void transitionBetweenQuestions() {
        isTransition = true;
        disable(); // put to disable the buttons if the player has not answered
        showAnswers();
        awardPoints();
    }

    /**
     * Awards points based on the answers the player had
     */
    @Override
    public void awardPoints() {
        MCQuestion q = (MCQuestion) playerCtrl.getCurrentQuestion();
        ScoreCalculator calc;
        if (selected == 0) calc = new ScoreCalculator(q, null, timeTaken);
        else calc = new ScoreCalculator(q, q.getAnswers()[selected - 1], timeTaken);
        currentQuestionScore = calc.calcScore();
        if (doublePoints) {
            currentQuestionScore *= 2;
        }
        super.awardPoints();
    }

    /**
     * Shows a wrong answer after the player presses the wrongAns joker
     */
    @FXML
    private void showWrongAnswer() {
        currentQuestion = (MCQuestion) playerCtrl.getCurrentQuestion();
        activities = currentQuestion.getAnswers();
        correct = currentQuestion.getCorrect();
        int choice = rand.nextInt(2);

        if (correct.equals(activities[0])) {
            if (choice == 0) {
                optionB.setDisable(true);
                optionB.setStyle("-fx-background-color: red; ");
            } else {
                optionC.setDisable(true);
                optionC.setStyle("-fx-background-color: red; ");
            }
        } else if (correct.equals(activities[1])) {
            if (choice == 0) {
                optionA.setDisable(true);
                optionA.setStyle("-fx-background-color: red; ");
            } else {
                optionC.setDisable(true);
                optionC.setStyle("-fx-background-color: red; ");
            }
        } else {
            if (choice == 0) {
                optionA.setDisable(true);
                optionA.setStyle("-fx-background-color: red; ");
            } else {
                optionB.setDisable(true);
                optionB.setStyle("-fx-background-color: red; ");
            }
        }
        wrongAnsJoker.setDisable(true);
        shownAnswerJoker = true;
    }
    /**
     * Shows the correct and incorrect answers and it changes the colors
     * of the buttons accordingly
     */
    private void showAnswers() {
        currentQuestion = (MCQuestion) playerCtrl.getCurrentQuestion();
        activities = currentQuestion.getAnswers();
        correct = currentQuestion.getCorrect();

        int correctOption = 0;
        if (correct.equals(activities[0])) {
            correctOption = 1;
            optionA.setStyle("-fx-background-color: green; ");
        } else {
            optionA.setStyle("-fx-background-color: red; ");
        }

        if (correct.equals(activities[1])) {
            correctOption = 2;
            optionB.setStyle("-fx-background-color: green; ");
        } else {
            optionB.setStyle("-fx-background-color: red; ");
        }

        if (correct.equals(activities[2])) {
            correctOption = 3;
            optionC.setStyle("-fx-background-color: green; ");
        } else {
            optionC.setStyle("-fx-background-color: red; ");
        }


        if (selected == 0) {
            ending.setText("Try to answer next time!");
            ending.setFill(Color.DARKBLUE);
            ending.setVisible(true);
        } else if (selected == correctOption) {
            ending.setText("You were right! Great job!");
            ending.setFill(Color.GREEN);
            ending.setVisible(true);
        } else {
            ending.setText("You were wrong! Better luck next time!");
            ending.setFill(Color.RED);
            ending.setVisible(true);
        }
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
            default:
                break;
        }
    }
}


