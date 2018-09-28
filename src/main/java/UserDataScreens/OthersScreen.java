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

public class OthersScreen {

    /**
     * This method starts the screen, where the user could enter a User specific Dataset, if needed (like TSH,Kreatinin,...)
     *
     * @param owner the "owner" stage of the new Window
     */
    public static void showOthersScreen(Stage owner) {
        GridPane createOthersScreen = new GridPane();
        createOthersScreen.setPadding(new Insets(10));
        createOthersScreen.setVgap(8);
        createOthersScreen.setHgap(10);

        Scene secScene = new Scene(createOthersScreen, 400, 300);
        Stage newWindow = new Stage();
        newWindow.setTitle("TODO-Placeholder");
        newWindow.setScene(secScene);

        newWindow.initModality(Modality.WINDOW_MODAL);
        newWindow.initOwner(owner);
        newWindow.setX(owner.getX() + 200);
        newWindow.setY(owner.getY() + 100);

        Button backFromOthersScreen = new Button("Click to go back");
        backFromOthersScreen.setOnAction(e -> newWindow.close());
        createOthersScreen.getChildren().add(backFromOthersScreen);

        newWindow.show();
    }
}
