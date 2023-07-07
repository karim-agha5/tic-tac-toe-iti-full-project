/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictactoe.game.providers;

import TicTacToeCommon.models.UserModel;
import TicTacToeCommon.models.events.GameEvent;
import TicTacToeCommon.services.engine.TicTacToeEngine;
import TicTacToeCommon.services.engine.TicTacToeEngine.InvalidMoveException;
import java.util.Objects;
import tictactoe.authentication.AuthenticationProvider;
import tictactoe.utils.ObjectUtils;

/**
 *
 * @author m-essam
 */
public class MultiplayerGameHandler extends LocalGameHandler {

    public MultiplayerGameHandler(AuthenticationProvider authenticationProvider) {
        super(new TicTacToeEngine(ObjectUtils.getOrElse(authenticationProvider.getUser().getValue(), PLAYER_1), PLAYER_2));
    }

    @Override
    public void start() {
    }
    
    @Override
    public void makeMove(Integer position) {
        try {
            makeUserMove(position);
        } catch (InvalidMoveException e) {
            lastMoveResult.setValue(false);
        }
    }

    @Override
    public void withdraw() {
        UserModel player = Objects.equals(currentPlayer.getValue(), FIRST_PLAYER) ? player2 : player1;
        winner = player;
        events.setValue(new GameEvent.Won(gameId));
    }
}
