package UserDataScreens.bloodSugarScreenMVP;

import Helper.ButtonConfiguration;
import UserDataScreens.IChartCreator;
import UserDataScreens.ScreenCreator;
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
import javafx.scene.control.Tooltip;
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
 * This class represents the functionality provided for a user to enter and process his blood sugar levels
 */

public class BloodSugarScreen implements IChartCreator {

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private String username;
    private Button convertMmol;
    private Button enterBSLevel;
    private TextField bsLevel;
    private Map<String, Double> mappedBSValues;
    private LineChart<String, Number> lineChart;
    private Stage bsPlotWindow;
    private Stage newWindow;
    private XYChart.Series bsSeries;

    private ScreenCreator bsCreator = this::buildBSScreen;

    public BloodSugarScreen(String username) {
        this.username = username;
    }

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
        newWindow = new Stage();
        newWindow.setTitle("Keeping an eye on your blood sugar levels!");
        newWindow.setScene(secScene);

        newWindow.initModality(Modality.WINDOW_MODAL);
        newWindow.initOwner(owner);
        newWindow.setX(owner.getX() + 200);
        newWindow.setY(owner.getY() + 100);
        newWindow.setOnShowing(e -> readRRDataFromJSON());


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
        enterBSLevel.setOnAction(e -> plotData());


        Button backFromBSScreen = new Button("Click to go back");
        GridPane.setConstraints(backFromBSScreen, 2, 1);
        backFromBSScreen.setOnAction(e -> {
            writeRRDataToJSON();
            newWindow.close();
        });
        createBSScreen.getChildren().addAll(backFromBSScreen, convertMmol, bsLevel, enterBSLevel);

        newWindow.show();
        newWindow.setOnCloseRequest(e -> writeRRDataToJSON());
    }


    /**
     * This method creates a line chart, used for displaying the users blood sugar levels
     */
    @Override
    public void createChart() {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Date");

        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Blood Sugar trend");

        bsSeries = new XYChart.Series();
        bsSeries.setName("Blood sugar level in mg/dl");


        Scene rrPlotScene = new Scene(lineChart, 400, 300);
        lineChart.getData().add(bsSeries);
        bsPlotWindow = new Stage();
        bsPlotWindow.setTitle("Your RR Plot");
        bsPlotWindow.initModality(Modality.WINDOW_MODAL);
        bsPlotWindow.initOwner(newWindow);
        bsPlotWindow.setX(newWindow.getX() + 200);
        bsPlotWindow.setY(newWindow.getY() + 100);
        bsPlotWindow.setScene(rrPlotScene);
    }

    /**
     * This method plots the chart with the corresponding blood sugar levels
     */
    @Override
    public void plotData() {
        createChart();
        LocalDateTime measuredDateTime = LocalDateTime.now();
        String formattedDateTime = measuredDateTime.format(DateTimeFormatter.ofPattern("dd.MM HH:mm"));
        mappedBSValues.put(formattedDateTime, Double.parseDouble(bsLevel.getText()));

        for (Map.Entry<String, Double> entry : mappedBSValues.entrySet()) {
            XYChart.Data<String, Number> currentData = new XYChart.Data<>(entry.getKey(), entry.getValue());
            bsSeries.getData().add(currentData);
            Tooltip.install(currentData.getNode(), new Tooltip(
                    currentData.getXValue() + "\n" +
                            "Blood sugar in mg/dl : " + currentData.getYValue()));
        }
        bsPlotWindow.show();

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
     * This method writes the received data for BS-levels into a User specific JSON-File"
     * <p>
     * ***IMPORTANT NOTE: This might be replaced in further implementations with an SQL Database!***
     */
    private void writeRRDataToJSON() {
        try (FileWriter writer = new FileWriter("Mapped_BS_Value_Sample_User: " + getUsername() + ".json")) {
            gson.toJson(mappedBSValues, writer);

        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    /**
     * This method reads the BS-Data from the User specific JSON-File
     * <p>
     * ***IMPORTANT NOTE: This might be replaced in further implementations with an SQL Database!***
     * <p>
     * If it doesnt exist ,the RRData will be stored (for temporary use) in a newly initialized TreeMap
     */

    private void readRRDataFromJSON() {
        if (new File("Mapped_BS_Value_Sample_User: " + getUsername() + ".json").exists()) {
            Type type = new TypeToken<TreeMap<String, Double>>() {
            }.getType();
            try (BufferedReader br = new BufferedReader(new FileReader("Mapped_BS_Value_Sample_User: " + getUsername() + ".json"))) {
                mappedBSValues = gson.fromJson(br, type);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mappedBSValues = new TreeMap<>();
        }
    }

    /**
     * Converts the input to mmol/l
     */
    private void convertToMmol() {
        double mmol = Double.parseDouble(bsLevel.getText());
        mmol = mmol * 0.0555;
        bsLevel.setText(String.valueOf(mmol));
    }

    private String getUsername() {
        return username;
    }
}
