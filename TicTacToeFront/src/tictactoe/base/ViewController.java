/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package tictactoe.base;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tictactoe.resources.ResourcesLoader;
import tictactoe.router.Router;
import tictactoe.utils.UIAlert;

public abstract class ViewController implements AutoCloseable {

    protected final List<ViewController> controllers = new LinkedList<>();
    protected static final Logger logger = Logger.getLogger(ViewController.class.getName());
    private UIAlert uiaAlert;
    private TicTacToeHandle handle;
    private Parent view;
    private Stage stage;
    private URL location;
    private ResourceBundle resources;

    public ViewController() {
    }

    public abstract URL getViewUri();

    public abstract void initialize(URL location, ResourceBundle resources);

    protected void attach(TicTacToeHandle handle, Stage stage) {
        this.handle = handle;
        this.stage = stage;
        init();
        initialize(location, resources);
    }
    
    private void init() {
        uiaAlert = new UIAlert(stage);
    }

    protected Node attachController(ViewController controller) {
        try {
            Parent parent = controller.createView();
            controller.attach(handle, stage);
            controllers.add(controller);
            return parent;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Node attachTo(ViewController controller) {
        return controller.attachController(this);
    }

    protected final TicTacToeHandle handle() {
        return handle;
    }

    protected final Stage stage() {
        return stage;
    }

    protected final UIAlert uIAlert() {
        return uiaAlert;
    }
    
    protected final Router router() {
        return handle.router();
    }

    protected final ResourcesLoader resourcesLoader() {
        return handle.resourcesLoader();
    }

    protected final Scene scene() {
        return stage.getScene();
    }

    protected final <T extends Parent> Parent view() {
        return (T) view;
    }
    
    protected final <T extends Parent> Parent root() {
        return (T) stage.getScene().getRoot();
    }

    public void onClosed() {
    }

    @Override
    public void close() {
        onClosed();
        controllers.forEach(ViewController::onClosed);
    }

    public final Parent createView() throws IOException {
        return createView(this);
    }

    public static Parent createView(ViewController controller) throws IOException {
        FXMLLoader loader = new FXMLLoader(controller.getViewUri());
        loader.setController(controller);
        Parent root = loader.load();
        controller.view = root;
        root.setUserData(controller);
        return root;
    }
}
