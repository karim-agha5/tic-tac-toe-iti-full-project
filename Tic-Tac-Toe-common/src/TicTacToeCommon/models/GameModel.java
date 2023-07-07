package TicTacToeCommon.models;

import java.io.Serializable;

public class GameModel implements Serializable {

    static final long serialVersionUID = 42L;
    private String gameId;
    private String player1Id;
    private String player2Id;
    private Long createdAt;

    public GameModel() {
    }

    public GameModel(String gameId, String player1Id, String player2Id, Long createdAt) {
        this.gameId = gameId;
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.createdAt = createdAt;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public void setPlayer1Id(String player1Id) {
        this.player1Id = player1Id;
    }

    public void setPlayer2Id(String player2Id) {
        this.player2Id = player2Id;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public String getGameId() {
        return gameId;
    }

    public String getPlayer1Id() {
        return player1Id;
    }

    public String getPlayer2Id() {
        return player2Id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }
}
