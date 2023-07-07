package TicTacToeCommon.models.requests;

import TicTacToeCommon.models.base.RemoteRequest;

// TODO change Object to MoveModel

public abstract class GameRequest implements RemoteRequest {

    private String gameId;

    public GameRequest() {

    }

    public GameRequest(String gameId) {
        this.gameId = gameId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

}
