/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe_server.mappers.impl;

import TicTacToeCommon.models.MoveModel;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import tictactoe_server.mappers.EntityMapper;

/**
 *
 * @author ITI
 */
public class MoveMapper implements EntityMapper<MoveModel> {

    @Override
    public MoveModel mapToEntity(ResultSet resultSet) throws SQLException {
        MoveModel moveModel = new MoveModel();
        moveModel.setId(resultSet.getString("Id"));
        moveModel.setGameId(resultSet.getString("GameId"));
        moveModel.setPlayerId(resultSet.getString("PlayerId"));
        moveModel.setSpacePosition(resultSet.getInt("SpacePosition"));
        moveModel.setCreatedAt(resultSet.getLong("CreatedAt"));
        return moveModel;
    }

    @Override
    public void writeToSet(MoveModel entity, ResultSet resultSet) throws SQLException {
        resultSet.updateString("Id", entity.getId());
        resultSet.updateString("PlayerId", entity.getPlayerId());
        resultSet.updateString("GameId", entity.getGameId());
        resultSet.updateInt("SpacePosition", entity.getSpacePosition());
        resultSet.updateLong("CreatedAt", entity.getCreatedAt());
    }

}
