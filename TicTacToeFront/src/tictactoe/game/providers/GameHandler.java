/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictactoe.game.providers;

import TicTacToeCommon.models.MoveModel;
import TicTacToeCommon.models.UserModel;
import TicTacToeCommon.models.events.GameEvent;
import TicTacToeCommon.services.engine.piece.League;
import TicTacToeCommon.utils.MutableObservableValue;
import TicTacToeCommon.utils.ObservableValue;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import tictactoe.utils.GameRecordService;

/**
 *
 * @author m-essam
 */
public abstract class GameHandler implements GameProvider {

    protected final MutableObservableValue<GameEvent> events = new MutableObservableValue<>();
    protected final MutableObservableValue<Boolean> canInput = new MutableObservableValue<>(false);
    protected final MutableObservableValue<Boolean> lastMoveResult = new MutableObservableValue<>();
    protected final MutableObservableValue<Boolean> isRecording = new MutableObservableValue<>(false);
    protected final MutableObservableValue<Integer> currentPlayer = new MutableObservableValue<>();

    protected final List<MoveModel> moves = new LinkedList<>();

    private final GameRecordService gameRecorder = new GameRecordService();

    protected final String gameId;
    protected final UserModel player1;
    protected final League player1League;
    protected final UserModel player2;
    protected final League player2League;

    protected UserModel winner;

    public GameHandler(String gameId, UserModel player1, League player1League, UserModel player2) {
        this.gameId = gameId;
        this.player1 = player1;
        this.player1League = player1League;
        this.player2 = player2;
        this.player2League = player1League.equals(League.Cross) ? League.Nought : League.Cross;
    }

    @Override
    public ObservableValue<GameEvent> getEvents() {
        return events;
    }

    @Override
    public ObservableValue<Boolean> getCanInput() {
        return canInput;
    }

    @Override
    public ObservableValue<Boolean> getLastMoveResult() {
        return lastMoveResult;
    }

    @Override
    public ObservableValue<Boolean> getIsRecording() {
        return isRecording;
    }
    
    @Override
    public ObservableValue<Integer> getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public List<MoveModel> getMoves() {
        return Collections.unmodifiableList(moves);
    }

    @Override
    public UserModel getPlayer1() {
        return player1;
    }

    @Override
    public League getPlayer1League() {
        return player1League;
    }

    @Override
    public UserModel getPlayer2() {
        return player2;
    }

    @Override
    public UserModel getWinner() {
        return winner;
    }

    @Override
    public League getPlayer2League() {
        return player2League;
    }

    @Override
    public void setIsRecording(boolean isRecording) {
        this.isRecording.setValue(canRecord() && isRecording);
    }

    @Override
    public boolean canRecord() {
        return true;
    }
    
    @Override
    public void close() {
        onClosed();
        if (isRecording.getValue()) {
            try {
                GameRecordService.Record record = new GameRecordService.Record();
                record.setGameId(gameId);
                record.setPlayer1(player1);
                record.setPlayer2(player2);
                record.setPlayer1League(player1League);
                record.setMoves(moves);
                record.setCreatedAt(new Date());
                gameRecorder.writeGame(record);
            } catch (IOException ex) {
                Logger.getLogger(GameHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void onBoardClicked() {
    }
    
    public void onClosed() {
    }
}
