/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictactoe_server.mappers.impl;

import TicTacToeCommon.models.UserModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import tictactoe_server.mappers.EntityMapper;

public class UserMapper implements EntityMapper<UserModel> {

    @Override
    public UserModel mapToEntity(ResultSet resultSet) throws SQLException {
        UserModel userModel = new UserModel();
        userModel.setId(resultSet.getString("Id"));
        userModel.setName(resultSet.getString("Username"));
        userModel.setCreatedAt(resultSet.getLong("CreatedAt"));
        return userModel;
    }

    @Override
    public void writeToSet(UserModel entity, ResultSet resultSet) throws SQLException {
        resultSet.updateString("Id", entity.getId());
        resultSet.updateString("Username", entity.getName());
        resultSet.updateLong("CreatedAt", entity.getCreatedAt());
    }
}
