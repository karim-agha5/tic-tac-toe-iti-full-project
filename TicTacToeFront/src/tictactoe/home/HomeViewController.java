/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package tictactoe.home;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import tictactoe.authentication.AuthenticationViewController;
import tictactoe.components.userview.UserViewController;
import tictactoe.game.GameViewController;
import tictactoe.game.providers.MultiplayerGameHandler;
import tictactoe.offline.pc.selector.OfflinePcSelectorViewController;
import tictactoe.online.players.OnlinePlayersViewController;
import tictactoe.resources.styles.Styles;
import tictactoe.router.RouteViewController;
import tictactoe.utils.UIHelper;

public class HomeViewController extends RouteViewController {

    @FXML
    private Region background;
    @FXML
    private Label gameTitle;
    @FXML
    private Group accountNode;
    @FXML
    private Button online1v1;
    @FXML
    private Button offline1v1;
    @FXML
    private Button offlinePC;

    @Override
    public URL getViewUri() {
        return getClass().getResource("HomeView.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        scene().getStylesheets().add(resourcesLoader().getCss(Styles.HOME_STYLE_STRING).toString());
        background.setEffect(UIHelper.createBlurEffect());
        
        Node node = attachController(new UserViewController());
        accountNode.getChildren().add(node);
        AnchorPane.setTopAnchor(accountNode, 50.0);
        AnchorPane.setRightAnchor(accountNode, 50.0);
        
        offlinePC.setOnAction((e) -> {
            router().push(new OfflinePcSelectorViewController());
        });
        
        online1v1.setOnAction((e) -> {
            if (handle().authenticationProvider().getIsAuth().getValue()) {
                router().push(new OnlinePlayersViewController());
            } else {
                Boolean result = uIAlert().showPromptDialog("Not logged in", "You must login to play online. Login?");
                if (result == true) {
                    router().push(new AuthenticationViewController());
                }
            }
        });
        
        offline1v1.setOnAction((e) -> {
            router().push(new GameViewController(new MultiplayerGameHandler(handle().authenticationProvider())));
        });
    }
}
