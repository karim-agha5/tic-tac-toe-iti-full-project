/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictactoe.router;

import java.util.concurrent.Future;

public interface Router {
    <T>Future <T>push(Route<T> route);

    boolean pop();

    <T> boolean pop(T result);
    
    <T> Future<T> replace(Route<T> route);

    <T, R> Future<T> replace(Route<T> route, R result);
}
