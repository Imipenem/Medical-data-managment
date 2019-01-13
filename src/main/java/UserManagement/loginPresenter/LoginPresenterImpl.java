package UserManagement.loginPresenter;

import Helper.ButtonConfiguration;
import UserManagement.FailedLoginAlert;
import UserManagement.loginModel.LoginModel;
import UserManagement.loginView.LoginView;
import javafx.beans.binding.BooleanBinding;
import javafx.stage.Stage;
import maindir.OverviewScreen;

import java.sql.*;

public class LoginPresenterImpl implements LoginPresenter,FailedLoginAlert {

    private LoginView loginView;
    private LoginModel loginModel;

    @Override
    public void initPresenter(LoginModel model){
        setLoginModel(model);
        setListenerForLoginButton();
        myButtConfiq.configureButton();
    }

    @Override
    public void setListenerForLoginButton() {
        loginView.getLoginButton().setOnAction(e -> processLogin(loginView.getLoginStage(),loginView.getUsernameFieldText(),loginView.getPasswordFieldText()));
    }

    @Override
    public void setListenerForRegisterButton() {

    }

    private void updateModelFromPresenter() {
        getLoginModel().setUsername(getLoginView().getUsernameFieldText());
        getLoginModel().setPassword(getLoginView().getPasswordFieldText());
    }


    @Override
    public void processLogin(Stage stage, String username, String password) {
        try (Connection myConn = connect();
             PreparedStatement myStat =  myConn.prepareStatement("SELECT passwort FROM LoginData WHERE username = ?")){
            updateModelFromPresenter();
            myStat.setString(1,getLoginView().getUsernameFieldText());
            ResultSet myRs= myStat.executeQuery();

            if(!myRs.next() || !myRs.getString("passwort").equals(getLoginModel().getPassword())) {
                FailedLoginAlert.super.showFailedLoginAlert("Username or Password incorrect: Please reenter");
            }
            else {
                stage.close();
                OverviewScreen OScreen = new OverviewScreen(getLoginView().getUsernameFieldText());
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
    private ButtonConfiguration myButtConfiq = this::bindLoginButton; //TODO: call this method!!!

    private void bindLoginButton() {
        BooleanBinding bb = new BooleanBinding() {
            {
                super.bind(getLoginView().getUsernameField().textProperty(),
                        getLoginView().getPasswordField().textProperty());
            }

            @Override
            protected boolean computeValue() {
                return ((getLoginView().getUsernameFieldText().isEmpty()
                        || getLoginView().getPasswordFieldText().isEmpty()));
            }
        };
        loginView.getLoginButton().disableProperty().bind(bb);
    }

    @Override
    public void setLoginModel(LoginModel loginModel) {
        this.loginModel = loginModel;
    }

    @Override
    public LoginModel getLoginModel() {
        return loginModel;
    }

    @Override
    public void setLoginView(LoginView loginView) {
        this.loginView = loginView;
    }

    @Override
    public LoginView getLoginView() {
        return loginView;
    }
}
