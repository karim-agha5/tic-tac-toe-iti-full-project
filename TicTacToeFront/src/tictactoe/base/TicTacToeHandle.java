/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictactoe.base;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import javafx.scene.Scene;
import tictactoe.authentication.AuthenticationProvider;
import tictactoe.resources.ResourcesLoader;
import tictactoe.router.Router;

public interface TicTacToeHandle {
    ResourcesLoader resourcesLoader();
    Router router();
    void setupScene(Scene scene);
    SocketHandler socketHandler();
    AuthenticationProvider authenticationProvider();
    Future<?> submitJob(Runnable job);
    <T> Future<T> submitJob(Callable<T> job);
}
