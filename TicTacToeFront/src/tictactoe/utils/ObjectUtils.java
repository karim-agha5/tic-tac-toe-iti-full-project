/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictactoe.utils;

import java.util.Objects;
import java.util.function.Consumer;

public abstract class ObjectUtils {
    public static <T> T getOrElse(T obj, T elseObj) {
        if (Objects.isNull(obj)) return elseObj;
        return obj;
    }
    
    public static <T> void ifNotNull(T obj, Consumer<T> op) {
        if (!Objects.isNull(obj)) op.accept(obj);
    }
}
