/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictactoe_server.managers;

import java.io.Serializable;
import tictactoe_server.handlers.GameHandler;


public interface GamesManager {

    boolean canProcess(String userId, Serializable data);

    void process(String userId, Serializable data);
    
    void setOngoingHandler(GameHandler handler);
    
    void removeHandler(GameHandler handler);
    
    void stop() throws Exception;
    
}
