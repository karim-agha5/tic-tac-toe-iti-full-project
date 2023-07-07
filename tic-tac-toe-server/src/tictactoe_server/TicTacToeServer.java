/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML.java to edit this template
 */
package tictactoe_server;

import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tictactoe_server.data.impl.DerbyDatabaseManager;
import tictactoe_server.managers.ServerSocketManager;
import tictactoe_server.managers.impl.ServerSocketManagerImpl;

/**
 *
 * @author m-essam
 */
public class TicTacToeServer extends Application {

    private final ServerSocketManager socketManager;

    public TicTacToeServer() throws SQLException {
        DerbyDatabaseManager.getInstance().start();
        this.socketManager = new ServerSocketManagerImpl();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
        loader.setController(new FXMLDocumentController(socketManager));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        DerbyDatabaseManager.getInstance().start();
        socketManager.stop();
        super.stop();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
