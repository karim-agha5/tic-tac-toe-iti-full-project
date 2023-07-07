/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package tictactoe.authentication.signup;

import TicTacToeCommon.models.requests.SignUpRequest;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import tictactoe.base.SocketHandler;
import tictactoe.base.ViewController;

public class SignupViewController extends ViewController {

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button signupButton;

    @Override
    public URL getViewUri() {
        return getClass().getResource("SignupView.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        signupButton.setOnAction((e) -> {
            try {
                handle().authenticationProvider().signUp(new SignUpRequest(username.getText(), password.getText()));
            } catch (SocketHandler.NotConnectedException ex) {
                uIAlert().showErrorDialog("Error", "Error authenticating you");
            }
        });
    }
}
