package UserManagement;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public interface FailedLoginAlert {
    default void showFailedLoginAlert (String errorDescription) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, errorDescription, ButtonType.OK);
        alert.showAndWait();
        if (alert.getResult().equals(ButtonType.OK)) {
            alert.close();
        }
    }
}
