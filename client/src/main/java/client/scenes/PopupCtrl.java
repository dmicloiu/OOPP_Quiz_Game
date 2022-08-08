package client.scenes;

import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * Controller that controls the close popup
 */
public class PopupCtrl {

    private Stage dialog;

    /**
     * Closes the application
     */
    public void close() {
        Platform.exit();
    }

    /**
     * Closes the popup {@link Stage}
     */
    public void stay() {
        dialog.close();
    }

    /**
     * @param dialog Links the given {@link Stage} to the popupCtrl to 
     * close when {@code stay()} is called
     */
    public void setDialog(Stage dialog) {
        this.dialog = dialog;
    }
}
