/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package tictactoe.authentication.login;

import TicTacToeCommon.models.requests.LoginRequest;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import tictactoe.base.SocketHandler;
import tictactoe.base.ViewController;

public class LoginViewController extends ViewController {

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button loginButton;

    @Override
    public URL getViewUri() {
        return getClass().getResource("LoginView.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginButton.setOnAction((e) -> {
            try {
                handle().authenticationProvider().login(new LoginRequest(username.getText(), password.getText()));
            } catch (SocketHandler.NotConnectedException ex) {
                uIAlert().showErrorDialog("Error", "Error authenticating you");
            }
        });
    }

}
