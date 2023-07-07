/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictactoe_server.handlers;

import TicTacToeCommon.models.UserModel;
import TicTacToeCommon.models.base.RemoteSendable;
import java.io.IOException;

public interface ClientHandler{

    void send(RemoteSendable data);

    boolean isAuthenticated();

    boolean isPlaying();
    
    void setIsPlaying(boolean isPlaying);

    UserModel getUser();

    void stop() throws IOException;
}
