/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package tictactoe.router;

import javafx.scene.Scene;
import tictactoe.base.*;
import javafx.stage.Stage;

public abstract class RouteViewController<T> extends ViewController implements Route<T> {

    @Override
    public void setup(TicTacToeHandle handle, Stage stage) {
        attach(handle, stage);
    }

    @Override
    public Scene createScene() throws Exception {
        return new Scene(createView());
    }

    @Override
    public void onPop() {
        System.out.println("popped " + getClass().getName());
        close();
    }
 
}
