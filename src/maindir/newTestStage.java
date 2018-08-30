import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class newTestStage {

    public static void createNewTestStage() {

        GridPane createLayoutScreen = new GridPane();
        createLayoutScreen.setPadding(new Insets(10));
        createLayoutScreen.setVgap(8);
        createLayoutScreen.setHgap(10);

        Stage testStage = new Stage();
        testStage.setTitle("Test");
        testStage.setScene(new Scene(createLayoutScreen,500,500));

        Button okButton = new Button("            OK          ");
        GridPane.setConstraints(okButton, 0, 0);
        okButton.setOnAction(e -> testStage.close());

        createLayoutScreen.getChildren().add(okButton);
        testStage.show();
    }
}
