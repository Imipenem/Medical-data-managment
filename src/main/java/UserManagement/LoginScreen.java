package UserManagement;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import maindir.OverviewScreen;
import java.sql.*;
import java.util.Objects;

/**
 * This class represents the login screen. The user is able to decide whether to create a new account ("Register") or
 * to log in with an already existing user account.
 *
 * The Users LoginData will be saved in a SQL Database using MySQL (NOTE: THIS IS A EARLY IMPLEMENTATION)
 * WILL IMPROVE THIS WITH FURTHER COMMITS
 */

public class LoginScreen {
    private TextField passwordInput;
    private TextField nameInput;

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


        Button logInButton = new Button("Log in");
        GridPane.setConstraints(logInButton, 0, 2);
        logInButton.prefWidthProperty().bind(grid.widthProperty());
        logInButton.prefHeightProperty().bind(grid.heightProperty());
        logInButton.setMaxHeight(40);
        logInButton.setDefaultButton(true);



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
             Statement myStat = Objects.requireNonNull(myConn).createStatement()) {
            ResultSet myRs = myStat.executeQuery("SELECT passwort FROM LoginData WHERE username = '"+nameInput.getText()+"'");
            if(!myRs.isBeforeFirst()){
                setFalseUsernameAlert();
            }
            else if (myRs.next()){
                if (myRs.getString("passwort").equals(passwordInput.getText())){
                    logScreen.close();
                    OverviewScreen OScreen = new OverviewScreen(nameInput.getText());
                    OScreen.createOverviewScreen();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }}

        private void setFalseUsernameAlert () {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Your Username is incorrect! Please reenter or create a new Account", ButtonType.OK, ButtonType.CANCEL);
            alert.showAndWait();

            if (alert.getResult().equals(ButtonType.OK)) {
                nameInput.setText("");
                nameInput.requestFocus();
                passwordInput.setText("");
                alert.close();
            } else if (alert.getResult().equals(ButtonType.CANCEL)) {
                alert.close();
            }
        }

        /*±
         * Get a connection to a database
         *
         * @return (if available) the connection to the specified Database
         */
        private Connection connect() {
            try {
                return DriverManager.getConnection("jdbc:mysql://localhost:3306/DB?autoReconnect=true&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=Europe/Helsinki", "root", "YOUWILLNOTSTEALPASSWORD");
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }


        }
    }