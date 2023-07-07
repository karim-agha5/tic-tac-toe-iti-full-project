/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictactoe.router;

import java.io.IOException;
import java.util.Stack;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tictactoe.base.TicTacToeHandle;

public class StackRouter implements Router {
    private static final Logger logger = Logger.getLogger(StackRoute.class.getName());
    private final Stack<StackRoute> routes = new Stack<>();
    private final TicTacToeHandle handle;
    private final Stage stage;
    private StackRoute top;

    public StackRouter(TicTacToeHandle handle, Stage stage) {
        this.handle = handle;
        this.stage = stage;
    }

 
    
    @Override
    public <T> Future<T> push(Route<T> route) {
        try {
            Scene scene = route.createScene();
            handle.setupScene(scene);
            stage.setScene(scene);
            route.setup(handle, stage);
            StackRoute<T> stackRoute = new StackRoute<>(route, scene);
            if (top != null) {
                top.route.onLeave();
            }
            routes.push(stackRoute);
            top = stackRoute;
            stackRoute.route.onPush();
            return stackRoute.resultCompleter;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }  
    }

    @Override
    public boolean pop() {
        return pop(null);
    }

    @Override
    public <T> boolean pop(T result) {
        if (!routes.isEmpty()) {
            StackRoute route = routes.pop();
            route.route.onPop();
            route.complete(result);
            top = null;
            if (!routes.isEmpty()) {
                top = routes.lastElement();
                stage.setScene(top.scene);
                top.route.onReturn();
            } else {
                stage.close();
            }
            return true;
        }
        return false;
    }

    @Override
    public <T> Future<T> replace(Route<T> route) {
        return replace(route, null);
    }

    @Override
    public <T, R> Future<T> replace(Route<T> route, R result) {
        if (!routes.isEmpty()) {
            StackRoute currentRoute = routes.pop();
            currentRoute.route.onPop();
            currentRoute.complete(result);
            top = null;
        }
        return push(route);
    }

    static private class StackRoute<T> {

        private final Route<T> route;
        private final Scene scene;
        private final CompletableFuture<T> resultCompleter;
        private T result = null;

        private StackRoute(Route<T> route, Scene scene) {
            this.route = route;
            this.scene = scene;
            this.resultCompleter = new CompletableFuture<>();
        }

        private boolean isCompleted() {
            return resultCompleter.isDone();
        }

        private boolean complete(T popResult) {
            if (popResult != null) {
                result = popResult;
            }
            return resultCompleter.complete(result);
        }
    }
}
