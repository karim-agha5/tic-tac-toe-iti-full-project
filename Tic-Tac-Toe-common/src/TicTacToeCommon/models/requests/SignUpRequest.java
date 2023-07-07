package TicTacToeCommon.models.requests;

import TicTacToeCommon.models.base.RemoteRequest;

/**
 * Represents a sign-up request to the server.
 * @author Karim
 * @version 1.0
 * @since 1.0
 * */
public class SignUpRequest implements RemoteRequest {
    static final long serialVersionUID = 42L;
    private String userName;
    private String password;

    /** Default Constructor */
    public SignUpRequest(){}

    /**
     * Creates a sign-up request wrapping a username and a password.
     * @param userName The requested username.
     * @param password The requested password.
     * */
    public SignUpRequest(String userName, String password) {
        
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
