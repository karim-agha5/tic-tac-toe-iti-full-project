/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictactoe.utils;

import java.util.Optional;
import java.util.function.Consumer;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;

/**
 *
 * @author m-essam
 */
public class UIAlert {

    private final Stage stage;
    private Alert alert;

    public UIAlert(Stage stage) {
        this.stage = stage;
    }

    public void showLoadingDialog(String title, String message) {
        close();
        alert = createProgressAlert(title, message);
        alert.show();
    }

    public void showErrorDialog(String title, String message) {
        close();
        alert = createErrorDialog(title, message);
        alert.show();
    }
    
    public Boolean showPromptDialog(String title, String message) {
        close();
        alert = createPromptAlert(title, message);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            return result.get().getButtonData().equals(ButtonType.YES.getButtonData());
        }
        return null;
    }

    public void close() {
        if (alert != null && alert.isShowing()) {
            alert.close();
        }
    }

    private Alert createErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(stage);
        alert.setTitle(title);
        alert.setContentText(message);
        return alert;
    }

    private Alert createProgressAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.initOwner(stage);
        alert.setTitle(title);
        alert.setContentText(message);
        ProgressIndicator pIndicator = new ProgressIndicator();
        pIndicator.setProgress(-1);
        alert.setGraphic(pIndicator);
        alert.setHeaderText("Loading...");
        alert.setResult(ButtonType.CLOSE);
        alert.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        alert.getDialogPane().lookupButton(ButtonType.CLOSE).setDisable(true);
        return alert;
    }

    private Alert createPromptAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(stage);
        alert.setTitle(title);
        alert.setContentText(message);
        ProgressIndicator pIndicator = new ProgressIndicator();
        pIndicator.setProgress(-1);
        alert.setGraphic(pIndicator);
        ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(okButton, noButton);
        return alert;
    }
}
