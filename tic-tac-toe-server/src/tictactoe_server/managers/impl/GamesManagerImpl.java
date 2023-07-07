/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictactoe_server.managers.impl;

import TicTacToeCommon.models.UserModel;
import TicTacToeCommon.models.GameOfferAnswer;
import TicTacToeCommon.models.requests.GameCancelRequest;
import TicTacToeCommon.models.requests.GameMoveRequest;
import TicTacToeCommon.models.requests.GameWithdrawRequest;
import TicTacToeCommon.models.requests.JoinGameRequest;
import TicTacToeCommon.models.requests.StartGameRequest;
import TicTacToeCommon.models.responses.GameCancelResponse;
import TicTacToeCommon.models.responses.JoinGameResponse;
import TicTacToeCommon.models.responses.StartGameResponse;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import tictactoe_server.handlers.GameHandler;
import tictactoe_server.handlers.impl.GameHandlerImpl;
import tictactoe_server.managers.ClientsManager;
import tictactoe_server.managers.GamesManager;
import tictactoe_server.managers.ServerSocketManager;

public class GamesManagerImpl implements GamesManager {

    private final ServerSocketManager serverSocketManager;
    private final List<PendingRequest> pendingRequests = new LinkedList<>();
    private final List<GameHandler> gameHandlers = new LinkedList<>();
    private final Map<String, GameHandler> activeGames = new HashMap<>();

    public GamesManagerImpl(ServerSocketManager serverSocketManager) {
        this.serverSocketManager = serverSocketManager;
    }

    @Override
    public boolean canProcess(String userId, Serializable data) {
        return data instanceof GameMoveRequest
                || data instanceof GameWithdrawRequest
                || data instanceof GameCancelRequest
                || data instanceof StartGameRequest
                || data instanceof JoinGameResponse;
    }

    @Override
    public void process(String userId, Serializable data) {
        ClientsManager clientManager = serverSocketManager.getClientsManager();
        if (data instanceof GameCancelRequest) {
            GameCancelRequest cancelGameRequest = (GameCancelRequest) data;
            String player1Id = userId;
            String player2Id = cancelGameRequest.getOpponent();
            if (haveActivePendingRequest(player1Id, player2Id)) {
                removeUsersPendingRequests(player1Id, player2Id);
                clientManager.send(player1Id,
                        new GameCancelResponse(true, true));
            } else {
                clientManager.send(player1Id,
                        new GameCancelResponse(false));
            }
        } else if (data instanceof StartGameRequest) {
            StartGameRequest startGameRequest = (StartGameRequest) data;
            String player1Id = userId;
            UserModel player2 = startGameRequest.getPlayer();
            if (canPlay(player1Id, player2.getId())) {
                PendingRequest request = new PendingRequest(player1Id, player2.getId());
                pendingRequests.add(request);
                clientManager.send(player2.getId(),
                        new JoinGameRequest(clientManager.getUser(player1Id)));
            } else {
                clientManager.send(player1Id, new StartGameResponse(false));
            }
        } else if (data instanceof JoinGameResponse) {
            JoinGameResponse joinGameResponse = (JoinGameResponse) data;
            String player1Id = userId;
            GameOfferAnswer answer = joinGameResponse.getData();
            String player2Id = answer.getOpponent();
            if (!answer.getWantToPlay()) {
                removeUsersPendingRequests(player1Id, player2Id);
                clientManager.send(player2Id, new StartGameResponse(true, false));
            } else if (canPlay(player1Id, answer.getOpponent()) && haveActivePendingRequest(player1Id, player2Id)) {
                removeUsersPendingRequests(player1Id, player2Id);
                clientManager.send(player2Id, new StartGameResponse(true, true));
                GameHandler gameHandler = new GameHandlerImpl(player1Id, player2Id, serverSocketManager);
                gameHandlers.add(gameHandler);
            } else {
                clientManager.send(player1Id, new StartGameResponse(false));
            }
        } else {
            for (GameHandler handler : activeGames.values()) {
                if (handler.canProcess(userId, data)) {
                    handler.process(userId, data);
                }
            }
        }
    }

    private boolean canPlay(String player1Id, String player2Id) {
        return serverSocketManager.getClientsManager().isAvailableToPlay(player1Id)
                && serverSocketManager.getClientsManager().isAvailableToPlay(player2Id);
    }

    private boolean haveActivePendingRequest(String player1, String player2) {
        for (PendingRequest request : pendingRequests) {
            return request.players.contains(player1) && request.players.contains(player2) && !request.isCancelled;
        }
        return false;
    }

    private void removeUsersPendingRequests(String player1, String player2) {
        pendingRequests.removeIf((e) -> {
            return e.players.contains(player1) && e.players.contains(player2);
        });
    }

    @Override
    public void setOngoingHandler(GameHandler handler) {
        activeGames.put(handler.getGameId(), handler);
    }

    @Override
    public void removeHandler(GameHandler handler) {
        activeGames.remove(handler.getGameId());
        gameHandlers.remove(handler);
    }

    @Override
    public void stop() throws Exception {
        for (GameHandler handler : gameHandlers) {
            handler.stop();
        }
        gameHandlers.clear();
        activeGames.clear();
        pendingRequests.clear();
    }

    static class PendingRequest {

        private final List<String> players;
        private boolean isCancelled;

        public PendingRequest(String player1, String player2) {
            this.players = Arrays.asList(player1, player2);
            this.isCancelled = false;
        }
    }
}
