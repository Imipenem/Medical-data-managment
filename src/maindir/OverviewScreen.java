import com.google.gson.reflect.TypeToken;
import javafx.application.Application;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import com.google.gson.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class OverviewScreen extends Application {

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    Map<String, int[]> mappedRRValues;

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
     * <p>
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

        newWindow.setOnShowing(event -> {

            if (new File("Mapped_RR_Value_Sample.json").exists()) {
                Type type = new TypeToken<TreeMap<String, int[]>>() {
                }.getType();
                try (BufferedReader br = new BufferedReader(new FileReader("Mapped_RR_Value_Sample.json"))) {
                    mappedRRValues = gson.fromJson(br, type);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                mappedRRValues = new TreeMap<>();
            }
        });


        TextField bloodPressureSystolic = new TextField();
        bloodPressureSystolic.setPromptText("Your systolic value");
        GridPane.setConstraints(bloodPressureSystolic, 0, 0);

        TextField bloodPressureDiastolic = new TextField();
        bloodPressureDiastolic.setPromptText("Your diastolic value");
        GridPane.setConstraints(bloodPressureDiastolic, 0, 1);


        Button enterRRData = new Button("Confirm");
        GridPane.setConstraints(enterRRData, 0, 2);
        enterRRData.setDisable(true);

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Date");
        //creating the chart
        final LineChart<String, Number> lineChart =
                new LineChart<String, Number>(xAxis, yAxis);

        lineChart.setTitle("Blood Pressure trend");
        //defining the systolic series
        XYChart.Series SysSeries = new XYChart.Series();
        SysSeries.setName("Systolic RR-value");

        XYChart.Series DiaSeries = new XYChart.Series();
        DiaSeries.setName("Diastolic RR-value");

        Scene rrPlotScene = new Scene(lineChart, 400, 300);
        lineChart.getData().addAll(SysSeries, DiaSeries);
        Stage rrPlotWindow = new Stage();
        rrPlotWindow.setTitle("Your RR Plot");
        rrPlotWindow.initModality(Modality.WINDOW_MODAL);
        rrPlotWindow.initOwner(newWindow);
        rrPlotWindow.setX(newWindow.getX() + 200);
        rrPlotWindow.setY(newWindow.getY() + 100);
        rrPlotWindow.setScene(rrPlotScene);


        enterRRData.setOnAction(event -> {
            int[] mapRRData = new int[2];
            LocalDateTime measuredDateTime = LocalDateTime.now();
            mapRRData[0] = Integer.parseInt(bloodPressureSystolic.getText());
            mapRRData[1] = Integer.parseInt(bloodPressureDiastolic.getText());
            String formattedDateTime = measuredDateTime.format(DateTimeFormatter.ofPattern("dd.MM HH:mm"));
            mappedRRValues.put(formattedDateTime, mapRRData);

            // map.forEach((k, v) -> i[0] += k + v);
            //populating the series with data
            for (Map.Entry<String, int[]> entry : mappedRRValues.entrySet()) {
                SysSeries.getData().add(new XYChart.Data<String, Number>(entry.getKey(), entry.getValue()[0]));
                DiaSeries.getData().add(new XYChart.Data<String, Number>(entry.getKey(), entry.getValue()[1]));
            }
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

        newWindow.setOnCloseRequest(event -> {
            System.out.println("RRScreen is closing");
            try (FileWriter writer = new FileWriter("Mapped_RR_Value_Sample.json")) {
                gson.toJson(mappedRRValues, writer);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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
