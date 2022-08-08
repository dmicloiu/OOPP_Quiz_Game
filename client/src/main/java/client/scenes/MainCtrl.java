/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import client.Main;
import client.utils.LongPollingThread;
import commons.Activity;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Pair;

import java.io.File;

public class MainCtrl {

    private static Stage primaryStage;
    private LongPollingThread thread;
    public static String lastGamePlayed;

    private SplashScreenCtrl splashCtrl;
    private Scene splash;

    public MainMenuCtrl mainMenuCtrl;
    public Scene mainmenu;

    private static OQCtrl oqCtrl;
    private  static Scene openQuestion;

    private static SingleLeaderboardCtrl singleLeaderboardCtrl;
    private static Scene singleLeaderboard;

    private static MultiLeaderboardCtrl multiLeaderboardCtrl;
    private static Scene multiLeaderboard;

    private static WaitingRoomCtrl waitingRoomCtrl;
    private Scene waitingRoom;

    private static MCQuestionCtrl mcQuestionCtrl;
    private static Scene mcQuestion;

    private static FinalScreenCtrl finalScreenCtrl;
    private static Scene finalScreen;

    public static SinglePlayerCtrl singlePlayerCtrl;
    private Scene singlePlayer;
    
    public static MultiPlayerCtrl multiPlayerCtrl;
    private Scene multiPlayer;
    
    private static ActivitiesOverviewCtrl activitiesOverviewCtrl;
    private Scene activitiesOverview;
    
    private static ActivitiesEditCtrl activitiesEditCtrl;
    private Scene activitiesEdit;

    private PopupCtrl popupCtrl;
    private Scene popupScene;


