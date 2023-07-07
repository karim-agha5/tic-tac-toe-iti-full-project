/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TicTacToeCommon.utils;

public class MutableObservableValue<T> extends ObservableValue<T> {
    
    public MutableObservableValue() {
    }

    public MutableObservableValue(T value) {
        this.value = value;
    }

    public void setValue(T value) {
        this.value = value;
        observable.notifyObservers(value);
    }
}
