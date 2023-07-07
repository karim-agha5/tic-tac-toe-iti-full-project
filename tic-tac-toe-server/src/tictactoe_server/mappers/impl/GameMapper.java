/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe_server.mappers.impl;

import TicTacToeCommon.models.GameModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import tictactoe_server.mappers.EntityMapper;

/**
 *
 * @author Karim
 */
public class GameMapper implements EntityMapper<GameModel> {

    @Override
    public GameModel mapToEntity(ResultSet resultSet) throws SQLException {
        String gameId = resultSet.getString("Id");
        String player1Id = resultSet.getString("Player1Id");
        String player2Id = resultSet.getString("Player2Id");
        Long createdAt = resultSet.getLong("CreatedAt");
        return new GameModel(gameId, player1Id, player2Id, createdAt);
    }

    @Override
    public void writeToSet(GameModel entity, ResultSet resultSet) throws SQLException {
        resultSet.updateString("Id", entity.getGameId());
        resultSet.updateString("Player1Id", entity.getPlayer1Id());
        resultSet.updateString("Player2Id", entity.getPlayer2Id());
        resultSet.updateLong("CreatedAt", entity.getCreatedAt());
    }

}
