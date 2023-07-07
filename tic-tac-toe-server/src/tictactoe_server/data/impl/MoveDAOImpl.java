/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe_server.data.impl;

import TicTacToeCommon.models.MoveModel;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import tictactoe_server.data.DatabaseManager;
import tictactoe_server.data.MoveDAO;
import tictactoe_server.data.ResultPacket;

/**
 *
 * @author ITI
 */
public class MoveDAOImpl implements MoveDAO {

    private final DatabaseManager dbm;

    public MoveDAOImpl(DatabaseManager dbm) {
        this.dbm = dbm;
    }

    @Override
    public ResultPacket findMoveById(String id) throws SQLException {
        String SEARCH_MOVE_SQL = "SELECT INTO Moves WHERE Id = ? ";
        PreparedStatement ps = dbm.getConnection().prepareStatement(SEARCH_MOVE_SQL);
        ps.setString(1, id);
        ResultSet result = ps.executeQuery();
        return new ResultPacket(result, ps);
    }

    @Override
    public ResultPacket findMoveByGameId(String gameId) throws SQLException {
        String SEARCH_MOVE_SQL = "SELECT INTO Moves WHERE Id = ? ";
        PreparedStatement ps = dbm.getConnection().prepareStatement(SEARCH_MOVE_SQL);
        ps.setString(1, gameId);
        ResultSet result = ps.executeQuery();
        return new ResultPacket(result, ps);
    }

    @Override
    public String createMove(MoveModel moveModel) throws SQLException {
        String INSERT_MOVE_SQL = "INSERT INTO Moves(Id, PlayerId, GameId, SpacePosition, CreatedAt) VALUES(?,?,?,?,?) ";
        try (
                PreparedStatement ps = dbm.getConnection().prepareStatement(INSERT_MOVE_SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, moveModel.getId());
            ps.setString(2, moveModel.getPlayerId());
            ps.setString(3, moveModel.getGameId());
            ps.setInt(4, moveModel.getSpacePosition());
            ps.setLong(5, moveModel.getCreatedAt());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                return keys.getString("id");
            }
            return null;
        }
    }

}
