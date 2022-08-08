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
package client;

import client.scenes.*;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

import static com.google.inject.Guice.createInjector;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    private static Image icon;

    public static void main(String[] args) throws URISyntaxException, IOException {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        var splash = FXML.load(SplashScreenCtrl.class, "client", "scenes", "SplashScreen.fxml");
        var mainmenu = FXML.load(MainMenuCtrl.class, 
                "client", "scenes", "MainMenu.fxml");
        var singleLeaderboard = FXML.load(SingleLeaderboardCtrl.class,
                "client", "scenes", "SingleLeaderboard.fxml");
        var multiLeaderboard = FXML.load(MultiLeaderboardCtrl.class,
                "client", "scenes", "MultiLeaderboard.fxml");
        var waitingRoom = FXML.load(WaitingRoomCtrl.class,
                "client", "scenes", "WaitingRoom.fxml");
        var mcQuestion = FXML.load(MCQuestionCtrl.class,
                "client", "scenes", "MCQuestion.fxml");
        var openQuestion = FXML.load(OQCtrl.class, "client", "scenes", "openQuestion.fxml");
        var finalScreen = FXML.load(FinalScreenCtrl.class,
                "client", "scenes", "FinalScreen.fxml");

        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        var singlePlayerCtrl = FXML.load(SinglePlayerCtrl.class,
                "client", "scenes", "SinglePlayer.fxml");
        var multiPlayerCtrl = FXML.load(MultiPlayerCtrl.class,
                "client", "scenes", "MultiPlayer.fxml");
        var ActivitiesOverview = FXML.load(ActivitiesOverviewCtrl.class,
                "client", "scenes", "ActivitiesOverview.fxml");
        var ActivitiesEdit = FXML.load(ActivitiesEditCtrl.class,
                "client", "scenes", "ActivitiesEdit.fxml");
        var Popup = FXML.load(PopupCtrl.class,
                "client", "scenes", "Popup.fxml");
        icon = new Image(Objects
                .requireNonNull(getClass()
                        .getResourceAsStream("/imgs/questionmark.png")));
        primaryStage.getIcons().add(icon);

        mainCtrl.initialize(primaryStage, splash, mainmenu, singleLeaderboard, multiLeaderboard,
                waitingRoom, mcQuestion, openQuestion, finalScreen, singlePlayerCtrl,
                multiPlayerCtrl, ActivitiesOverview, ActivitiesEdit, Popup);
    }

    public static Image getIcon() {
        return icon;
    }
}