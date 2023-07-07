/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML.java to edit this template
 */
package tictactoe;

import TicTacToeCommon.models.GameOfferAnswer;
import TicTacToeCommon.models.UserModel;
import TicTacToeCommon.models.events.GameEvent;
import TicTacToeCommon.models.requests.JoinGameRequest;
import TicTacToeCommon.models.responses.JoinGameResponse;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tictactoe.authentication.AuthenticationProvider;
import tictactoe.base.SocketHandler;
import tictactoe.base.TicTacToeHandle;
import tictactoe.game.GameViewController;
import tictactoe.game.providers.OnlineGameHandler;
import tictactoe.landing.LandingViewController;
import tictactoe.resources.ResourcesLoader;
import tictactoe.resources.styles.Styles;
import tictactoe.router.StackRouter;
import tictactoe.router.Router;
import tictactoe.utils.UIAlert;

public class TicTacToe extends Application implements TicTacToeHandle {

    private Router router;
    private final SocketHandler socketHandler;
    private final AuthenticationProvider authenticationProvider;
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final ResourcesLoader resourcesLoader = new ResourcesLoader() {
    };

    public TicTacToe() {
        socketHandler = new SocketHandler(this);
        authenticationProvider = new AuthenticationProvider(socketHandler);
    }

    @Override
    public Router router() {
        return router;
    }

    @Override
    public ResourcesLoader resourcesLoader() {
        return resourcesLoader;
    }

    @Override
    public SocketHandler socketHandler() {
        return socketHandler;
    }

    @Override
    public AuthenticationProvider authenticationProvider() {
        return authenticationProvider;
    }

    @Override
    public void setupScene(Scene scene) {
        scene.getStylesheets().add(resourcesLoader.getCss(Styles.BASE_STYLE_STRING).toString());
        scene.getStylesheets().add(resourcesLoader.getCss(Styles.TABVIEW_STYLE_STRING).toString());
    }

    @Override
    public Future<?> submitJob(Runnable job) {
        return executorService.submit(job);
    }

    @Override
    public <T> Future<T> submitJob(Callable<T> job) {
        return executorService.submit(job);
    }

    @Override
    public void start(Stage stage) throws Exception {
        setupStage(stage);
        router = new StackRouter(this, stage);
        router.push(new LandingViewController());
        stage.show();
        final UIAlert alert = new UIAlert(stage);
        socketHandler.getMessage().addListener((newValue) -> {
            Platform.runLater(() -> {
                if (newValue instanceof JoinGameRequest) {
                    UserModel player = ((JoinGameRequest) newValue).getPlayer();
                    Boolean result = alert.showPromptDialog("New Game Request", "Player " + player.getName() + " wants to play with you. Would you like to start game with him?");
                    System.out.println(result);
                    try {
                        socketHandler.send(new JoinGameResponse(true, new GameOfferAnswer(result == true, player.getId())));
                    } catch (SocketHandler.NotConnectedException ex) {
                        alert.showErrorDialog("Error", "Failed to accept the request");
                        Logger.getLogger(TicTacToe.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (newValue instanceof GameEvent.Started) {
                    GameEvent.Started event = (GameEvent.Started) newValue;
                    router.push(new GameViewController(new OnlineGameHandler(event, socketHandler, authenticationProvider)));
                }
            });
        });
        socketHandler.getConnected().addListener((connected) -> {
            if (connected == false) {
                Platform.runLater(() -> {
                    alert.showErrorDialog("Error", "Server is down");
                });
            }
        });
    }

    @Override
    public void stop() throws Exception {
        socketHandler.stop();
        executorService.shutdown();
    }

    private void setupStage(Stage stage) {
        stage.setMinHeight(720);
        stage.setMinWidth(1024);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
