package TicTacToeCommon.models;

import java.io.Serializable;

public class RecordModel implements Serializable{

    static final long serialVersionUID = 42L;
    private String gameId;
    private String playerOneId;
    private String playerTwoId;

    public RecordModel() {
    }

    public RecordModel(String gameId, String playerOneId, String playerTwoId) {
        this.gameId = gameId;
        this.playerOneId = playerOneId;
        this.playerTwoId = playerTwoId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public void setPlayerOneId(String playerOneId) {
        this.playerOneId = playerOneId;
    }

    public void setPlayerTwoId(String playerTwoId) {
        this.playerTwoId = playerTwoId;
    }

    public String getGameId() {
        return gameId;
    }

    public String getPlayerOneId() {
        return playerOneId;
    }

    public String getPlayerTwoId() {
        return playerTwoId;
    }

}
