package TicTacToeCommon.models.requests;

public class GameMoveRequest extends GameRequest {

    static final long serialVersionUID = 42L;
    private Integer move;

    public GameMoveRequest() {

    }

    public GameMoveRequest(String gameId, Integer move) {
        super(gameId);
        this.move = move;
    }

    public Integer getMove() {
        return move;
    }

    public void setMove(Integer move) {
        this.move = move;
    }

}
