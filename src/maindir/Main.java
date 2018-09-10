import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    /**
     * Starting this JavFX Application will result in showing the LoginScreen
     *
     * @param primaryStage the stage for showing the LoginScreen
     */

    @Override
    public void start(Stage primaryStage) {
        LoginScreen logScreen = new LoginScreen();
        logScreen.createLoginScreen(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
