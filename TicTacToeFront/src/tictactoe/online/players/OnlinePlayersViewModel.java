/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package tictactoe.online.players;

import TicTacToeCommon.models.UserModel;
import TicTacToeCommon.models.base.RemoteSendable;
import TicTacToeCommon.models.requests.OnlinePlayersRequest;
import TicTacToeCommon.models.responses.OnlinePlayersResponse;
import TicTacToeCommon.utils.ObservableValue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tictactoe.base.SocketHandler;
import tictactoe.base.ViewModel;

public class OnlinePlayersViewModel extends ViewModel<Boolean> implements ObservableValue.Observer<RemoteSendable> {

    private final ObservableList<UserModel> data;
    private final SocketHandler socketHandler;
    private ObservableValue.ListenCanceller socketSub;
    private ObservableValue.ListenCanceller connectedSub;

    public OnlinePlayersViewModel(SocketHandler socketHandler) {
        data = FXCollections.observableArrayList();
        this.socketHandler = socketHandler;
    }

    public ObservableList<UserModel> getData() {
        return data;
    }

    @Override
    public void start() {
        socketSub = socketHandler.getMessage().addListener(this);
        connectedSub = socketHandler.getConnected().addListener((newValue) -> {
            if (newValue != true) {
                updateState(false);
            }
        });
    }

    @Override
    public void didChange(RemoteSendable message) {
        if (message instanceof OnlinePlayersResponse) {
            OnlinePlayersResponse response = ((OnlinePlayersResponse) message);
            updateState(response.isStatus());
            if (response.isStatus()) {
                Platform.runLater(() -> {
                    data.setAll(response.getData());
                });
            }
        }
    }

    public void fetch() {
        try {
            socketHandler.send(new OnlinePlayersRequest());
        } catch (SocketHandler.NotConnectedException ex) {
            updateState(false);
            Logger.getLogger(OnlinePlayersViewModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void stop() {
        socketSub.cancel();
        connectedSub.cancel();
    }
}
