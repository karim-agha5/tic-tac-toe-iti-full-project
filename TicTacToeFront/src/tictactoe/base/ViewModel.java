/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictactoe.base;

import TicTacToeCommon.utils.MutableObservableValue;
import TicTacToeCommon.utils.ObservableValue;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;
import tictactoe.utils.ObjectUtils;

public class ViewModel<T> {

    private final MutableObservableValue<T> observableValue = new MutableObservableValue<>();
    private final Map<ViewModelListener<T>, ObservableValue.ListenCanceller> listeners = new HashMap<>();

    protected void updateState(T newState) {
        Platform.runLater(() -> {
            observableValue.setValue(newState);
        });
    }

    public ObservableValue<T> getObservableValue() {
        return observableValue;
    }

    public void start() {}
    
    public void bind(ViewModelListener<T> listener) {
        boolean doStart = listeners.isEmpty();
        listeners.put(listener, observableValue.addListener(((newValue) -> {
            listener.didUpdateState(newValue);
        })));
        if (doStart) start();
    }

    public void unbind(ViewModelListener<T> listener) {
        ObjectUtils.ifNotNull(listeners.remove(listener), (e) -> e.cancel());
        if (listeners.isEmpty()) {
            stop();
        }
    }
    
    public void stop() {}
}
