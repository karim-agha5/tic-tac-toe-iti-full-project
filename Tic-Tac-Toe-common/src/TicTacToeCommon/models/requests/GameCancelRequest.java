/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TicTacToeCommon.models.requests;

import TicTacToeCommon.models.base.RemoteRequest;

/**
 *
 * @author m-essam
 */
public class GameCancelRequest implements RemoteRequest {

    static final long serialVersionUID = 42L;
    private String playerId;

    public GameCancelRequest() {

    }

    public GameCancelRequest(String playerId) {
        this.playerId = playerId;
    }

    public String getOpponent() {
        return playerId;
    }

    public void setOpponent(String playerId) {
        this.playerId = playerId;
    }

}
