package UserManagement.loginScreenMVP.loginView;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class LoginViewImpl implements ILoginView {

    private TextField passwordInput;
    private TextField nameInput;
    private Button logInButton;
    private Button registerButton;
    private GridPane grid;
    private Stage logStage;

    public LoginViewImpl(Stage primaryStage) {
        logStage = primaryStage;
        createLoginScreen(primaryStage);
    }

    @Override
    public void createLoginScreen(Stage primaryStage) {
        grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(8);
        grid.setHgap(10);
        BackgroundImage backgroundLoginScreen = new BackgroundImage(new Image(getClass().getResourceAsStream("/Background/LoginScreenBackground.png"), 450, 350, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        Label password = initLabels("Passwort",0,1);
        Label UserName = initLabels("Username",0,0);
        nameInput = initTextFields("Username",1,0);
        passwordInput = initTextFields("Passwort",1,1);
        registerButton = initButtons("Register",1,2,false,false);
        logInButton = initButtons("Log In",0,2,true,true);

        grid.getChildren().addAll(UserName, nameInput, password, passwordInput, registerButton, logInButton);
        grid.setBackground(new Background(backgroundLoginScreen));

        Scene scene = new Scene(grid, 450, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Welcome to Medical_Data Plot");
        primaryStage.show();
    }

    @Override
    public Button initButtons( String Description, int row, int col, boolean defaultButton, boolean disable) {
       Button button = new Button(Description);
        GridPane.setConstraints(button,row,col);
        button.prefWidthProperty().bind(grid.widthProperty());
        button.prefHeightProperty().bind(grid.heightProperty());
        button.setMaxHeight(40);
        button.setDefaultButton(defaultButton);
        button.setDisable(disable);
        return button;
    }

    @Override
    public TextField initTextFields(String promptText, int row, int col) {
        TextField textField = new TextField();
        GridPane.setConstraints(textField, row, col);
        textField.setPromptText(promptText);
        textField.setPrefHeight(40);
        return textField;
    }

    @Override
    public Label initLabels(String label, int row, int col) {
        Label textLabel = new Label(label);
        textLabel.setTextFill(Paint.valueOf("red"));
        GridPane.setConstraints(textLabel, row, col);
        textLabel.prefHeight(40);
        return textLabel;
    }


    @Override
    public Stage getLoginStage() {
        return logStage;
    }
    @Override
    public Button getLoginButton() {
        return logInButton;
    }

    @Override
    public Button getRegisterButton() {
        return registerButton;
    }

    @Override
    public String getUsernameFieldText() {
        return nameInput.getText();
    }

    @Override
    public String getPasswordFieldText() {
        return passwordInput.getText();
    }

    @Override
    public TextField getUsernameField() {
        return nameInput;
    }

    @Override
    public TextField getPasswordField() {
        return passwordInput;
    }
}
