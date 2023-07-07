/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictactoe.base;

import TicTacToeCommon.models.base.RemoteMessage;
import TicTacToeCommon.models.base.RemoteSendable;
import TicTacToeCommon.utils.MutableObservableValue;
import TicTacToeCommon.utils.ObservableValue;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author m-essam
 */
public class SocketHandler {

    private final MutableObservableValue<RemoteSendable> message = new MutableObservableValue<>();
    private final MutableObservableValue<Boolean> isConnected = new MutableObservableValue<>(false);
    private final TicTacToeHandle handle;
    private Socket socket;
    private ObjectOutputStream out;

    public SocketHandler(TicTacToeHandle handle) {
        this.handle = handle;
    }

    public void start() {
        if (socket == null) {
            handle.submitJob(() -> {
                try {
                    socket = new Socket(InetAddress.getLocalHost(), 1000);
                    out = new ObjectOutputStream(socket.getOutputStream());
                } catch (IOException e) {
                    Logger.getLogger(SocketHandler.class.getName()).log(Level.SEVERE, null, e);
                    stop();
                    isConnected.setValue(false);
                    return;
                }
                isConnected.setValue(true);
                try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
                    while (true) {
                        RemoteMessage receivedMessage = RemoteMessage.readFrom(in);
                        Logger.getLogger(getClass().getName()).info("Message received " + receivedMessage.getMessage());
                        try {
                            message.setValue(receivedMessage.getMessage());
                        } catch (Exception e) {
                            Logger.getLogger(SocketHandler.class.getName()).log(Level.SEVERE, null, e);
                        }
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    Logger.getLogger(SocketHandler.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    stop();
                    isConnected.setValue(false);
                }
            });
        }
    }

    public void stop() {
        if (socket != null) {
            handle.submitJob(() -> {
                socket.close();
                socket = null;
                return 0;
            });
        }
    }

    public void send(RemoteSendable message) throws NotConnectedException {
       if (out == null) {
           throw new NotConnectedException();
       }
        handle.submitJob(() -> {
            Logger.getLogger(getClass().getName()).info("Sending message " + message);
            new RemoteMessage(message).writeInto(out);
            Logger.getLogger(getClass().getName()).info("Message sent " + message);
            return 0;
        });
    }

    public ObservableValue<Boolean> getConnected() {
        return isConnected;
    }

    public ObservableValue<RemoteSendable> getMessage() {
        return message;
    }
    
    
    public static class NotConnectedException extends Exception {}
}
