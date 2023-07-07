package tictactoe_server.data.impl;

import TicTacToeCommon.models.UserModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import tictactoe_server.data.DatabaseManager;
import tictactoe_server.data.ResultPacket;
import tictactoe_server.data.UserDAO;

public class UserDAOImpl implements UserDAO {

    private final DatabaseManager dbm;

    public UserDAOImpl(DatabaseManager dbm) {
        this.dbm = dbm;
    }

    @Override
    public ResultPacket findById(String id) throws SQLException {
        Connection conn = dbm.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Users WHERE Id=?", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        stmt.setString(1, id);
        ResultSet resultSet = stmt.executeQuery();
        ResultPacket packet = new ResultPacket(resultSet, stmt);
        return packet;
    }

    @Override
    public ResultPacket findByUsernameAndPassword(String name, String password) throws SQLException {
        Connection conn = dbm.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * from Users where Username=? And Password=?", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        stmt.setString(1, name);
        stmt.setString(2, password);
        ResultSet resultSet = stmt.executeQuery();
        return new ResultPacket(resultSet, stmt);
    }

    @Override
    public String createUser(UserModel user, String password) throws SQLException {
        Connection conn = dbm.getConnection();
        try (PreparedStatement preparedStmt = conn.prepareStatement("INSERT INTO Users (Id, Username, Password ,CreatedAt) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            preparedStmt.setString(1, user.getId());
            preparedStmt.setString(2, user.getName());
            preparedStmt.setString(3, password);
            preparedStmt.setObject(4, user.getCreatedAt());
            preparedStmt.executeUpdate();
            ResultSet keys = preparedStmt.getGeneratedKeys();
            if (keys.next()) {
                return keys.getString(1);
            }
            return null;
        }
    }

    @Override
    public long getUsersCount() throws SQLException {
        
        Connection connection = dbm.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM Users");
        ResultSet resultSet = preparedStatement.executeQuery();
        
        if(resultSet.next()){
            return resultSet.getLong(1);
        }
        
        return 0;
    }

}
