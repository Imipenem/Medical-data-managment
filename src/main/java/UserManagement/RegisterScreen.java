package UserManagement;

import Helper.ButtonConfiguration;
import javafx.beans.binding.BooleanBinding;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.*;

/**
 * This class represents the Registration Screen, where the User can choose a username and password.
 * Spaces will be removed to prevent false input.
 */

public class RegisterScreen implements FailedLoginAlert {

    private Button okButton;
    private TextField nameInput2;
    private TextField passwordInput1;
    private Stage newWindow;

    public void createRegistrationScreen(Stage primaryStage) {
        GridPane createLayoutScreen = new GridPane();
        createLayoutScreen.setPadding(new Insets(10));
        createLayoutScreen.setVgap(8);
        createLayoutScreen.setHgap(10);

        Scene secScene = new Scene(createLayoutScreen, 400, 300);
        newWindow = new Stage();
        newWindow.setTitle("Creating your user account");
        newWindow.setScene(secScene);

        newWindow.initModality(Modality.WINDOW_MODAL);
        newWindow.initOwner(primaryStage);
        newWindow.setX(primaryStage.getX() + 200);
        newWindow.setY(primaryStage.getY() + 100);


        Label choosesUserName = new Label("Choose your username:");
        GridPane.setConstraints(choosesUserName, 0, 0);

        nameInput2 = new TextField();
        GridPane.setConstraints(nameInput2, 1, 0);
        nameInput2.setPromptText("Username");

        Label choosePassword = new Label("Choose your password:");
        GridPane.setConstraints(choosePassword, 0, 1);

        passwordInput1 = new TextField();
        GridPane.setConstraints(passwordInput1, 1, 1);


        okButton = new Button("            OK          ");
        GridPane.setConstraints(okButton, 0, 2);
        okButton.setDefaultButton(true);
        okButton.setDisable(true);
        enterButtConf.configureButton();


        okButton.setOnAction(e -> {storeUserInDatabase(nameInput2.getText().replaceAll(" ", ""),passwordInput1.getText());
        });

        createLayoutScreen.getChildren().addAll(choosesUserName, nameInput2, choosePassword, passwordInput1, okButton);
        newWindow.show();
    }

   /** TODO ID for DB, ExceptionsHandling

    /**
     * This method stores the recently registered user in the "LoginData" Database. Therefore, it stores
     * the username and the password for each user.
     *
     * @param username the username (identifier) of the user
     * @param password the password of the user
     */

    private void storeUserInDatabase(String username, String password) {
        try (Connection conn = connect()) {
            assert conn != null;
            PreparedStatement createDatabase = conn.prepareStatement("CREATE TABLE IF NOT EXISTS LoginData(username varchar(255) NOT NULL UNIQUE, passwort varchar(255) NOT NULL, PRIMARY KEY (username))");
            createDatabase.executeUpdate();

            PreparedStatement insertInDB = conn.prepareStatement("INSERT INTO LoginData VALUES(?,?)");
            insertInDB.setString(1,username);
            insertInDB.setString(2,password);
            insertInDB.executeUpdate();
            newWindow.close();
        }
         catch (SQLIntegrityConstraintViolationException dublEntry){
             FailedLoginAlert.super.showFailedLoginAlert("This username already exists. Please choose another name.");
        }
         catch (Exception e) {
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
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/DB?autoReconnect=true&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=Europe/Helsinki", "root", "O_O");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method reference binds the enable property of the OK- Button to the fact whether the inputs are empty or not.
     */

    private ButtonConfiguration enterButtConf = this::configureRegisterButton;

    private void configureRegisterButton() {
        BooleanBinding bb = new BooleanBinding() {
            {
                super.bind(nameInput2.textProperty(),
                        passwordInput1.textProperty());
            }

            @Override
            protected boolean computeValue() {
                return ((nameInput2.getText().isEmpty()
                        || passwordInput1.getText().isEmpty()));
            }
        };
        okButton.disableProperty().bind(bb);
    }
}

