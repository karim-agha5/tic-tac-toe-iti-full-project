package tictactoe_server.managers.impl;

import TicTacToeCommon.utils.MutableObservableValue;
import TicTacToeCommon.utils.ObservableValue;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import tictactoe_server.managers.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import tictactoe_server.data.DatabaseManager;
import tictactoe_server.data.impl.DerbyDatabaseManager;

public class ServerSocketManagerImpl implements ServerSocketManager {

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final GamesManager gamesManager;
    private final ClientsManager clientsManager;
    private final DatabaseManager databaseManager;
    private ServerSocket server;
    private final MutableObservableValue<Boolean> serverStatus = new MutableObservableValue<>(false);

    public ServerSocketManagerImpl() throws SQLException {

        this.gamesManager = new GamesManagerImpl(this);
        this.databaseManager = DerbyDatabaseManager.getInstance();
        this.clientsManager = new ClientsManagerImpl(this, databaseManager);

    }

    @Override
    public ClientsManager getClientsManager() {
        return clientsManager;
    }

    @Override
    public GamesManager getGamesManager() {
        return gamesManager;
    }

    @Override
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    @Override
    public <T> Future<T> submitJob(Callable<T> job) {
        return executorService.submit(job);
    }

    @Override
    public Future<?> submitJob(Runnable job) {
        return executorService.submit(job);
    }

    @Override
    public Future<?> start() {
        return submitJob(() -> {
            int port = 1000;
            try {
                server = new ServerSocket(port);
                databaseManager.start();
            } catch (IOException e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                return 0;
            }
            serverStatus.setValue(true);
            try {
                while (true) {
                    Socket socket = server.accept();
                    clientsManager.accept(socket);
                }
            } finally {
                serverStatus.setValue(false);
                server.close();
                server = null;
            }
        });
    }

    @Override
    public Future<?> stop() {
        return submitJob(() -> {
            server.close();
            gamesManager.stop();
            clientsManager.stop();
            databaseManager.stop();
            return 0;
        });
    }

    @Override
    public ObservableValue<Long> getActiveUsers() {
        return clientsManager.getActiveUsers();
    }

    @Override
    public ObservableValue<Long> getAllUsers() {
        return clientsManager.getAllUsers();
    }

    @Override
    public ObservableValue<Boolean> getServerStatus() {
        return this.serverStatus;
    }
}
