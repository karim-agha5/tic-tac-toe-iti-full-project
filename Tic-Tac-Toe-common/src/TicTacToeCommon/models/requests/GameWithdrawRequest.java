package TicTacToeCommon.models.requests;

public class GameWithdrawRequest extends GameRequest {

    static final long serialVersionUID = 42L;

    public GameWithdrawRequest() {

    }

    public GameWithdrawRequest(String gameId) {
        super(gameId);
    }

}
