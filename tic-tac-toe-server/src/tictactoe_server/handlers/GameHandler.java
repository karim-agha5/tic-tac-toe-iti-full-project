/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictactoe_server.handlers;

import java.io.Serializable;

public interface GameHandler {

    String getGameId();
    
    String[] getPlayersIds();
    
    boolean canProcess(String userId, Serializable data);

    void process(String userId, Serializable data);

    void stop();

}