    public void initialize(Stage primaryStage,
                           Pair<SplashScreenCtrl, Parent> splash,
                           Pair<MainMenuCtrl, Parent> mainmenu,
                           Pair<SingleLeaderboardCtrl, Parent> singleLeaderboard,
                           Pair<MultiLeaderboardCtrl, Parent> multiLeaderboard,
                           Pair<WaitingRoomCtrl, Parent> waitingRoom,
                           Pair<MCQuestionCtrl, Parent> mcQuestion,
                           Pair<OQCtrl, Parent> openQuestion,
                           Pair<FinalScreenCtrl, Parent> finalScreen,
                           Pair<SinglePlayerCtrl, Parent> singlePlayer, 
                           Pair<MultiPlayerCtrl, Parent> multiPlayer,
                           Pair<ActivitiesOverviewCtrl, Parent> activitiesOverview,
                           Pair<ActivitiesEditCtrl, Parent> activitiesEdit,
                           Pair<PopupCtrl, Parent> popup) {

        this.primaryStage = primaryStage;
        this.splashCtrl = splash.getKey();
        this.splash = new Scene(splash.getValue());

        this.mainMenuCtrl = mainmenu.getKey();
        this.mainmenu = new Scene(mainmenu.getValue());

        this.singleLeaderboardCtrl = singleLeaderboard.getKey();
        this.singleLeaderboard = new Scene(singleLeaderboard.getValue());

        this.multiLeaderboardCtrl = multiLeaderboard.getKey();
        this.multiLeaderboard = new Scene(multiLeaderboard.getValue());

        this.waitingRoomCtrl = waitingRoom.getKey();
        this.waitingRoom = new Scene(waitingRoom.getValue());

        this.mcQuestionCtrl = mcQuestion.getKey();
        this.mcQuestion = new Scene(mcQuestion.getValue());

        this.oqCtrl = openQuestion.getKey();
        this.openQuestion = new Scene(openQuestion.getValue());

        this.finalScreenCtrl = finalScreen.getKey();
        this.finalScreen = new Scene(finalScreen.getValue());

        this.singlePlayerCtrl = singlePlayer.getKey();
        this.singlePlayer = new Scene(singlePlayer.getValue());

        this.multiPlayerCtrl = multiPlayer.getKey();
        this.multiPlayer = new Scene(multiPlayer.getValue());

        this.activitiesOverviewCtrl = activitiesOverview.getKey();
        this.activitiesOverview = new Scene(activitiesOverview.getValue());
        
        this.activitiesEditCtrl = activitiesEdit.getKey();
        this.activitiesEdit = new Scene(activitiesEdit.getValue());

        this.popupCtrl = popup.getKey();
        this.popupScene = new Scene(popup.getValue());
        
        loadPreferences();
        showSplash();
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event -> {
            event.consume();
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(primaryStage);
            dialog.setScene(popupScene);
            dialog.getIcons().add(Main.getIcon());
            dialog.show();
            popupCtrl.setDialog(dialog);
        });
    }

    /**
     * All the methods below do almost exactly the same. They make it, so we can switch scenes
     * with a single method call. Some of them also allow for shortcuts to be used like ESCAPE.
     */

    public void showSplash() {
        primaryStage.setTitle("Game: Splash");
        primaryStage.setScene(splash);
        splashCtrl.initialise();
        splash.setOnKeyPressed(e -> splashCtrl.keyPressed(e));
    }

    public void showMainMenu() {
        primaryStage.setTitle("Game: Main Menu");
        primaryStage.setScene(mainmenu);
    }

    public void showWaitingRoom() {
        waitingRoomCtrl.initializeScene();
        primaryStage.setTitle("Game: waiting room");
        primaryStage.setScene(waitingRoom);
        waitingRoomCtrl.refresh();
        waitingRoom.setOnKeyPressed(e -> waitingRoomCtrl.keyPressed(e));
    }

    public static Runnable showOpenQuestion() {
        return new Runnable() {
            @Override
            public void run() {
                primaryStage.setTitle("Game: open question");
                primaryStage.setScene(openQuestion);
                oqCtrl.showQuestion();
                openQuestion.setOnKeyPressed(e -> oqCtrl.keyPressed(e));
            }
        };
    }

    /**
     * Shows the leaderboard for the single player games
     * @param from a String that represents whether the leaderboard
     *             is shown from the main menu or after a single player game
     */
    public static void showSingleLeaderboard(String from) {
        singleLeaderboardCtrl.refresh();
        primaryStage.setTitle("Game: single leaderboard");
        primaryStage.setScene(singleLeaderboard);
        if (from.equals("menu")) {
            singleLeaderboardCtrl.play.setDisable(true);
            singleLeaderboardCtrl.play.setOpacity(0);
        } else {
            singleLeaderboardCtrl.play.setDisable(false);
            singleLeaderboardCtrl.play.setOpacity(1);
        }
        singleLeaderboard.setOnKeyPressed(e -> singleLeaderboardCtrl.keyPressed(e));
    }
    
    public void showFinalMultiLeaderboard() {
        multiLeaderboardCtrl.initializeFinalScene();
        primaryStage.setTitle("Game: multi leaderboard");
        primaryStage.setScene(multiLeaderboard);
        multiLeaderboard.setOnKeyPressed(e -> multiLeaderboardCtrl.keyPressed(e));
    }

    public static void showHalfMultiLeaderBoard() {
        multiLeaderboardCtrl.initializeHalfScene();
        primaryStage.setTitle("Game: multi leaderboard");
        primaryStage.setScene(multiLeaderboard);
        multiLeaderboard.setOnKeyPressed(e -> multiLeaderboardCtrl.keyPressed(e));
    }
    /**
     * Starts the thread that is looking for the updates
     */
    public void startThread() {
        thread = new LongPollingThread(waitingRoomCtrl.server, waitingRoomCtrl,
                mcQuestionCtrl, multiPlayerCtrl);
        thread.start();
    }

    public static Runnable showMCQuestion() {
        return new Runnable() {
            @Override
            public void run() {
                primaryStage.setTitle("Game: multiple choice question");
                primaryStage.setScene(mcQuestion);
                mcQuestionCtrl.showQuestion();
                mcQuestion.setOnKeyPressed(e -> mcQuestionCtrl.keyPressed(e));
            }
        };
    }

    /**
     * Shows the final screen with the number of points won by the player
     */
    public static void showFinalScreen() {
        primaryStage.setTitle("Game: final screen");
        primaryStage.setScene(finalScreen);
        finalScreen.setOnKeyPressed(e -> finalScreenCtrl.keyPressed(e));
        PlayerCtrl playerCtrl = lastGamePlayed.equals("multiplayer") ?
                multiPlayerCtrl : singlePlayerCtrl;
        finalScreenCtrl.setEndText("Congratulations! You scored " +
                playerCtrl.getScore() + " points!");
        finalScreenCtrl.timeline.play();
    }

    /**
     * Prepares and shows the {@code activitiesOverview} {@link Scene}
     */
    public void showActivitiesOverview() {
        activitiesOverviewCtrl.initializeScene();
        primaryStage.setTitle("Activities");
        primaryStage.setScene(activitiesOverview);
    }

    /**
     * Prepares and shows the {@code activitiesEdit} {@link Scene} using the given activity
     * 
     * @param activity The activity to edit in the {@code activitiesEdit} Scene,
     *                 or null if creating a new activity
     * 
     */
    public void showActivitiesEdit(Activity activity) {
        primaryStage.setTitle("Edit activity");
        primaryStage.setScene(activitiesEdit);
        activitiesEditCtrl.initialise(activity);
    }

    /**
     * Calling the method in SinglePlayerCtrl to initialise the game
     */
    public void initialiseGame(String name) {
        singlePlayerCtrl.initialise(name);
        lastGamePlayed = "singleplayer";
    }

    /**
     * Calling the method in MultiPlayerCtrl to initialise the game
     */
    public void initialiseMultiGame(String name, int gameID) {
        multiPlayerCtrl.initialise(name, gameID);
    }

    /**
     * Starts a multiplayer game when a player presses start in the waiting room
     */
    public void startMultiGame() {
        thread.setPlayerCtrl(multiPlayerCtrl);
        multiPlayerCtrl.nextQuestion();
        lastGamePlayed = "multiplayer";
    }

    /**
     * Opens a {@link FileChooser} for the user to choose an image
     * 
     * @return the file that the user selected or {@code null} if the user exits the FileChooser
     */
    public File selectImageFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Images (*.png, *.gif, *.jpeg, *.jpg)",
                        "*.png", "*.gif", "*.jpeg", "*.jpg")
        );
        try {
            return fileChooser.showOpenDialog(primaryStage).getAbsoluteFile();
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Loads all locally stored preferences for the user
     */
    public void loadPreferences() {
        mainMenuCtrl.loadPreferences();
        splashCtrl.loadPreferences();
    }
}