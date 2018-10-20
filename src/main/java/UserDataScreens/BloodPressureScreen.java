package UserDataScreens;

import Helper.ButtonConfiguration;
import Helper.Pair;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.beans.binding.BooleanBinding;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class represents the screen, where the user can enter his RR-levels.
 * The data will be saved using a JSON-File, where the program is mapping the actual date with the two values (diastolic and systolic).
 * <p>
 * Data will be loaded while opening the stage and be saved while closing it to provide this data for the chart for graphical representation.
 */

public class BloodPressureScreen {

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private Map<String, Pair> mappedRRValues;
    private String username;
    private TextField bloodPressureSystolic;
    private TextField bloodPressureDiastolic;
    private Button enterRRData;

    public BloodPressureScreen(String username) {
        this.username = username;
    }

    /**
     * This method starts the screen, where the user could enter his RR-Data. When starting this stage, the JSON File will
     * be loaded (if it exists) to prepare the data for further use.
     * The Button is only enabled, if the User gives the input in a correct format e.g both values (the systolic and
     * the diastolic RR-value have to be a 2-3 digit (0-9) number.
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

        newWindow.setOnShowing(event -> readRRDataFromJSON());


        bloodPressureSystolic = new TextField();
        bloodPressureSystolic.setPromptText("Your systolic value");
        GridPane.setConstraints(bloodPressureSystolic, 0, 0);

        bloodPressureDiastolic = new TextField();
        bloodPressureDiastolic.setPromptText("Your diastolic value");
        GridPane.setConstraints(bloodPressureDiastolic, 0, 1);


        enterRRData = new Button("Confirm");
        GridPane.setConstraints(enterRRData, 0, 2);
        enterRRData.setDefaultButton(true);
        enterRRData.setDisable(true);
        myButton.configureButton();

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Date");
        //creating the chart
        final LineChart<String, Number> lineChart =
                new LineChart<>(xAxis, yAxis);

        lineChart.setTitle("Blood Pressure trend");

        //defining the systolic series
        XYChart.Series SysSeries = new XYChart.Series();
        SysSeries.setName("Systolic RR-value");

        //defining the diastolic series
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
            Pair<Integer, Integer> rrPair = new Pair<>(Integer.parseInt(bloodPressureSystolic.getText()), Integer.parseInt(bloodPressureDiastolic.getText()));
            LocalDateTime measuredDateTime = LocalDateTime.now();
            String formattedDateTime = measuredDateTime.format(DateTimeFormatter.ofPattern("dd.MM HH:mm"));
            mappedRRValues.put(formattedDateTime, rrPair);

            //populating the series with data received from the "Mapped_RR_Value_Sample.json" - File
            for (Map.Entry<String, Pair> entry : mappedRRValues.entrySet()) {
                SysSeries.getData().add(new XYChart.Data<String, Object>(entry.getKey(), entry.getValue().getFirstValue()));
                DiaSeries.getData().add(new XYChart.Data<String, Object>(entry.getKey(), entry.getValue().getSecValue()));
            }
            rrPlotWindow.show();
        });

        Button backFromRRScreen = new Button("Click to go back");
        GridPane.setConstraints(backFromRRScreen, 1, 2);

        /*
         * Clicking the "Back-Button", the Data will be written to "Mapped_RR_Value_Sample.json"
         */

        backFromRRScreen.setOnAction(e -> {
            writeRRDataToJSON();
            newWindow.close();
        });


        createRRScreen.getChildren().addAll(enterRRData, bloodPressureSystolic, bloodPressureDiastolic, backFromRRScreen);
        newWindow.show();

        /*
         * Closing the Screen, the Data will be written to "Mapped_RR_Value_Sample.json"
         */
        newWindow.setOnCloseRequest(event -> writeRRDataToJSON());
    }

    /**
     * This method writes the received data for RR-levels into a User specific JSON-File"
     * <p>
     * ***IMPORTANT NOTE: This might be replaced in further implementations with an SQL Database!***
     */
    private void writeRRDataToJSON() {
        try (FileWriter writer = new FileWriter("Mapped_RR_Value_Sample_User: " + getUsername() + ".json")) {
            gson.toJson(mappedRRValues, writer);

        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    /**
     * This method reads the RR-Data from the User specific JSON-File
     * <p>
     * ***IMPORTANT NOTE: This might be replaced in further implementations with an SQL Database!***
     * <p>
     * If it doesnt exist ,the RRData will be stored (for temporary use) in a newly initialized TreeMap
     */

    private void readRRDataFromJSON() {
        if (new File("Mapped_RR_Value_Sample_User: " + getUsername() + ".json").exists()) {
            Type type = new TypeToken<TreeMap<String, Pair>>() {
            }.getType();
            try (BufferedReader br = new BufferedReader(new FileReader("Mapped_RR_Value_Sample_User: " + getUsername() + ".json"))) {
                mappedRRValues = gson.fromJson(br, type);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mappedRRValues = new TreeMap<>();
        }
    }

    /**
     * This method reference binds the "enable" property to the fact, whether the two textfields are filled in the correct manner.
     */

    private ButtonConfiguration myButton = this::bindEnterButton;

    private void bindEnterButton() {
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
    }

    private String getUsername() {
        return username;
    }
}
