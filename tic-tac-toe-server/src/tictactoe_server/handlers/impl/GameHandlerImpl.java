/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictactoe_server.handlers.impl;

import TicTacToeCommon.models.GameModel;
import TicTacToeCommon.models.MoveModel;
import TicTacToeCommon.models.UserModel;
import TicTacToeCommon.models.base.RemoteSendable;
import TicTacToeCommon.models.events.GameEvent;
import TicTacToeCommon.models.requests.GameMoveRequest;
import TicTacToeCommon.models.requests.GameRequest;
import TicTacToeCommon.models.requests.GameWithdrawRequest;
import TicTacToeCommon.models.responses.GameMoveResponse;
import TicTacToeCommon.models.responses.GameWithdrawResponse;
import TicTacToeCommon.services.engine.TicTacToeEngine;
import TicTacToeCommon.services.engine.piece.League;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import tictactoe_server.handlers.GameHandler;
import tictactoe_server.managers.ServerSocketManager;

import static TicTacToeCommon.services.engine.TicTacToeEngine.GameResult.*;
import TicTacToeCommon.services.engine.TicTacToeEngine.InvalidMoveException;

public class GameHandlerImpl implements GameHandler {

    private final List<MoveModel> moves = new LinkedList<>();
    private final ServerSocketManager serverSocketManager;
    private final List<String> players;
    private final TicTacToeEngine engine;
    private final GameModel gameModel;

    public GameHandlerImpl(String player1Id, String player2Id, ServerSocketManager serverSocketManager) {
        this.players = Arrays.asList(player1Id, player2Id);
        this.serverSocketManager = serverSocketManager;
        UserModel player1 = serverSocketManager.getClientsManager().getUser(player1Id);
        UserModel player2 = serverSocketManager.getClientsManager().getUser(player2Id);
        this.engine = new TicTacToeEngine(player1, player2);
        this.gameModel = new GameModel(UUID.randomUUID().toString(), players.get(0), players.get(1), new Date().getTime());
        startGame(player1, player2);
    }

    private void startGame(UserModel player1, UserModel player2) {
        serverSocketManager.getClientsManager().send(player1.getId(),
                new GameEvent.Started(gameModel.getGameId(), gameModel,
                        player2, engine.getLeague(player1.getId())));
        serverSocketManager.getClientsManager().setIsPlaying(player1.getId(), true);
        serverSocketManager.getClientsManager().send(player2.getId(),
                new GameEvent.Started(gameModel.getGameId(), gameModel,
                        player1, engine.getLeague(player2.getId())));
        serverSocketManager.getClientsManager().setIsPlaying(player2.getId(), true);
        serverSocketManager.getGamesManager().setOngoingHandler(this);

    }

    private void broadcast(GameEvent event) {
        for (String userId : players) {
            send(userId, event);
        }
    }

    private void sendToOpponent(String playerId, GameEvent event) {
        for (String userId : players) {
            if (!Objects.equals(playerId, userId)) {
                send(userId, event);
                return;
            }
        }
    }

    private void send(String playerId, RemoteSendable event) {
        serverSocketManager.getClientsManager().send(playerId, event);
    }

    @Override
    public String getGameId() {
        if (gameModel != null) {
            return gameModel.getGameId();
        }
        return null;
    }

    @Override
    public String[] getPlayersIds() {
        return players.toArray(new String[0]);
    }

    @Override
    public boolean canProcess(String userId, Serializable data) {
        if (!players.contains(userId)) {
            return false;
        }
        if (data instanceof GameRequest) {
            String gameId = ((GameRequest) data).getGameId();
            return gameId == null || gameId.equals(getGameId());
        }
        return false;
    }

    @Override
    public void process(String userId, Serializable data) {
        if (data instanceof GameWithdrawRequest) {
            sendToOpponent(userId, new GameEvent.Withdraw(userId));
            send(userId, new GameWithdrawResponse(true, getGameId()));
            remove();
            stop();
        } else if (data instanceof GameMoveRequest) {
            GameMoveRequest moveRequest = (GameMoveRequest) data;
            try {
                TicTacToeEngine.GameResult result = engine.makeMove(userId, moveRequest.getMove());
                send(userId, new GameMoveResponse(true, true));
                MoveModel move = new MoveModel(UUID.randomUUID().toString(), userId,
                        getGameId(), moveRequest.getMove(),
                        new Date().getTime());
                moves.add(move);
                broadcast(new GameEvent.Moved(getGameId(), userId, move));
                checkEndGame(result);
            } catch (InvalidMoveException e) {
                send(userId, new GameMoveResponse(true, false));
            }
        }
    }

    private boolean checkEndGame(TicTacToeEngine.GameResult result) throws InvalidMoveException {
        if (result != ONGOING) {
            UserModel cross = engine.getPlayer(League.Cross);
            UserModel nougth = engine.getPlayer(League.Nought);
            switch (result) {
                case CROSS_WINS:
                    send(cross.getId(), new GameEvent.Won(getGameId()));
                    send(nougth.getId(), new GameEvent.Lost(getGameId()));
                    break;
                case NOUGHT_WINS:
                    send(nougth.getId(), new GameEvent.Won(getGameId()));
                    send(cross.getId(), new GameEvent.Lost(getGameId()));
                    break;
                case DRAW:
                    send(nougth.getId(), new GameEvent.Draw(getGameId()));
                    send(cross.getId(), new GameEvent.Draw(getGameId()));
                    break;
            }
            remove();
            return true;
        }
        return false;
    }

    private void remove() {
        for (String userId : players) {
            serverSocketManager.getClientsManager().setIsPlaying(userId, false);
        }
        serverSocketManager.getGamesManager().removeHandler(this);
    }

    @Override
    public void stop() {
        broadcast(new GameEvent.Ended(getGameId()));
    }
}
