package UserManagement.loginScreenMVP;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public interface IFailedLoginAlert {
    default void showFailedLoginAlert (String errorDescription) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, errorDescription, ButtonType.OK);
        alert.showAndWait();
        if (alert.getResult().equals(ButtonType.OK)) {
            alert.close();
        }
    }
}
