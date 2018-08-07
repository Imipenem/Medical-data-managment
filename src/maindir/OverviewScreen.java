import javafx.application.Application;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class OverviewScreen extends Application {


    @Override
    public void start(Stage starterStage) throws Exception {

        BorderPane layout = new BorderPane();

        MenuBar menuBar = new MenuBar();

        // Creating the About Us Menu

        Menu aboutTheProgram = new Menu("Medical_Data_Plot");
        MenuItem aboutUs = new MenuItem("About Medical_Data_Plot");
        MenuItem quitProgram = new MenuItem("Quit");
        aboutTheProgram.getItems().addAll(aboutUs, quitProgram);

        // Creating the Medi-Plan Menu

        Menu myMedics = new Menu("My Medi-Plan");
        MenuItem showMediPlan = new MenuItem("Show my Medi-Plan");
        myMedics.getItems().add(showMediPlan);

        // Creating the Doctor Appointement Menu

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
                    showBloodPressureScreen(starterStage);
                    break;

                case "Blood Sugar/HbA1c":
                    showBloodSugarScreen(starterStage);
                    break;

                case "BPM":
                    showHearthRateScreen(starterStage);
                    break;

                case "Others":
                    showOthersScreen(starterStage);
                    break;
            }
        });


        layout.setTop(menuBar);
        layout.setLeft(chooseAction);


        Scene scene = new Scene(layout, 450, 300);
        starterStage.setScene(scene);
        starterStage.setTitle("Medical_Data Plot: Track your health level");
        starterStage.setMinWidth(450);
        starterStage.setMinHeight(300);
        starterStage.show();
    }

    /**
     * This method starts the screen, whrere the user could enter his RR-Data.
     * The Button is only enabled, if the User gives the input in a correct format e.g both values (the systolic and
     * the diastolic RR-value have to be a 2-3 digit (0-9) number.
     *
     * TODO: This method contains just a placeholder line chart for general testing, will be replaced soon
     *
     * @param owner the "owner" stage of the new Window
     */
    public void showBloodPressureScreen(Stage owner) {
        GridPane createRRScreen = new GridPane();
        createRRScreen.setPadding(new Insets(10));
        createRRScreen.setVgap(8);
        createRRScreen.setHgap(10);


        Scene secScene = new Scene(createRRScreen, 400, 300);
        Stage newWindow = new Stage();
        newWindow.setTitle("Keeping an eye on your blood pressure");
        newWindow.setScene(secScene);

        newWindow.initModality(Modality.WINDOW_MODAL);
        newWindow.initOwner(owner);
        newWindow.setX(owner.getX() + 200);
        newWindow.setY(owner.getY() + 100);


        TextField bloodPressureSystolic = new TextField();
        bloodPressureSystolic.setPromptText("Your systolic value");
        GridPane.setConstraints(bloodPressureSystolic, 0, 0);

        TextField bloodPressureDiastolic = new TextField();
        bloodPressureDiastolic.setPromptText("Your diastolic value");
        GridPane.setConstraints(bloodPressureDiastolic, 0, 1);


        Button enterRRData = new Button("Confirm");
        GridPane.setConstraints(enterRRData, 0, 2);
        enterRRData.setDisable(true);
        enterRRData.setOnAction(event -> {

            Stage rrPlotWindow = new Stage();
            rrPlotWindow.setTitle("Your RR Plot");


            rrPlotWindow.initModality(Modality.WINDOW_MODAL);
            rrPlotWindow.initOwner(newWindow);
            rrPlotWindow.setX(newWindow.getX() + 200);
            rrPlotWindow.setY(newWindow.getY() + 100);

            final NumberAxis xAxis = new NumberAxis();
            final NumberAxis yAxis = new NumberAxis();
            xAxis.setLabel("Number of Month");
            //creating the chart
            final LineChart<Number,Number> lineChart =
                    new LineChart<>(xAxis,yAxis);

            lineChart.setTitle("Stock Monitoring, 2010");
            //defining a series
            XYChart.Series series = new XYChart.Series();
            series.setName("My portfolio");
            //populating the series with data
            series.getData().add(new XYChart.Data(1, 23));
            series.getData().add(new XYChart.Data(2, 14));
            series.getData().add(new XYChart.Data(3, 15));
            series.getData().add(new XYChart.Data(4, 24));
            series.getData().add(new XYChart.Data(5, 34));
            series.getData().add(new XYChart.Data(6, 36));
            series.getData().add(new XYChart.Data(7, 22));
            series.getData().add(new XYChart.Data(8, 45));
            series.getData().add(new XYChart.Data(9, 43));
            series.getData().add(new XYChart.Data(10, 17));
            series.getData().add(new XYChart.Data(11, 29));
            series.getData().add(new XYChart.Data(12, 25));

            Scene rrPlotScene = new Scene(lineChart, 400, 300);
            lineChart.getData().add(series);
            rrPlotWindow.setScene(rrPlotScene);

            rrPlotWindow.show();
        });

        Button backFromRRScreen = new Button("Click to go back");
        GridPane.setConstraints(backFromRRScreen, 1, 2);

        backFromRRScreen.setOnAction(e -> newWindow.close());

        BooleanBinding bb = new BooleanBinding() {
            {
                super.bind(bloodPressureDiastolic.textProperty(),
                        bloodPressureSystolic.textProperty());
            }

            @Override
            protected boolean computeValue() {
                return ((bloodPressureDiastolic.getText().isEmpty()
                        || bloodPressureSystolic.getText().isEmpty()))
                        || !((bloodPressureDiastolic.getText().matches("[0-9]{2,3}"))
                        && bloodPressureSystolic.getText().matches("[0-9]{2,3}"));
            }
        };

        enterRRData.disableProperty().bind(bb);

        createRRScreen.getChildren().addAll(enterRRData, bloodPressureSystolic, bloodPressureDiastolic, backFromRRScreen);

        newWindow.show();
    }

    /**
     * This method starts the screen, whrere the user could enter his Blood Sugar levels
     *
     * @param owner the "owner" stage of the new Window
     */
    public void showBloodSugarScreen(Stage owner) {
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

    /**
     * This method starts the screen, whrere the user could enter his heart rate data
     *
     * @param owner the "owner" stage of the new Window
     */
    public void showHearthRateScreen(Stage owner) {
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

    /**
     * This method starts the screen, whrere the user could enter a User specific Dataset, if needed (like TSH,Kreatinin,...)
     *
     * @param owner the "owner" stage of the new Window
     */
    public void showOthersScreen(Stage owner) {
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
