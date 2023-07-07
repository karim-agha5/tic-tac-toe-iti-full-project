/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictactoe_server.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface EntityMapper<T> {

    T mapToEntity(ResultSet resultSet) throws SQLException;

    void writeToSet(T entity, ResultSet resultSet) throws SQLException;

    default void writeDefaults(T entity, ResultSet resultSet) throws SQLException {
    }
}
