/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package tictactoe.user;

import TicTacToeCommon.models.base.RemoteSendable;
import TicTacToeCommon.models.requests.OnlinePlayersRequest;
import TicTacToeCommon.models.responses.OnlinePlayersResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tictactoe.base.TicTacToeHandle;
import tictactoe.base.ViewModel;
import tictactoe.game.GameViewController;
import tictactoe.game.providers.RecordedGameHandler;
import tictactoe.utils.GameRecordService;

public class UserAccountViewModel extends ViewModel<Boolean> {

    private final TicTacToeHandle handle;
    private final GameRecordService recordService;
    private final ObservableList<String> data;

    public UserAccountViewModel(TicTacToeHandle handle) {
        this.handle = handle;
        recordService = new GameRecordService();
        data = FXCollections.observableArrayList();
    }

    public ObservableList<String> getData() {
        return data;
    }

    @Override
    public void start() {
        handle.submitJob(() -> {
            List<String> files = recordService.getGameList();
            Platform.runLater(() -> data.setAll(files));
        });
    }

    public void loadGame(String id) {
        handle.submitJob(() -> {
            try {
                GameRecordService.Record record = recordService.readGame(id);
                updateState(true);
                Platform.runLater(() -> {
                    handle.router().push(new GameViewController(new RecordedGameHandler(record)));
                });
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(UserAccountViewModel.class.getName()).log(Level.SEVERE, null, ex);
                updateState(false);
            }
        });
    }

    @Override
    public void stop() {
    }
}
