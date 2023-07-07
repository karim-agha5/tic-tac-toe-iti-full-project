/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package tictactoe.authentication;

import TicTacToeCommon.models.base.RemoteResponse;
import TicTacToeCommon.utils.ObservableValue;
import java.util.logging.Level;
import java.util.logging.Logger;
import tictactoe.base.ViewModel;

public class AuthenticationViewModel extends ViewModel<Boolean> implements ObservableValue.Observer {

    private final AuthenticationProvider authenticationProvider;
    private ObservableValue.ListenCanceller socketSub;
    private ObservableValue.ListenCanceller messageSub;

    public AuthenticationViewModel(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    public void start() {
        socketSub = authenticationProvider.getIsAuth().addListener(this);
        messageSub = authenticationProvider.getAuthError().addListener(this);
        authenticationProvider.openSocket();
    }

    @Override
    public void didChange(Object value) {
        if (value instanceof Boolean) {
            updateState((Boolean) value);
        } else if (value instanceof RemoteResponse) {
            if (!((RemoteResponse) value).isStatus()) {
                updateState(false);
            }
        }
    }

    @Override
    public void stop() {
        socketSub.cancel();
        messageSub.cancel();
        try {
            authenticationProvider.closeSocketIfUnAuth();
        } catch (Exception ex) {
            Logger.getLogger(AuthenticationViewModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
