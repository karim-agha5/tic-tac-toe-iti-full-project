package TicTacToeCommon.models.responses;

import TicTacToeCommon.models.UserModel;
import TicTacToeCommon.models.base.RemoteResponse;
import java.util.ArrayList;

public class OnlinePlayersResponse extends RemoteResponse<ArrayList<UserModel>> {

    static final long serialVersionUID = 42L;

    public OnlinePlayersResponse() {
    }

    public OnlinePlayersResponse(boolean status) {
        super(status);
    }

    public OnlinePlayersResponse(boolean status, ArrayList<UserModel> data) {
        super(status, data);
    }

}
