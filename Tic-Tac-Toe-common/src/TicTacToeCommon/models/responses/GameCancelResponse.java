package TicTacToeCommon.models.responses;

import TicTacToeCommon.models.base.RemoteResponse;

public class GameCancelResponse extends RemoteResponse<Boolean> {

    static final long serialVersionUID = 42L;

    public GameCancelResponse() {
    }

    public GameCancelResponse(boolean status) {
        super(status);
    }

    /**
     * @param status wether the request was successful
     * @param data the move that was successful
     */
    public GameCancelResponse(boolean status, Boolean data) {
        super(status, data);
    }

}
