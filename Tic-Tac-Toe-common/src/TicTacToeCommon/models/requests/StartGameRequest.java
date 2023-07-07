package TicTacToeCommon.models.requests;

import TicTacToeCommon.models.UserModel;
import TicTacToeCommon.models.base.RemoteRequest;

public class StartGameRequest implements RemoteRequest {

    static final long serialVersionUID = 42L;
    private UserModel player;

    public StartGameRequest() {
    }

    public StartGameRequest(UserModel player) {
        this.player = player;
    }

    public UserModel getPlayer() {
        return player;
    }

    public void setPlayer(UserModel playerId) {
        this.player = playerId;
    }
}
