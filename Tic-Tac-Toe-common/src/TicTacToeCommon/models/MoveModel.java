package TicTacToeCommon.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Represents a single move at a certain space on the grid.
 *
 * @author Karim
 * @version 1.0
 * @since 1.0
 *
 */
public class MoveModel implements Serializable {

    static final long serialVersionUID = 42L;

    private String id;
    private String playerId;
    private String gameId;
    private Integer spacePosition;
    private Long createdAt;

    /**
     * Default Constructor
     */
    public MoveModel() {
    }

    /**
     * Creates a single a move associated with a player's ID, a game's ID, and
     * in a certain space position on the grid.
     *
     * @param id
     * @param playerId The ID of the player that took the move.
     * @param gameId the ID of the game where this move was taken.
     * @param spacePosition The exact position where the move was taken on the
     * grid.
     * @param createdAt The exact time this move was taken.
     *
     */
    public MoveModel(String id, String playerId, String gameId, Integer spacePosition, Long createdAt) {
        this.id = id;
        this.playerId = playerId;
        this.gameId = gameId;
        this.spacePosition = spacePosition;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public Integer getSpacePosition() {
        return spacePosition;
    }

    public void setSpacePosition(Integer spacePosition) {
        this.spacePosition = spacePosition;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
}
