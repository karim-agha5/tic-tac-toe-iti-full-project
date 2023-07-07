/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictactoe.game.providers;

import TicTacToeCommon.models.events.GameEvent;
import TicTacToeCommon.services.engine.TicTacToeEngine;
import TicTacToeCommon.services.engine.TicTacToeEngine.InvalidMoveException;
import java.util.logging.Level;
import java.util.logging.Logger;
import tictactoe.authentication.AuthenticationProvider;
import tictactoe.utils.ObjectUtils;

/**
 *
 * @author m-essam
 */
public class RobotGameHandler extends LocalGameHandler {

    private final TicTacToeEngine.Difficulty difficulty;

    public RobotGameHandler(AuthenticationProvider authenticationProvider, TicTacToeEngine.Difficulty difficulty) {
        super(new TicTacToeEngine(ObjectUtils.getOrElse(authenticationProvider.getUser().getValue(), LOCAL_PLAYER)));
        this.difficulty = difficulty;
    }

    @Override
    public void start() {
        if (player1.getId().equals(TicTacToeEngine.AI.getId())) {
            try {
                makeAIMove(difficulty);
                
            } catch (InvalidMoveException ex) {
                Logger.getLogger(RobotGameHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void makeMove(Integer position) {
        try {
            if (!makeUserMove(position)) {
                makeAIMove(difficulty);
            }
        } catch (InvalidMoveException e) {
            lastMoveResult.setValue(false);
        }
    }

    @Override
    public void withdraw() {
        events.setValue(new GameEvent.Lost(gameId));
    }
}
