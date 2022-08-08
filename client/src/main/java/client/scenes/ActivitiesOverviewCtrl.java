package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Activity;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;

public class ActivitiesOverviewCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private static ObservableList<Activity> activities;

    @FXML
    TableView<Activity> tableView;
    @FXML
    TableColumn<Activity, String> idCol;
    @FXML
    TableColumn<Activity, String> titleCol;
    @FXML
    TableColumn<Activity, String> consumptionCol;
    @FXML
    TableColumn<Activity, String> sourceCol;
    @FXML
    TableColumn<Activity, String> imageCol;

    /**
     * Controller for the ActivitiesOverview scene.
     *
     * @param server the controller for all interactions with the server
     * @param mainCtrl the main controller for all the scenes
     */
    @Inject
    public ActivitiesOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Refreshes the context of the {@link TableView}
     * to reflect the newest changes to the activities.
     */
    public void refresh() {
        List<Activity> actsAL = server.getAllActivities();
        activities = FXCollections.observableArrayList(actsAL);
        tableView.setItems(activities);
    }

    /**
     * Initialises the {@link TableColumn}s with the constructor for their {@link TableCell}s.
     */
    public void initializeScene() {
        idCol.setCellValueFactory(
                p -> new SimpleStringProperty(p.getValue().getId())
        );
        titleCol.setCellValueFactory(
                p -> new SimpleStringProperty(p.getValue().getTitle())
        );
        consumptionCol.setCellValueFactory(
                p -> new SimpleStringProperty("" + p.getValue().getConsumption_in_wh())
        );
        sourceCol.setCellValueFactory(
                p -> new SimpleStringProperty(p.getValue().getSource())
        );
        imageCol.setCellFactory(
                param -> {
                    ImageView imageView = new ImageView();
                    imageView.setFitHeight(50);
                    imageView.setFitWidth(50);

                    TableCell<Activity, String> tableCell = new TableCell<>() {
                        @Override
                        public void updateItem(String activity, boolean isEmpty) {
                            if (imageCol != null) {
                                imageView.setImage(new Image(server.getImage(activity)));
                            }
                        }
                    };

                    tableCell.setGraphic(imageView);
                    return tableCell;
                }
        );
        imageCol.setCellValueFactory(
                new PropertyValueFactory<>("image_path")
        );

        refresh();
    }

    /**
     * Redirects to the edit activity page, for the selected activity.
     * If no activity is selected, nothing happens.
     */
    public void editActivity() {
        if (tableView.getSelectionModel().getSelectedItem() == null) return;
        mainCtrl.showActivitiesEdit(tableView.getSelectionModel().getSelectedItem());
    }

    public void addActivity() {
        mainCtrl.showActivitiesEdit(null);
    }

    public void deleteActivity() {
        if (tableView.getSelectionModel().getSelectedItem() == null) return;
        server.deleteActivity(tableView.getSelectionModel().getSelectedItem());
        System.out.println("Activity deleted from DB");
        refresh();
    }

    /**
     * Goes back to the main menu.
     */
    public void goBack() {
        mainCtrl.showMainMenu();
    }
}
