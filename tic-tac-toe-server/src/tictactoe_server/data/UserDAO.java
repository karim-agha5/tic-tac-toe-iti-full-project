package tictactoe_server.data;

import TicTacToeCommon.models.UserModel;
import java.sql.SQLException;

public interface UserDAO {

    ResultPacket findById(String id) throws SQLException;

    String createUser(UserModel user, String password) throws SQLException;

    ResultPacket findByUsernameAndPassword(String name, String password) throws SQLException;

    long getUsersCount() throws SQLException;
}
