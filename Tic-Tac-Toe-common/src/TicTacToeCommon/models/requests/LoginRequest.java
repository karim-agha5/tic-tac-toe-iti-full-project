package TicTacToeCommon.models.requests;

import TicTacToeCommon.models.base.RemoteRequest;

public class LoginRequest implements RemoteRequest {

    static final long serialVersionUID = 42L;
    private String username;
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
