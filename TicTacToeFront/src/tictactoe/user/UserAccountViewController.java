/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package tictactoe.user;

import TicTacToeCommon.models.UserModel;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import tictactoe.base.ViewModelListener;
import tictactoe.components.gamerecordcell.GameRecordCell;
import tictactoe.components.userview.UserViewController;
import tictactoe.resources.styles.Styles;
import tictactoe.router.RouteViewController;
import tictactoe.utils.UIHelper;

public class UserAccountViewController extends RouteViewController implements ViewModelListener<Boolean> {

    @FXML
    private Region background;
    @FXML
    private Label gameTitle;
    @FXML
    private Button backButton;
    @FXML
    private Button startButton;
    @FXML
    private StackPane userAccountParent;
    @FXML
    private ListView<String> playersList;
    
    private UserAccountViewModel viewModel;

    @Override
    public URL getViewUri() {
        return getClass().getResource("UserAccountView.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewModel = new UserAccountViewModel(handle());
        scene().getStylesheets().add(resourcesLoader().getCss(Styles.USER_STYLE_STRING).toString());
        background.setEffect(UIHelper.createBlurEffect());
        backButton.setOnAction(router()::pop);
        playersList.setItems(viewModel.getData());
        playersList.setCellFactory((e) -> {
            return new GameRecordCell(this);
        });
        startButton.setOnAction((event) -> {
            ObservableList<String> selected = playersList.getSelectionModel().getSelectedItems();
            if (!selected.isEmpty()) {
                String selectedGameId = selected.get(0);
                viewModel.loadGame(selectedGameId);
                uIAlert().showLoadingDialog("Fetching", "Loading your record");
            }
        });
        userAccountParent.getChildren().add(attachController(new UserViewController()));
        viewModel.bind(this);
    }

    @Override
    public void didUpdateState(Boolean newState) {
        Platform.runLater(() -> {
            if (newState == false) {
                uIAlert().showErrorDialog("Failed", "Failed to load game");
            } else if (newState == true) {
                uIAlert().close();
            }
        });
    }

    @Override
    public void onClosed() {
        viewModel.unbind(this);
    }
    
}
