package tictactoe_server.managers;

import TicTacToeCommon.utils.ObservableValue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import tictactoe_server.data.DatabaseManager;

public interface ServerSocketManager {

    ObservableValue<Long> getActiveUsers();
    
    ObservableValue<Long> getAllUsers();
    
    ObservableValue<Boolean> getServerStatus();
    
    ClientsManager getClientsManager();

    GamesManager getGamesManager();
    
    DatabaseManager getDatabaseManager();

    <T> Future<T> submitJob(Callable<T> job);

    Future<?> submitJob(Runnable job);

    Future<?> start();

    Future<?> stop();

}
