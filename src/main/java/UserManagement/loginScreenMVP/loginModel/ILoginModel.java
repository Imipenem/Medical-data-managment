package UserManagement.loginScreenMVP.loginModel;

public interface ILoginModel {

    void setUsername(String username);
    void setPassword(String password);

    String getUsername();
    String getPassword();
}
