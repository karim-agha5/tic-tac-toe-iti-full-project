/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package tictactoe.landing;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import tictactoe.home.HomeViewController;
import tictactoe.resources.styles.Styles;
import tictactoe.router.RouteViewController;
import tictactoe.utils.UIHelper;

public class LandingViewController extends RouteViewController {

    @FXML
    private Region background;
    @FXML
    private Label gameTitle1;
    @FXML
    private Label gameTitle2;
    @FXML
    private Label gameTitle3;
    @FXML
    private Button startButton;

    @Override
    public URL getViewUri() {
        return getClass().getResource("LandingView.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        scene().getStylesheets().add(resourcesLoader().getCss(Styles.LANDING_STYLE_STRING).toString());
        background.setEffect(UIHelper.createBlurEffect());
        startButton.setOnAction((e) -> {
            router().replace(new HomeViewController());
        });
    }
}
