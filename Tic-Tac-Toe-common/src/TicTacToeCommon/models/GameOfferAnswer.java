/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TicTacToeCommon.models;

import java.io.Serializable;

/**
 *
 * @author m-essam
 */
public class GameOfferAnswer implements Serializable {

    static final long serialVersionUID = 42L;
    private Boolean wantToPlay;
    private String opponentId;

    public GameOfferAnswer() {

    }

    public GameOfferAnswer(Boolean wantToPlay, String opponent) {
        this.wantToPlay = wantToPlay;
        this.opponentId = opponent;
    }

    public Boolean getWantToPlay() {
        return wantToPlay;
    }

    public void setWantToPlay(Boolean wantToPlay) {
        this.wantToPlay = wantToPlay;
    }

    public String getOpponent() {
        return opponentId;
    }

    public void setOpponent(String opponentId) {
        this.opponentId = opponentId;
    }

}
