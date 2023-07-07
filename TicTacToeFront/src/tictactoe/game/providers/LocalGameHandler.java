/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictactoe.game.providers;

import TicTacToeCommon.models.MoveModel;
import TicTacToeCommon.models.UserModel;
import TicTacToeCommon.models.events.GameEvent;
import TicTacToeCommon.services.engine.TicTacToeEngine;
import static TicTacToeCommon.services.engine.TicTacToeEngine.AI;
import static TicTacToeCommon.services.engine.TicTacToeEngine.GameResult.CROSS_WINS;
import static TicTacToeCommon.services.engine.TicTacToeEngine.GameResult.DRAW;
import static TicTacToeCommon.services.engine.TicTacToeEngine.GameResult.NOUGHT_WINS;
import static TicTacToeCommon.services.engine.TicTacToeEngine.GameResult.ONGOING;
import TicTacToeCommon.services.engine.move.Move;
import java.util.Date;
import java.util.UUID;

/**
 *
 * @author m-essam
 */
public abstract class LocalGameHandler extends GameHandler {

    public static final UserModel LOCAL_PLAYER = new UserModel("1", "You", null, Long.MIN_VALUE);
    public static final UserModel PLAYER_1 = new UserModel("2", "Player 1", null, Long.MIN_VALUE);
    public static final UserModel PLAYER_2 = new UserModel("3", "Player 2", null, Long.MIN_VALUE);

    protected final TicTacToeEngine engine;

    public LocalGameHandler(TicTacToeEngine engine) {
        super(UUID.randomUUID().toString(),
                engine.getPlayer(engine.getCurrentPlayer().getLeague()),
                engine.getCurrentPlayer().getLeague(),
                engine.getPlayer(engine.getCurrentPlayer().getOpponent().getLeague()));
        this.engine = engine;
        canInput.setValue(true);
        currentPlayer.setValue(FIRST_PLAYER);
    }

    protected boolean makeUserMove(Integer position) throws TicTacToeEngine.InvalidMoveException {
        UserModel player = getCurrentUser();
        TicTacToeEngine.GameResult result = engine.makeMove(player.getId(), position);
        return makeMove(player, result);
    }

    protected boolean makeAIMove(TicTacToeEngine.Difficulty difficulty) throws TicTacToeEngine.InvalidMoveException {
        UserModel player = getCurrentUser();
        TicTacToeEngine.GameResult result = engine.makeAIMove(difficulty);
        return makeMove(player, result);
    }

    private boolean makeMove(UserModel player, TicTacToeEngine.GameResult result) throws TicTacToeEngine.InvalidMoveException {
        Move postion = engine.getMoves().get(engine.getMoves().size() - 1);
        MoveModel move = new MoveModel(UUID.randomUUID().toString(),
                player.getId(), gameId, postion.getIndex(),
                new Date().getTime());
        moves.add(move);
        events.setValue(new GameEvent.Moved(gameId, player.getId(), move));
        switchCurrentPlayer();
        return checkEndGame(result);
    }

    private boolean checkEndGame(TicTacToeEngine.GameResult result) {
        if (result != ONGOING) {
            switch (result) {
                case CROSS_WINS:
                case NOUGHT_WINS:
                    winner = result == CROSS_WINS ? player1 : player2;
                    if (winner.getId().equals(AI.getId())) {
                        events.setValue(new GameEvent.Lost(gameId));
                    } else {
                        events.setValue(new GameEvent.Won(gameId));
                    }
                    break;
                case DRAW:
                    events.setValue(new GameEvent.Draw(gameId));
                    break;
            }
            return true;
        }
        return false;
    }

    protected UserModel getCurrentUser() {
        return currentPlayer.getValue().equals(FIRST_PLAYER) ? player1 : player2;
    }

    protected void switchCurrentPlayer() {
        currentPlayer.setValue(currentPlayer.getValue().equals(FIRST_PLAYER) ? SECOND_PLAYER : FIRST_PLAYER);
    }
}
