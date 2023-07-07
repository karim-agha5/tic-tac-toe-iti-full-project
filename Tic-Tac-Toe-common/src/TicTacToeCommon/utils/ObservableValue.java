/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TicTacToeCommon.utils;

import java.util.Observable;

public class ObservableValue<T> {

    protected T value;
    
    protected final Observable observable = new Observable() {
        @Override
        public void notifyObservers(Object data) {
            setChanged();
            super.notifyObservers(data);
        }
    };

    public ObservableValue() {
    }

    public ObservableValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
    
    public ListenCanceller addListener(Observer<T> observer) {
        final java.util.Observer jObserver = (Observable o, Object arg) -> {
            observer.didChange(value);
        };
        observable.addObserver(jObserver);
        return () -> observable.deleteObserver(jObserver);
    }

    @FunctionalInterface
    public static interface Observer<T> {
        void didChange(T newValue);
    }
    
    @FunctionalInterface
    public static interface ListenCanceller {
        void cancel();
    }
}
