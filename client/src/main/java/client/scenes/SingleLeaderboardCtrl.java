package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.LeaderboardEntry;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.util.*;

public class SingleLeaderboardCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private static ObservableList<LeaderboardEntry> playerList;
    ArrayList<XYChart.Series> series;
    CategoryAxis xAxis;
    CategoryAxis yAxis;
    @FXML
    private TableView<LeaderboardEntry> singleplayerTable;
    @FXML
    private TableColumn<LeaderboardEntry, String> currentPlace;
    @FXML
    private TableColumn<LeaderboardEntry, String> name;
    @FXML
    private TableColumn<LeaderboardEntry, String> highScore;
    @FXML
    private TableColumn<LeaderboardEntry, String> date;
    @FXML
    private Button back;
    @FXML
    private Button refresh;
    @FXML
    private Pane popupBarCharts;
    @FXML
    private BarChart<String, Integer> barChart;
    @FXML
    public Button play;
    @FXML
    private TextField username;
    @FXML
    private TextField score;
    @FXML
    private Text alertMessage;
    @FXML
    private Button littleBack;
    @FXML
    private Button deleteEntry;

    @Inject
    public SingleLeaderboardCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        xAxis = new CategoryAxis();
        yAxis = new CategoryAxis();
    }

    /**
     * Initializes the leaderboard and keeps it up to date whenever the scene is refreshed
     */
    public void initialize() {
        name.setCellValueFactory(
            p -> new SimpleStringProperty(p.getValue().getName())
        );
        highScore.setCellValueFactory(
            p -> new SimpleStringProperty("" + p.getValue().getScore())
        );
        currentPlace.setCellValueFactory(
            p -> new SimpleStringProperty("" + place(p.getValue()))
        );
        date.setCellValueFactory(
            p -> new SimpleStringProperty("" + p.getValue().getDate())
        );
        popupBarCharts.setDisable(true);
        popupBarCharts.setOpacity(0);
        littleBack.setDisable(true);
        littleBack.setOpacity(0);
        xAxis.setLabel("Name");
        yAxis.setLabel("High Score");
    }

    /**
     * Shows a bar chart representation of the games played,
     * when the player is the first one with a specific name
     */
    @SuppressWarnings("unchecked")
    public void showBarCharts() {
        barChart.getData().clear();
        popupBarCharts.setOpacity(1);
        popupBarCharts.setDisable(false);
        littleBack.setDisable(false);
        littleBack.setOpacity(1);
        back.setOpacity(0);
        back.setDisable(true);
        deleteEntry.setOpacity(0);
        deleteEntry.setDisable(true);
        username.setOpacity(0);
        username.setDisable(true);
        score.setOpacity(0);
        score.setDisable(true);
        refresh.setDisable(true);
        refresh.setOpacity(0);
        singleplayerTable.setOpacity(0);
        var leaderboardEntries = server.getAllLeaderboardEntries();
        playerList = FXCollections.observableList(leaderboardEntries);
        HashSet<String> names = new HashSet<>();
        series = new ArrayList<>();
        for (LeaderboardEntry p : playerList) {
            if (!names.contains(p.getName())) {
                series.add(new XYChart.Series());
                series.get(series.size() - 1).setName(p.getName());
                series.get(series.size() - 1).getData().add(
                        new XYChart.Data(p.getName(), p.getScore()));
            }
            names.add(p.getName());
        }
        List<String> n = new ArrayList<>(names);
        xAxis.setCategories(FXCollections.observableList(n));
        for (int i = 0; i < series.size(); i++)
            barChart.getData().add(series.get(i));
    }

    /**
     * Goes back from the bar charts
     */
    public void back() {
        popupBarCharts.setDisable(true);
        popupBarCharts.setOpacity(0);
        deleteEntry.setOpacity(1);
        deleteEntry.setDisable(false);
        back.setOpacity(1);
        back.setDisable(false);
        refresh.setDisable(false);
        refresh.setOpacity(1);
        singleplayerTable.setOpacity(1);
        username.setOpacity(1);
        username.setDisable(false);
        score.setOpacity(1);
        score.setDisable(false);
    }

    /**
     * Starts another single player game
     */
    public void play() {
        mainCtrl.mainMenuCtrl.singleMode();
    }

     /**
      *  This method is called whenever the user wants to delete all entries
      * with a matching username and score.
      */
    public void deleteEntryByNameAndScore() {
        alertMessage.setVisible(false);
        if (username.getText().isBlank()) {
            alertMessage.setVisible(true);
            try {
                int i = Integer.parseInt(score.getText());
            } catch (NumberFormatException e) {
                alertMessage.setText("Invalid username and score.");
                return;
            }
            alertMessage.setText("Invalid username.");
            return;
        }
        try {
            int i = Integer.parseInt(score.getText());
        } catch (NumberFormatException e) {
            alertMessage.setVisible(true);
            alertMessage.setText("Invalid score.");
        }
        boolean indicator = server.deleteEntriesByNameAndScore(
            username.getText(),
            Integer.parseInt(score.getText())
        );
        alertMessage.setVisible(true);
        if (indicator) alertMessage.setText("Successfully deleted.");
        else alertMessage.setText("No entries found!");
        refresh();
    }

    /**
     * finds a leaderboardEntry in the sorted observableList and with that determines
     * the ranking place that leaderboardEntry corresponds to.
     * @param p - leaderboardEntry
     * @return the ranking number of the player in the leaderboardEntry
     */
    public int place(LeaderboardEntry p) {
        int place = 0;
        if (playerList.isEmpty()) {
            return place;
        } else {
            for (int i = 0; i < playerList.size(); i++) {
                if (p.getName().equals(playerList.get(i).getName())) {
                    if (p.getScore() == playerList.get(i).getScore()) {
                        if (p.getDate().equals(playerList.get(i).getDate())) {
                            place = i + 1;
                            break;
                        }
                    }
                }
            }
            return place;
        }
    }

    /**
     * refreshes the scene and more specifically updates the list of leaderboardEntries
     * in the observableList and adds the list to the leaderboard.
     */
    public void refresh() {
        var leaderboardEntries = server.getAllLeaderboardEntries();
        playerList = FXCollections.observableList(leaderboardEntries);
        singleplayerTable.setItems(playerList);
    }

    /**
     * Method to return the player to the main menu.
     */
    public void goBack() {
        mainCtrl.showMainMenu();
    }

    /**
     * Allows the player to use short-cuts like pressing escape to quit the scene.
     * @param e - The key that is being pressed.
     */
    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ESCAPE:
                goBack();
                break;
            default:
                break;
        }
    }
}
