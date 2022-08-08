package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Activity;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;

public class ActivitiesEditCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    /**
     * Whether the {@code Activity} is being created ({@code true}), 
     * or the activity is being edited ({@code false}).
     */
    private boolean isNew;

    /**
     * All the field for creating or editing an {@link Activity}.
     */
    @FXML
    TextField id;
    @FXML
    TextField title;
    @FXML
    TextField source;
    @FXML
    TextField consumption;
    @FXML
    ImageView image;
    @FXML
    Text statusText;

    File imageFile;
    Activity currAct;

    /**
     * Controller for the ActivitiesEdit scene.
     *
     * @param server the controller for all interactions with the server
     * @param mainCtrl the main controller for all the scenes
     */
    @Inject
    public ActivitiesEditCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Initialises the ActivitiesEdit scene with the given activity if specified,
     * or {@code null} when creating a new {@link Activity}.
     *
     * @param activity the activity to edit, or {@code null} if creating a new one
     */
    public void initialise(Activity activity) {
        if (activity == null) {
            isNew = true;
            id.setText("");
            title.setText("");
            source.setText("");
            consumption.setText("");
            image.setImage(null);
            return;
        }
        isNew = false;
        id.setText(activity.getId());
        title.setText(activity.getTitle());
        source.setText(activity.getSource());
        consumption.setText("" + activity.getConsumption_in_wh());
        image.setImage(new Image(server.getImage(activity.getImage_path())));

        currAct = activity;
    }

    /**
     * Sets the status text on the scene to a given String.
     *
     * @param status the text which the status bar should display
     */
    public void setStatus(String status) {
        statusText.setVisible(true);
        statusText.setText(status);
    }

    /**
     * Functionality for the upload button, to allow a user to upload
     * a (new) {@link Image} for a question.
     */
    public void upload() {
        File file = mainCtrl.selectImageFile();
        if (file == null) return;

        try {
            image.setImage(new Image(file.toURI().toString()));
            imageFile = file;
            setStatus("");
        } catch (Exception e) {
            e.printStackTrace();
            setStatus("There was an issue while setting the image");
        }
    }

    /**
     * Saves the activity as it is currently edited.
     */
    public void saveActivity() {
        if (id.getText().isBlank()) {
            setStatus("The id may not be empty");
            return;
        }
        if (title.getText().isBlank()) {
            setStatus("The title may not be empty");
            return;
        }
        if (title.getText().length() > 140) {
            setStatus("The title may not be longer than 140 characters");
            return;
        }
        if (title.getText().startsWith(" ")) {
            setStatus("The title should not start with a space");
            return;
        }
        if (title.getText().contains(".") 
                || title.getText().contains("?") 
                || title.getText().contains("!")) {
            setStatus("The title should contain punctuation");
            return;
        }
        if (!title.getText().split(" ")[0].matches("\\w+ing$")) {
            setStatus("The title should be in the past participle (first word ending with '-ing')");
            return;
        }
        if (source.getText().isBlank()) {
            setStatus("The source may not be empty");
            return;
        }
        if (consumption.getText().isBlank()) {
            setStatus("The consumption may not be empty");
            return;
        }
        try {
            Long.parseLong(consumption.getText());
        } catch (NumberFormatException e) {
            setStatus("Please provide a 'long' for the consumption");
            return;
        }
        if (Long.parseLong(consumption.getText()) <= 0) {
            setStatus("Please please provide a positive consumption");
            return;
        }
        if (image == null || image.getImage() == null || image.getImage().getHeight() == 0) {
            setStatus("Please select an image");
            return;
        }
        if (image.getImage().getHeight() < 200 || image.getImage().getWidth() < 200) {
            setStatus("Please select an image at least 200x200 pixels");
            return;
        }

        Activity newAct;
        if (isNew) {
            String path = id.getText().split("-")[0].replace("/", "") + "/" + imageFile.getName();
            newAct = new Activity(id.getText(), title.getText(), source.getText(),
                    null, Long.parseLong(consumption.getText()));
            try {
                server.addFile(imageFile, path);
                newAct.setImage_path(path);
            } catch (IOException e) {
                setStatus("Something went wrong when searching for the image file");
                return;
            }
        } else {
            String path = (imageFile == null) ? 
                    currAct.getImage_path() 
                    :
                    currAct.getImage_path().split("/")[0] + "/" + imageFile.getName();
            
            newAct = new Activity(id.getText(), title.getText(), source.getText(),
                    path, Long.parseLong(consumption.getText()));
            newAct.setActivityID(currAct.getActivityID());
            
            if (imageFile != null) {
                try {
                    server.addFile(imageFile, path);
                    newAct.setImage_path(path);
                } catch (IOException e) {
                    setStatus("Something went wrong when searching for the image file");
                    return;
                }
            }
        }
        server.addActivity(newAct);
        setStatus("Activity saved");
    }

    /**
     * Goes back to the activities overview without saving.
     */
    public void goBack() {
        statusText.setVisible(false);
        mainCtrl.showActivitiesOverview();
    }
}
