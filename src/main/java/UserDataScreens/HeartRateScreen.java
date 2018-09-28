package UserDataScreens;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Error: Druids stole description
 */

public class HeartRateScreen {

    /**
     * This method starts the screen, where the user could enter his heart rate data
     *
     * @param owner the "owner" stage of the new Window
     */
    public static void showHearthRateScreen(Stage owner) {
        GridPane createBPMScreen = new GridPane();
        createBPMScreen.setPadding(new Insets(10));
        createBPMScreen.setVgap(8);
        createBPMScreen.setHgap(10);

        Scene secScene = new Scene(createBPMScreen, 400, 300);
        Stage newWindow = new Stage();
        newWindow.setTitle("Keeping an eye on your hearth rate!");
        newWindow.setScene(secScene);

        newWindow.initModality(Modality.WINDOW_MODAL);
        newWindow.initOwner(owner);
        newWindow.setX(owner.getX() + 200);
        newWindow.setY(owner.getY() + 100);

        Button backFromBPMScreen = new Button("Click to go back");
        backFromBPMScreen.setOnAction(e -> newWindow.close());
        createBPMScreen.getChildren().add(backFromBPMScreen);

        newWindow.show();
    }
}
