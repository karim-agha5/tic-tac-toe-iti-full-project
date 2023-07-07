/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package tictactoe.online.players;

import TicTacToeCommon.models.UserModel;
import TicTacToeCommon.models.base.RemoteSendable;
import TicTacToeCommon.models.requests.StartGameRequest;
import TicTacToeCommon.models.responses.StartGameResponse;
import TicTacToeCommon.utils.ObservableValue;
import java.util.logging.Level;
import java.util.logging.Logger;
import tictactoe.base.SocketHandler;
import tictactoe.base.ViewModel;

public class OnlineRequestViewModel extends ViewModel<Boolean> implements ObservableValue.Observer<RemoteSendable> {

    private final SocketHandler socketHandler;
    private ObservableValue.ListenCanceller socketSub;

    public OnlineRequestViewModel(SocketHandler socketHandler) {
        this.socketHandler = socketHandler;
    }

    @Override
    public void start() {
        socketSub = socketHandler.getMessage().addListener(this);
    }

    @Override
    public void didChange(RemoteSendable message) {
        if (message instanceof StartGameResponse) {
            StartGameResponse response = (StartGameResponse) message;
            updateState(response.isStatus() && response.getData());
        }
    }

    @Override
    public void stop() {
        socketSub.cancel();
    }

    void sendRequestTo(UserModel selectedUser) {
        try {
            socketHandler.send(new StartGameRequest(selectedUser));
        } catch (SocketHandler.NotConnectedException ex) {
            updateState(false);
            Logger.getLogger(OnlineRequestViewModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
