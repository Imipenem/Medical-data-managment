import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * This class represents the Overview Screen, e.g the default user screen after a successful login attempt.
 * For now, functionality is only provided for the "BloodPressure"_Data, selectable in the choice box.
 * For a detailed explanation of the single functions, read the corresponding class description(s).
 * <p>
 * TODO: Will provide functionality for remaining MenuItems/ChoiceBoxItems
 */

public class OverviewScreen {

    private String username;

    public OverviewScreen(String username) {
        this.username = username;
    }


    public void createOverviewScreen() {

        Stage starterStage = new Stage();
        BorderPane layout = new BorderPane();

        MenuBar menuBar = new MenuBar();

        // Creating the "About Us" Menu

        Menu aboutTheProgram = new Menu("Medical_Data_Plot");
        MenuItem aboutUs = new MenuItem("About Medical_Data_Plot");
        MenuItem quitProgram = new MenuItem("Quit");
        aboutTheProgram.getItems().addAll(aboutUs, quitProgram);

        // Creating the "Medi-Plan" Menu

        Menu myMedics = new Menu("My Medi-Plan");
        MenuItem showMediPlan = new MenuItem("Show my Medi-Plan");
        myMedics.getItems().add(showMediPlan);

        // Creating the "Doctor Appointement" Menu

        Menu myNextAppointements = new Menu("Doctor Appointments");
        MenuItem showAppointements = new MenuItem("Show next appointements");
        myNextAppointements.getItems().add(showAppointements);

        menuBar.getMenus().addAll(aboutTheProgram, myMedics, myNextAppointements);

        // Creating the choice Box to decide what action should be performed

        ChoiceBox<String> chooseAction = new ChoiceBox<>();
        chooseAction.setTooltip(new Tooltip("Choose your desired option"));
        chooseAction.setItems(FXCollections.observableArrayList("Blood Pressure", "Blood Sugar/HbA1c", "BPM", "Others"));
        chooseAction.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            switch (newValue) {
                case "Blood Pressure":
                    BloodPressureScreen rrScreen = new BloodPressureScreen(getUsername());
                    rrScreen.showBloodPressureScreen(starterStage);
                    break;

                case "Blood Sugar/HbA1c":
                    BloodSugarScreen.showBloodSugarScreen(starterStage);
                    break;

                case "BPM":
                    HeartRateScreen.showHearthRateScreen(starterStage);
                    break;

                case "Others":
                    OthersScreen.showOthersScreen(starterStage);
                    break;
            }
        });

        layout.setTop(menuBar);
        layout.setLeft(chooseAction);

        Scene scene = new Scene(layout, 450, 300);
        starterStage.setScene(scene);
        starterStage.setTitle("Medical_Data Plot: Let´s track your health level, "+ getUsername());
        starterStage.setMinWidth(450);
        starterStage.setMinHeight(300);
        starterStage.show();
    }

    public String getUsername() {
        return username;
    }
}