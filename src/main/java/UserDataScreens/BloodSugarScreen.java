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

public class BloodSugarScreen {

    private ScreenCreator bsCreator = this::buildBSScreen;

    public void showBloodSugarScreen(Stage owner) {
            bsCreator.buildScreen(owner);
        }

    /**
     * This method starts the screen, where the user could enter his Blood Sugar levels
     *
     * @param owner the "owner" stage of the new Window
     */

    private void buildBSScreen(Stage owner){
        GridPane createBSScreen = new GridPane();
        createBSScreen.setPadding(new Insets(10));
        createBSScreen.setVgap(8);
        createBSScreen.setHgap(10);

        Scene secScene = new Scene(createBSScreen, 400, 300);
        Stage newWindow = new Stage();
        newWindow.setTitle("Keeping an eye on your blood sugar levels!");
        newWindow.setScene(secScene);

        newWindow.initModality(Modality.WINDOW_MODAL);
        newWindow.initOwner(owner);
        newWindow.setX(owner.getX() + 200);
        newWindow.setY(owner.getY() + 100);

        Button backFromBSScreen = new Button("Click to go back");
        backFromBSScreen.setOnAction(e -> newWindow.close());
        createBSScreen.getChildren().add(backFromBSScreen);

        newWindow.show();
    }
}
