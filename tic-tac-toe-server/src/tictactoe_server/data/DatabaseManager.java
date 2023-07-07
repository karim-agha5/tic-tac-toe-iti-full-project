/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictactoe_server.data;

import java.sql.Connection;
import java.sql.SQLException;


public interface DatabaseManager {

    void start() throws SQLException;

    Connection getConnection() throws SQLException;

    void stop() throws SQLException;
}
