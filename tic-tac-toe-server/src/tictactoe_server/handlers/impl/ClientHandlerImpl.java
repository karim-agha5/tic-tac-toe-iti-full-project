/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictactoe_server.handlers.impl;

import TicTacToeCommon.models.UserModel;
import TicTacToeCommon.models.base.RemoteMessage;
import TicTacToeCommon.models.base.RemoteSendable;
import TicTacToeCommon.models.requests.LoginRequest;
import TicTacToeCommon.models.requests.OnlinePlayersRequest;
import TicTacToeCommon.models.requests.SignUpRequest;
import TicTacToeCommon.models.responses.LoginResponse;
import TicTacToeCommon.models.responses.OnlinePlayersResponse;
import TicTacToeCommon.models.responses.SignUpResponse;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import tictactoe_server.data.DatabaseManager;
import tictactoe_server.data.ResultPacket;
import tictactoe_server.data.impl.UserDAOImpl;
import tictactoe_server.handlers.ClientHandler;
import tictactoe_server.managers.ServerSocketManager;
import tictactoe_server.mappers.EntityMapper;
import tictactoe_server.mappers.impl.UserMapper;

public class ClientHandlerImpl implements ClientHandler, Runnable {

    private final ServerSocketManager serverSocketManager;
    private final Socket socket;
    private final ObjectOutputStream objectOutputStream;
    private final ObjectInputStream objectInputStream;
    private final DatabaseManager databaseManager;
    private final UserDAOImpl userDao;
    private final EntityMapper<UserModel> entityMapper;
    private ResultPacket resultPacket;
    private UserModel userModel;
    private boolean isPlaying;

    public ClientHandlerImpl(ServerSocketManager serverSocketManager, Socket socket)
            throws IOException, SQLException {
        this.serverSocketManager = serverSocketManager;
        this.socket = socket;
        this.databaseManager = serverSocketManager.getDatabaseManager();
        this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        this.objectInputStream = new ObjectInputStream(socket.getInputStream());
        this.userDao = new UserDAOImpl(databaseManager);
        this.entityMapper = new UserMapper();
        // Submit yourself in the thread pool
        serverSocketManager.submitJob(this);
    }

    public void handleMessage(RemoteMessage remoteMessage) {
        try {
            if (remoteMessage.getMessage(LoginRequest.class) != null) {
                handleLoginMessage((LoginRequest) remoteMessage.getMessage());
            } else if (remoteMessage.getMessage(SignUpRequest.class) != null) {
                handleSignUpRequest((SignUpRequest) remoteMessage.getMessage());
            } else if (remoteMessage.getMessage(OnlinePlayersRequest.class) != null) {
                handleOnlinePlayersRequest();
            } else if (userModel != null) {
                serverSocketManager.getGamesManager().process(userModel.getId(), remoteMessage.getMessage());
            }
        } catch (Exception e) {
            Logger.getLogger(ClientHandlerImpl.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void handleLoginMessage(LoginRequest loginRequest) {
        try {
            // In the case of multiple threads accessing this method
            ResultPacket resultPacket = userDao.
                    findByUsernameAndPassword(loginRequest.getUsername(), loginRequest.getPassword());
            ResultSet resultSet = resultPacket.getResultSet();
            // Check if the user account is in the database or not.
            // If the result set is empty (The client hasn't signed up before)
            if (!resultSet.next()) {
                send(new LoginResponse(false));
                resultPacket.close();
            } else { // already signed up before
                try {
                    this.userModel = entityMapper.mapToEntity(resultSet);
                } catch (SQLException e) {
                    resultPacket.close();
                    throw e;
                }
                this.resultPacket = resultPacket;
                send(new LoginResponse(true, userModel));
                serverSocketManager.getClientsManager().authenticateHandler(false, resultSet.getString(1), this);
            }
        } catch (SQLException e) {
            send(new LoginResponse(false));
            Logger.getLogger(ClientHandlerImpl.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void handleSignUpRequest(SignUpRequest signUpRequest) {
        /*
            Check if the user has signed up before by getting a ResultSet.
         */
        try {
            if (signUpRequest.getPassword() == null || signUpRequest.getPassword().length() < 8) {
                throw new SQLException("Wrong password length");
            } 
            UserModel userModel = new UserModel(UUID.randomUUID().toString(), signUpRequest.getUserName(), null, new Date().getTime());
            userDao.createUser(userModel, signUpRequest.getPassword());
            ResultPacket resultPacket = userDao.findById(userModel.getId());
            try {
                if (resultPacket.getResultSet().next()) {
                    this.userModel = entityMapper.mapToEntity(resultPacket.getResultSet());
                } else {
                    throw new SQLException();
                }
            } catch (SQLException e) {
                resultPacket.close();
                throw e;
            }
            this.resultPacket = resultPacket;
            serverSocketManager.getClientsManager().authenticateHandler(true, userModel.getId(), this);
            send(new SignUpResponse(true, this.userModel));
        } catch (SQLException ex) { // Unable to access the database to retrieve the user's data
            send(new SignUpResponse(false));
            Logger.getLogger(ClientHandlerImpl.class.getName()).log(Level.SEVERE, null, ex);
            // once a SignUpResponse with a failure status is sent. Terminate the method.
        }
    }

    private void handleOnlinePlayersRequest() {
        String userId = userModel == null ? null : userModel.getId();
        if (userId != null) {
            ArrayList<UserModel> players = serverSocketManager.getClientsManager().getAvailablePlayers()
                    .filter((e) -> !e.getId().equals(userId))
                    .collect(Collectors.toCollection(() -> new ArrayList<>()));
            send(new OnlinePlayersResponse(true, players)); // If unable to send an OnlinePlayersResponse.
        } else {
            send(new OnlinePlayersResponse(false));
        }
    }

    /**
     * Constantly listen for messages from the client on a thread
     */
    @Override
    public void run() {

        try {
            while (true) {
                RemoteMessage message = RemoteMessage.readFrom(objectInputStream);
                serverSocketManager.submitJob(() -> {
                    Logger.getLogger(ClientHandlerImpl.class.getName()).info("Got message " + message.getMessage());
                    handleMessage(message);
                });
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ClientHandlerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (userModel != null) {
                serverSocketManager.getClientsManager().unathenticateHandler(userModel.getId());
            }
            serverSocketManager.getClientsManager().removeHandler(this);
            if (resultPacket != null) {
                try {
                    resultPacket.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ClientHandlerImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    Logger.getLogger(ClientHandlerImpl.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
    }

    /**
     * Sends Serializable messages to the client
     */
    @Override
    public void send(RemoteSendable data) {
        serverSocketManager.submitJob(() -> {
            try {
                Logger.getLogger(ClientHandlerImpl.class.getName()).info("Sending message " + data);
                new RemoteMessage(data).writeInto(objectOutputStream);
                Logger.getLogger(ClientHandlerImpl.class.getName()).info("Message sent " + data);
            } catch (IOException ex) {
                Logger.getLogger(ClientHandlerImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

    }

    @Override
    public boolean isAuthenticated() {
        return userModel != null;
    }

    @Override
    public boolean isPlaying() {
        return isPlaying;
    }

    @Override
    public UserModel getUser() {
        return this.userModel;
    }

    @Override
    public void stop() throws IOException {
        socket.close();
    }

    @Override
    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

}
