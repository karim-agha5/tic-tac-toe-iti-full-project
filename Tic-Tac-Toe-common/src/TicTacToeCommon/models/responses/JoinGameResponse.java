package TicTacToeCommon.models.responses;

import TicTacToeCommon.models.GameOfferAnswer;
import TicTacToeCommon.models.base.RemoteResponse;

public class JoinGameResponse extends RemoteResponse<GameOfferAnswer> {

    static final long serialVersionUID = 42L;

    public JoinGameResponse() {
    }

    public JoinGameResponse(boolean status) {
        super(status);
    }

    public JoinGameResponse(boolean status, GameOfferAnswer data) {
        super(status, data);
    }

}
