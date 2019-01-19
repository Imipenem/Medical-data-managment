package maindir;
import UserManagement.loginScreenMVP.loginModel.LoginModelImpl;
import UserManagement.loginScreenMVP.loginPresenter.ILoginPresenter;
import UserManagement.loginScreenMVP.loginPresenter.LoginPresenterImpl;
import UserManagement.loginScreenMVP.loginView.LoginViewImpl;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    /**
     * Starting this JavaFX Application will result in showing the LoginScreen
     *
     * @param primaryStage the stage for showing the LoginScreen
     */

    private ILoginPresenter presenter = new LoginPresenterImpl();

    @Override
    public void start(Stage primaryStage) {
        presenter.setLoginView(new LoginViewImpl(primaryStage));
        presenter.initPresenter(new LoginModelImpl());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
