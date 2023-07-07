/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictactoe.router;

import javafx.scene.Scene;
import javafx.stage.Stage;
import tictactoe.base.TicTacToeHandle;

public interface Route<T> {

    void setup(TicTacToeHandle handle, Stage stage);

    Scene createScene() throws Exception;

    default void onPop() {
        System.out.println("popped " + getClass().getName());
    }

    default void onPush() {
        System.out.println("pushed " + getClass().getName());
    }

    default void onLeave() {
        System.out.println("left " + getClass().getName());
    }

    default void onReturn() {
        System.out.println("returned " + getClass().getName());
    }
}
