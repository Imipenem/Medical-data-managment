package UserManagement;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import maindir.User;

import java.util.List;

/**
 * This class represents the Registration Screen, where the User can choose a username and password.
 * Spaces will be removed to prevent false input.
 * <p>
 * TODO: Ensure that the Username (at least) is unique!
 */

public class RegisterScreen {

    public void registerNewUser(Stage primaryStage, List<User> UserSample) {
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


        Label choosesUserName = new Label("Choose your username:");
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
    }
}
