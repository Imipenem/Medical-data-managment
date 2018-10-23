package UserDataScreens;

import Helper.ButtonConfiguration;
import javafx.beans.binding.BooleanBinding;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This class represents the functionality provided for a user to enter and process his blood sugar levels
 */

public class BloodSugarScreen {

    private Button convertMmol;
    private Button enterBSLevel;
    private TextField bsLevel;
    private ScreenCreator bsCreator = this::buildBSScreen;

    /**
     * This method starts the screen, where the user could enter his Blood Sugar levels.
     * Implemented from the ScreenCreator- Interface
     *
     * @param owner the "owner" stage of the new Window
     */
    public void showBloodSugarScreen(Stage owner) {
        bsCreator.buildScreen(owner);
    }

    private void buildBSScreen(Stage owner) {
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


        bsLevel = new TextField();
        bsLevel.setPromptText("BS-Level in mg/dl");
        GridPane.setConstraints(bsLevel, 0, 0);

        convertMmol = new Button("Convert to mmol/l");
        GridPane.setConstraints(convertMmol, 1, 1);
        convertMmol.setOnAction(e -> convertToMmol());
        convertMmol.setDisable(true);

        enterBSLevel = new Button("Show Chart");
        GridPane.setConstraints(enterBSLevel, 0, 1);
        enterBSLevel.setDisable(true);
        myButton.configureButton();


        Button backFromBSScreen = new Button("Click to go back");
        GridPane.setConstraints(backFromBSScreen, 2, 1);
        backFromBSScreen.setOnAction(e -> newWindow.close());
        createBSScreen.getChildren().addAll(backFromBSScreen, convertMmol, bsLevel, enterBSLevel);

        newWindow.show();
    }


    /**
     * This method binds the disable property of the "Convert"-/" Enter"-Button to the fact,
     * whether the input is in a specific pattern ([2-3 digits].[1-2 digits])
     * Implemented from the functional interface ButtonConfiguration
     */
    private ButtonConfiguration myButton = this::bindEnterButton;

    private void bindEnterButton() {
        BooleanBinding bb = new BooleanBinding() {
            {
                super.bind(bsLevel.textProperty());
            }

            @Override
            protected boolean computeValue() {
                return ((bsLevel.getText().isEmpty()))
                        || !((bsLevel.getText().matches("[0-9]{2,3}\\.\\d{1,2}")));
            }
        };
        enterBSLevel.disableProperty().bind(bb);
        convertMmol.disableProperty().bind(bb);
    }

    /**
     * Converts the input to mmol/l
     */
    private void convertToMmol() {
        double mmol = Double.parseDouble(bsLevel.getText());
        mmol = mmol * 0.0555;
        bsLevel.setText(String.valueOf(mmol));
    }
}
