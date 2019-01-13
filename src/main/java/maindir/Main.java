package maindir;
import UserManagement.loginModel.LoginModelImpl;
import UserManagement.loginPresenter.LoginPresenter;
import UserManagement.loginPresenter.LoginPresenterImpl;
import UserManagement.loginView.LoginViewImpl;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    /**
     * Starting this JavaFX Application will result in showing the LoginScreen
     *
     * @param primaryStage the stage for showing the LoginScreen
     */

    private LoginPresenter presenter = new LoginPresenterImpl();

    @Override
    public void start(Stage primaryStage) {
        presenter.setLoginView(new LoginViewImpl(primaryStage));
        presenter.initPresenter(new LoginModelImpl());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
