package UserManagement.loginScreenMVP.loginView;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public interface ILoginView {
    void createLoginScreen(Stage loginStage);
    Stage getLoginStage();
    Button getLoginButton();
    Button getRegisterButton();
    String getUsernameFieldText();
    String getPasswordFieldText();
    TextField getUsernameField();
    TextField getPasswordField();
    Button initButtons(String Description, int row, int col, boolean defaultButton, boolean disable);
    TextField initTextFields(String promptText, int row, int col);
    Label initLabels(String label, int row, int col);
}
