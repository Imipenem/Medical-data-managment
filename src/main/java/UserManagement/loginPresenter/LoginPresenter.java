package UserManagement.loginPresenter;

import UserManagement.loginModel.LoginModel;
import UserManagement.loginView.LoginView;
import javafx.stage.Stage;

public interface LoginPresenter {
    void setListenerForLoginButton();
    void setListenerForRegisterButton();
    void processLogin(Stage stage, String username, String password);
    void initPresenter(LoginModel model);
    void setLoginModel(LoginModel loginModel);
    LoginModel getLoginModel();
    void setLoginView(LoginView loginView);
    LoginView getLoginView();
}
