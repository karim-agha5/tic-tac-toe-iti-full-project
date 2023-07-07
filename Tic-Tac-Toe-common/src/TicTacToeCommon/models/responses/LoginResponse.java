package TicTacToeCommon.models.responses;

import TicTacToeCommon.models.UserModel;
import TicTacToeCommon.models.base.RemoteResponse;

public class LoginResponse extends RemoteResponse<UserModel> {

    static final long serialVersionUID = 42L;
    public LoginResponse() {
    }

    public LoginResponse(boolean status) {
        super(status);
    }

    public LoginResponse(boolean status, UserModel data) {
        super(status, data);
    }

}
