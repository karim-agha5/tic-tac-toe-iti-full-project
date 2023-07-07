package TicTacToeCommon.models.requests;

import TicTacToeCommon.models.UserModel;
import TicTacToeCommon.models.base.RemoteRequest;

public class JoinGameRequest implements RemoteRequest {

    static final long serialVersionUID = 42L;
    private UserModel player;

    public JoinGameRequest() {
    }

    public JoinGameRequest(UserModel player) {
        this.player = player;
    }

    public UserModel getPlayer() {
        return player;
    }

    public void setPlayer(UserModel player) {
        this.player = player;
    }

}
