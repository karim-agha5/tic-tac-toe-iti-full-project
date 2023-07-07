/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe_server.data;

import TicTacToeCommon.models.MoveModel;
import java.sql.SQLException;

/**
 *
 * @author ITI
 */
public interface MoveDAO {

    public abstract ResultPacket findMoveById (String id)throws SQLException;
    public abstract ResultPacket findMoveByGameId (String gameId) throws SQLException;
    public abstract String createMove(MoveModel moveModel) throws SQLException; 
}
