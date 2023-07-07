package tictactoe_server.data;

import TicTacToeCommon.models.GameModel;
import java.sql.SQLException;

/**
 *
 * @author Karim
 */
public interface GameDAO {
    
    String createGame(GameModel game) throws SQLException;
            
    ResultPacket getGameById(String id) throws SQLException;
    
    ResultPacket getGamesByPlayerId(String id) throws SQLException;
    
}
