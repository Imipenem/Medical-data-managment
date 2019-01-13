package UserManagement;

import Helper.ButtonConfiguration;
import javafx.beans.binding.BooleanBinding;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import maindir.OverviewScreen;
import java.sql.*;

/**
 * This class represents the login screen. The user is able to decide whether to create a new account ("Register") or
 * to log in with an already existing user account.
 *
 * The Users LoginData will be saved in a SQL Database using MySQL (NOTE: THIS IS A EARLY IMPLEMENTATION)
 * WILL IMPROVE THIS WITH FURTHER COMMITS
 */

public class LoginScreen implements FailedLoginAlert {
    private TextField passwordInput;
    private TextField nameInput;
    private Button logInButton;

    public void createLoginScreen(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(8);
        grid.setHgap(10);
        BackgroundImage backgroundLoginScreen = new BackgroundImage(new Image(getClass().getResourceAsStream("/Background/LoginScreenBackground.png"), 450, 350, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        Label UserName = new Label("Username:");
        UserName.setTextFill(Paint.valueOf("red"));
        GridPane.setConstraints(UserName, 0, 0);
        UserName.prefHeight(40);

        nameInput = new TextField();
        GridPane.setConstraints(nameInput, 1, 0);
        nameInput.setPromptText("Username");
        nameInput.setPrefHeight(40);

        Label password = new Label("Password:");
        password.setTextFill(Paint.valueOf("red"));
        GridPane.setConstraints(password, 0, 1);
        password.prefHeight(40);

        passwordInput = new TextField();
        GridPane.setConstraints(passwordInput, 1, 1);
        passwordInput.setPrefHeight(40);


        Button createNewAccount = new Button("Register");
        GridPane.setConstraints(createNewAccount, 1, 2);
        createNewAccount.prefWidthProperty().bind(grid.widthProperty());
        createNewAccount.prefHeightProperty().bind(grid.heightProperty());
        createNewAccount.setMaxHeight(40);


        logInButton = new Button("Log in");
        GridPane.setConstraints(logInButton, 0, 2);
        logInButton.prefWidthProperty().bind(grid.widthProperty());
        logInButton.prefHeightProperty().bind(grid.heightProperty());
        logInButton.setMaxHeight(40);
        logInButton.setDefaultButton(true);
        logInButton.setDisable(true);
        myButtConfiq.configureButton();



        createNewAccount.setOnAction(event -> {
            RegisterScreen regScreen = new RegisterScreen();
            regScreen.createRegistrationScreen(primaryStage);
        });


        logInButton.setOnAction(event -> processLogin(primaryStage));

        grid.getChildren().addAll(UserName, nameInput, password, passwordInput, createNewAccount, logInButton);
        grid.setBackground(new Background(backgroundLoginScreen));

        Scene scene = new Scene(grid, 450, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Welcome to Medical_Data Plot");
        primaryStage.show();
    }


    private void processLogin(Stage logScreen) {
        try (Connection myConn = connect();
             PreparedStatement myStat =  myConn.prepareStatement("SELECT passwort FROM LoginData WHERE username = ?")){
            myStat.setString(1,nameInput.getText());
            ResultSet myRs= myStat.executeQuery();

             if(!myRs.next() || !myRs.getString("passwort").equals(passwordInput.getText())) {
                FailedLoginAlert.super.showFailedLoginAlert("Username or Password incorrect: Please reenter");
            }
            else {
                    logScreen.close();
                    OverviewScreen OScreen = new OverviewScreen(nameInput.getText());
                    OScreen.createOverviewScreen();
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

        /**
         * Get a connection to a database
         *
         * @return (if available) the connection to the specified Database
         */
        private Connection connect() {
            try {
                return DriverManager.getConnection("jdbc:mysql://localhost:3306/test?useSSL=false", "root", "dumboistgerneeis");
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
            //DB?autoReconnect=true&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=Europe/Helsinki
        }

    /**
     * This method reference binds the enable property of the login Button to the fact whether the inputs are empty or not.
     */
    private ButtonConfiguration myButtConfiq = this::bindLoginButton;

        private void bindLoginButton() {
            BooleanBinding bb = new BooleanBinding() {
                {
                    super.bind(nameInput.textProperty(),
                            passwordInput.textProperty());
                }

                @Override
                protected boolean computeValue() {
                    return ((nameInput.getText().isEmpty()
                            || passwordInput.getText().isEmpty()));
                }
            };
            logInButton.disableProperty().bind(bb);
        }
}