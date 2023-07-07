/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictactoe.game.providers;

import TicTacToeCommon.models.MoveModel;
import TicTacToeCommon.models.events.GameEvent;
import tictactoe.utils.GameRecordService;

/**
 *
 * @author m-essam
 */
public class RecordedGameHandler extends GameHandler {

    protected final GameRecordService.Record gameRecord;
    private int index = 0;

    public RecordedGameHandler(GameRecordService.Record gameRecord) {
        super(gameRecord.getGameId(),
                gameRecord.getPlayer1(),
                gameRecord.getPlayer1League(),
                gameRecord.getPlayer2());
        this.gameRecord = gameRecord;

    }

    @Override
    public boolean canRecord() {
        return false;
    }

    @Override
    public void start() {
        canInput.setValue(true);
        currentPlayer.setValue(FIRST_PLAYER);
    }

    @Override
    public void makeMove(Integer position) {
    }

    @Override
    public void onBoardClicked() {
        if (index >= gameRecord.getMoves().size()) {
            withdraw();
        } else {
            MoveModel move = gameRecord.getMoves().get(index);
            moves.add(move);
            events.setValue(new GameEvent.Moved(gameId, move.getPlayerId(), move));
            if (player1.getId().equals(move.getPlayerId())) {
                currentPlayer.setValue(2);
            } else {
                currentPlayer.setValue(1);
            }
            index++;
        }
    }

    @Override
    public void withdraw() {
        events.setValue(new GameEvent.Ended(gameId));
    }

}
