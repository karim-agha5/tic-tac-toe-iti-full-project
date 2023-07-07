package tictactoe_server.data.impl;

import TicTacToeCommon.models.GameModel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import tictactoe_server.data.DatabaseManager;
import tictactoe_server.data.GameDAO;
import tictactoe_server.data.ResultPacket;

/**
 *
 * @author Karim
 * @version 1.1
 * @since 1.0
 */
public class GameDAOImpl implements GameDAO {

    private final DatabaseManager databaseManager;

    public GameDAOImpl(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public ResultPacket getGameById(String id) throws SQLException {
        String queryString = "SELECT * FROM Games WHERE Id = ?";
        PreparedStatement statement = databaseManager.getConnection().prepareStatement(queryString, ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_UPDATABLE);
        statement.setString(1, id);
        return new ResultPacket(statement.executeQuery(), statement);
    }

    @Override
    public ResultPacket getGamesByPlayerId(String id) throws SQLException {
        String queryString = "SELECT * FROM Games WHERE Player1Id = ? OR Player2Id = ?";
        PreparedStatement statement = databaseManager.getConnection().prepareStatement(queryString, ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY);
        statement.setString(1, id);
        statement.setString(2, id);
        return new ResultPacket(statement.executeQuery(), statement);
    }

    @Override
    public String createGame(GameModel game) throws SQLException {
        String queryString = "INSERT INTO Games (Id,Player1Id,Player2Id,CreatedAt) VALUES(?,?,?,?)";
        try (PreparedStatement statement = databaseManager.getConnection().prepareStatement(queryString, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, game.getGameId());
            statement.setString(2, game.getPlayer1Id());
            statement.setString(3, game.getPlayer2Id());
            statement.setLong(4, game.getCreatedAt());
            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                return keys.getString("id");
            }
            return null;
        }
    }
}
