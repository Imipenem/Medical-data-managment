import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the login screen. The user is able to decide whether to create a new account ("Register") or
 * to log in with an already existing user account.
 * The User Data will be saved to a JSON-File in both cases whether the User logs in right after registration or closes the Login Screen immediately
 * after registration.
 */

public class LoginScreen {

    private List<User> UserSample = new ArrayList<>();
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void createLoginScreen(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(8);
        grid.setHgap(10);

        BackgroundImage backgroundLoginScreen = new BackgroundImage(new Image("file:src/maindir/resources/LoginScreenBackground.png", 450, 350, false, true),
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


        Button createNewAccount = new Button("Register");
        GridPane.setConstraints(createNewAccount, 1, 2);
        createNewAccount.prefWidthProperty().bind(grid.widthProperty());
        createNewAccount.prefHeightProperty().bind(grid.heightProperty());
        createNewAccount.setMaxHeight(40);


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

        createNewAccount.setOnAction(event -> {
            RegisterScreen regScreen = new RegisterScreen();
            regScreen.registerNewUser(primaryStage, UserSample);
        });

        /*
         * Load the existing UserData from the JSON-File and check, if the typed in fits one.
         */

        logInButton.setOnAction(event -> {

            User localDummyUser = new User(nameInput.getText(), passwordInput.getText());
            if (UserSample.contains(localDummyUser)) {
                writeUserDataToJSON();
                OverviewScreen OScreen = new OverviewScreen();
                OScreen.createOverviewScreen();
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
        grid.getChildren().addAll(UserName, nameInput, password, passwordInput, createNewAccount, logInButton);
        grid.setBackground(new Background(backgroundLoginScreen));

        Scene scene = new Scene(grid, 450, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Welcome to Medical_Data Plot");
        primaryStage.show();

        /*
         * The User Data is written to the JSON-File only when the stage is closing and re-written for every further cycle!
         */
        primaryStage.setOnCloseRequest(e -> writeUserDataToJSON());
    }

    private void writeUserDataToJSON() {
        System.out.println("LoginScreen is closing");
        try (FileWriter writer = new FileWriter("UserData.json")) {

            gson.toJson(UserSample, writer);

        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }
}