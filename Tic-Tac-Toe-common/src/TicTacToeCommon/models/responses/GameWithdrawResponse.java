package TicTacToeCommon.models.responses;

import TicTacToeCommon.models.base.RemoteResponse;

public class GameWithdrawResponse extends RemoteResponse<String> {
    static final long serialVersionUID = 42L;
    public GameWithdrawResponse() {
    }

    public GameWithdrawResponse(boolean status) {
        super(status);
    }

    /**
     * @param status    wether the request was successful
     * @param data      the id of the game
     */
    public GameWithdrawResponse(boolean status, String data) {
        super(status, data);
    }

}
