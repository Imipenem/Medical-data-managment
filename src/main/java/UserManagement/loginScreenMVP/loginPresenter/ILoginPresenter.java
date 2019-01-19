package UserManagement.loginScreenMVP.loginPresenter;

import UserManagement.loginScreenMVP.loginModel.ILoginModel;
import UserManagement.loginScreenMVP.loginView.ILoginView;
import javafx.stage.Stage;

public interface ILoginPresenter {
    void setListenerForLoginButton();
    void setListenerForRegisterButton();
    void processLogin(Stage stage, String username, String password);
    void initPresenter(ILoginModel model);
    void setLoginModel(ILoginModel loginModel);
    ILoginModel getLoginModel();
    void setLoginView(ILoginView loginView);
    ILoginView getLoginView();
}
