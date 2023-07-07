/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictactoe.authentication;

import TicTacToeCommon.models.UserModel;
import TicTacToeCommon.models.base.RemoteResponse;
import TicTacToeCommon.models.base.RemoteSendable;
import TicTacToeCommon.models.requests.LoginRequest;
import TicTacToeCommon.models.requests.SignUpRequest;
import TicTacToeCommon.models.responses.LoginResponse;
import TicTacToeCommon.models.responses.SignUpResponse;
import TicTacToeCommon.utils.MutableObservableValue;
import TicTacToeCommon.utils.ObservableValue;
import javafx.application.Platform;
import tictactoe.base.SocketHandler;

/**
 *
 * @author m-essam
 */
public class AuthenticationProvider implements AutoCloseable, ObservableValue.Observer {

    private final MutableObservableValue<Boolean> isAuth = new MutableObservableValue<>(false);
    private final MutableObservableValue<RemoteResponse> authError = new MutableObservableValue<>(null);
    private final MutableObservableValue<UserModel> user = new MutableObservableValue<>();
    private final SocketHandler socketHandler;
    private final ObservableValue.ListenCanceller connected;
    private final ObservableValue.ListenCanceller message;

    public AuthenticationProvider(SocketHandler socketHandler) {
        this.socketHandler = socketHandler;
        connected = socketHandler.getConnected().addListener(this);
        message = socketHandler.getMessage().addListener(this);
    }

    public void signUp(SignUpRequest request) throws SocketHandler.NotConnectedException {
        socketHandler.start();
        socketHandler.send(request);
    }

    public void login(LoginRequest request) throws SocketHandler.NotConnectedException {
        socketHandler.start();
        socketHandler.send(request);
    }

    public ObservableValue<Boolean> getIsAuth() {
        return isAuth;
    }

    public ObservableValue<UserModel> getUser() {
        return user;
    }

    public ObservableValue<RemoteResponse> getAuthError() {
        return authError;
    }

    public void openSocket() {
        socketHandler.start();
    }

    public void closeSocketIfUnAuth() throws Exception {
        if (!isAuth.getValue()) {
            socketHandler.stop();
        }
    }

    @Override
    public void close() throws Exception {
        connected.cancel();
        message.cancel();
    }

    @Override
    public void didChange(Object newValue) {
        if (newValue instanceof Boolean) {
            handleConnection((Boolean) newValue);
        } else if (newValue instanceof RemoteSendable) {
            handleMessage((RemoteSendable) newValue);
        }
    }

    private void handleConnection(Boolean isConnected) {
        if (!isConnected && isAuth.getValue()) {
            isAuth.setValue(false);
            user.setValue(null);
        }
    }

    private void handleMessage(RemoteSendable message) {
        if (message instanceof SignUpResponse) {
            SignUpResponse res = (SignUpResponse) message;
            if (res.isStatus()) {
                isAuth.setValue(true);
                user.setValue(res.getData());
            } else {
                authError.setValue(res);
            }
        } else if (message instanceof LoginResponse) {
            LoginResponse res = (LoginResponse) message;
            if (res.isStatus()) {
                isAuth.setValue(true);
                user.setValue(res.getData());
            } else {
                authError.setValue(res);
            }
        }
    }
}
