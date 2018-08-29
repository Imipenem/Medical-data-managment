import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class Main extends Application {

    private List<User> UserSample = new ArrayList<>();
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();


    @Override
    public void start(Stage primaryStage) throws Exception {

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(8);
        grid.setHgap(10);

        BackgroundImage backgroundLoginScreen = new BackgroundImage(new Image("file:LoginScreenBackground.png", 450, 350, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        Label UserName = new Label("Username:");
        UserName.setTextFill(Paint.valueOf("red"));
        GridPane.setConstraints(UserName, 0, 0);
        UserName.prefHeight(40);

        TextField nameInput = new TextField();
        GridPane.setConstraints(nameInput, 1, 0);
        nameInput.setPromptText("Username");
        nameInput.setPrefHeight(40);

        Label password = new Label("Password:");
        password.setTextFill(Paint.valueOf("red"));
        GridPane.setConstraints(password, 0, 1);
        password.prefHeight(40);

        TextField passwordInput = new TextField();
        GridPane.setConstraints(passwordInput, 1, 1);
        passwordInput.setPrefHeight(40);


        Button createNewPassword = new Button("Register");
        GridPane.setConstraints(createNewPassword, 1, 2);
        createNewPassword.prefWidthProperty().bind(grid.widthProperty());
        createNewPassword.prefHeightProperty().bind(grid.heightProperty());
        createNewPassword.setMaxHeight(40);


        Button logInButton = new Button("Log in");
        GridPane.setConstraints(logInButton, 0, 2);
        logInButton.prefWidthProperty().bind(grid.widthProperty());
        logInButton.prefHeightProperty().bind(grid.heightProperty());
        logInButton.setMaxHeight(40);
        logInButton.setDefaultButton(true);

        if (new File("UserData.json").exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader("UserData.json"))) {

                // Convert JSON to Java Object

                Type listType = new TypeToken<ArrayList<User>>() {
                }.getType();
                List<User> staff2 = gson.fromJson(br, listType);

                if (UserSample.size() > staff2.size()) {
                    staff2 = UserSample;
                } else {
                    UserSample = staff2;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        createNewPassword.setOnAction(event -> {
            GridPane createLayoutScreen = new GridPane();
            createLayoutScreen.setPadding(new Insets(10));
            createLayoutScreen.setVgap(8);
            createLayoutScreen.setHgap(10);

            Scene secScene = new Scene(createLayoutScreen, 400, 300);
            Stage newWindow = new Stage();
            newWindow.setTitle("Creating your user account");
            newWindow.setScene(secScene);

            newWindow.initModality(Modality.WINDOW_MODAL);
            newWindow.initOwner(primaryStage);
            newWindow.setX(primaryStage.getX() + 200);
            newWindow.setY(primaryStage.getY() + 100);


            Label choosesUserName = new Label("Choose your Username:");
            GridPane.setConstraints(choosesUserName, 0, 0);

            TextField nameInput2 = new TextField();
            GridPane.setConstraints(nameInput2, 1, 0);
            nameInput2.setPromptText("Username");

            Label choosePassword = new Label("Choose your password:");
            GridPane.setConstraints(choosePassword, 0, 1);

            TextField passwordInput1 = new TextField();
            GridPane.setConstraints(passwordInput1, 1, 1);

            Button okButton = new Button("            OK          ");
            GridPane.setConstraints(okButton, 0, 2);

            /*
             * Write the User Data to the List to keep it for the running cycle!
             */

            okButton.setOnAction(e -> {
                nameInput2.setText(nameInput2.getText().replaceAll(" ", ""));
                passwordInput1.setText(passwordInput1.getText().replaceAll(" ", ""));
                User currentUser = new User(nameInput2.getText(), passwordInput1.getText());
                UserSample.add(currentUser);
                newWindow.close();
            });

            createLayoutScreen.getChildren().addAll(choosesUserName, nameInput2, choosePassword, passwordInput1, okButton);
            newWindow.show();
        });

        /*
         * Load the existing UserData from the JSON-File and check, if the typed in fits one.
         */

        logInButton.setOnAction(event -> {

            User localDummyUser = new User(nameInput.getText(), passwordInput.getText());
            if (UserSample.contains(localDummyUser)) {
                System.out.println("Login successfull");
                startDefaultScreen();
                primaryStage.close();

            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Your password or Username were incorrect! Please reenter", ButtonType.OK, ButtonType.CANCEL);
                alert.showAndWait();

                if (alert.getResult().equals(ButtonType.OK)) {
                    nameInput.setText("");
                    nameInput.requestFocus();
                    passwordInput.setText("");
                    alert.close();
                } else if (alert.getResult().equals(ButtonType.CANCEL)) {
                    alert.close();
                    primaryStage.close();
                }
            }
        });
        grid.getChildren().addAll(UserName, nameInput, password, passwordInput, createNewPassword, logInButton);
        grid.setBackground(new Background(backgroundLoginScreen));

        Scene scene = new Scene(grid, 450, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Welcome to Medical_Data Plot");
        primaryStage.show();
    }

    /**
     * The User Data is written to the JSON-File only when the stage is closing and re-written for every further cycle!
     */
    @Override
    public void stop() {
        System.out.println("Stage is closing");
        try (FileWriter writer = new FileWriter("UserData.json")) {

            gson.toJson(UserSample, writer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void startDefaultScreen() {
        final OverviewScreen defaultScreen = new OverviewScreen();
        Stage default_primaryStage = new Stage();
        try {
            defaultScreen.start(default_primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
