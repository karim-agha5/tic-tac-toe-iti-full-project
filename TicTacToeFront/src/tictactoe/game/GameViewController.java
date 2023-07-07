/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package tictactoe.game;

import TicTacToeCommon.models.MoveModel;
import TicTacToeCommon.models.events.GameEvent;
import TicTacToeCommon.services.engine.piece.League;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import tictactoe.game.providers.GameProvider;
import tictactoe.game.result.GameResultViewController;
import tictactoe.resources.styles.Styles;
import tictactoe.router.RouteViewController;
import tictactoe.utils.ObjectUtils;
import tictactoe.utils.UIHelper;

public class GameViewController extends RouteViewController {

    @FXML
    private Region background;
    @FXML
    private Label gameTitle;
    @FXML
    private GridPane gameGrid;
    @FXML
    private Button backButton;
    @FXML
    private Label player1Username;
    @FXML
    private ImageView player1Icon;
    @FXML
    private Label player2Username;
    @FXML
    private ImageView player2Icon;
    @FXML
    private Label timer;
    @FXML
    private ImageView timerIcon;
    @FXML
    private Button recordButton;
    @FXML
    private Rectangle finishLineLeft;
    @FXML
    private Rectangle finishLineMiddleV;
    @FXML
    private Rectangle finishLineRight;
    @FXML
    private Rectangle finishLineTop;
    @FXML
    private Rectangle finishLineMiddleH;
    @FXML
    private Rectangle finishLineBottom;
    @FXML
    private Rectangle finishLineTopLeft;
    @FXML
    private Rectangle finishLineTopRight;

    private Future<?> transition;

    private final List<Pane> squares = new LinkedList<>();

    private final GameProvider gameProvider;

    public GameViewController(GameProvider gameProvider) {
        this.gameProvider = gameProvider;
    }

    @Override
    public URL getViewUri() {
        return getClass().getResource("GameView.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        scene().getStylesheets().add(resourcesLoader().getCss(Styles.GAME_STYLE_STRING).toString());
        background.setEffect(UIHelper.createBlurEffect());
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                ImageView image = new ImageView();
                Pane node = new StackPane(image);
                StackPane.setAlignment(image, Pos.CENTER);
                gameGrid.add(node, x, y);
                node.setOnMouseClicked(this::handleOnGridClicked);
                squares.add(node);
            }
        }

        gameGrid.setOnMouseClicked((e) -> {
            gameProvider.onBoardClicked();
        });

        backButton.setOnAction((e) -> gameProvider.withdraw());
//        backButton.setOnAction(router()::pop);

        player1Username.setText(gameProvider.getPlayer1().getName());
        player2Username.setText(gameProvider.getPlayer2().getName());

        player1Icon.getStyleClass().setAll(getCssClassFromLeague(gameProvider.getPlayer1League()));
        player2Icon.getStyleClass().setAll(getCssClassFromLeague(gameProvider.getPlayer2League()));

        for (Rectangle finishLine : getFinishLines()) {
            finishLine.setVisible(false);
        }

        gameProvider.getEvents().addListener((event) -> {
            Platform.runLater(() -> {
                System.out.println(event);
                if (event instanceof GameEvent.Moved) {
                    handleMove(((GameEvent.Moved) event).getMove());
                } else if (event instanceof GameEvent.Ended) {
                    router().pop(false);
                } else if (event instanceof GameEvent.Draw) {
                    finishSequence(new GameResultViewController());
                } else if (event instanceof GameEvent.Withdraw) {
                    router().replace(new GameResultViewController(gameProvider.getWinner(), true));
                } else if (event instanceof GameEvent.Won) {
                    finishSequence(new GameResultViewController(gameProvider.getWinner(), true));
                } else if (event instanceof GameEvent.Lost) {
                    finishSequence(new GameResultViewController(false));
                }
            });
        });

        gameProvider.getLastMoveResult().addListener((isValid) -> {
            Platform.runLater(() -> {
                if (isValid == false) {
                    uIAlert().showErrorDialog("Invalid move", "Invalid move made");
                }
            });
        });

        gameProvider.getCurrentPlayer().addListener((newValue) -> {
            Platform.runLater(() -> {
                if (Objects.equals(newValue, GameProvider.FIRST_PLAYER)) {
                    timer.setText(gameProvider.getPlayer1().getName());
                    timerIcon.getStyleClass().setAll(getCssClassFromLeague(gameProvider.getPlayer1League()));
                } else {
                    timer.setText(gameProvider.getPlayer2().getName());
                    timerIcon.getStyleClass().setAll(getCssClassFromLeague(gameProvider.getPlayer2League()));
                }
            });
        });

        gameProvider.getIsRecording().addListener((newValue) -> {
            if (newValue) {
                recordButton.setText("Recording");
            } else {
                recordButton.setText("Not recording");
            }
        });

        recordButton.setOnAction((event) -> {
            gameProvider.setIsRecording(!gameProvider.getIsRecording().getValue());
        });

        recordButton.setVisible(gameProvider.canRecord());
        
        gameProvider.start();
    }

    private void finishSequence(RouteViewController route) {
        showFinishLine();
        backButton.setOnAction(null);
        transition = handle().submitJob(() -> {
            try {
                Thread.sleep(3000);
                Platform.runLater(() -> {
                    router().replace(route);
                });
            } catch (InterruptedException ex) {
                Logger.getLogger(GameViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    @Override
    public void onClosed() {
        gameProvider.close();
        if (transition != null) {
            transition.cancel(true);
        }
    }

    private Rectangle[] getFinishLines() {
        return new Rectangle[]{
            finishLineLeft, finishLineMiddleV, finishLineRight,
            finishLineTop, finishLineMiddleH, finishLineBottom,
            finishLineTopLeft, finishLineTopRight
        };
    }

    private void handleOnGridClicked(MouseEvent event) {
        if (gameProvider.getCanInput().getValue()) {
            Pane source = (Pane) event.getSource();
            int colIndex = ObjectUtils.getOrElse(GridPane.getColumnIndex(source), 0);
            int colRow = ObjectUtils.getOrElse(GridPane.getRowIndex(source), 0);
            int index = colRow * 3 + colIndex;
            gameProvider.makeMove(index);
            logger.log(Level.INFO, "Pressed at {0} {1}", new Object[]{colIndex, colRow});
        }
    }

    private void handleMove(MoveModel move) {
        Pane pane = squares.get(move.getSpacePosition());
        pane.setOnMouseClicked(null);
        League league;
        if (move.getPlayerId().equals(gameProvider.getPlayer1().getId())) {
            league = gameProvider.getPlayer1League();
        } else {
            league = gameProvider.getPlayer2League();
        }
        pane.setUserData(move.getPlayerId());
        pane.getChildren().get(0).getStyleClass().add(getCssClassFromLeague(league));
    }

    private void showFinishLine() {
        Rectangle line = getRectangle();
        if (line != null) {
            line.setVisible(true);
        }
    }

    private Rectangle getRectangle() {
        if (areTrue(squares.get(0), squares.get(1), squares.get(2))) {
            return finishLineTop;
        }
        if (areTrue(squares.get(3), squares.get(4), squares.get(5))) {
            return finishLineMiddleH;
        }
        if (areTrue(squares.get(6), squares.get(7), squares.get(8))) {
            return finishLineBottom;
        }

        if (areTrue(squares.get(0), squares.get(3), squares.get(6))) {
            return finishLineLeft;
        }
        if (areTrue(squares.get(1), squares.get(4), squares.get(7))) {
            return finishLineMiddleV;
        }
        if (areTrue(squares.get(2), squares.get(5), squares.get(8))) {
            return finishLineRight;
        }

        if (areTrue(squares.get(0), squares.get(4), squares.get(8))) {
            return finishLineTopLeft;
        }

        if (areTrue(squares.get(2), squares.get(4), squares.get(6))) {
            return finishLineTopRight;
        }
        return null;
    }

    private boolean areTrue(Pane... squares) {
        Object data = null;
        for (Pane pane : squares) {
            Object d = pane.getUserData();
            if (d == null || (data != null && !Objects.equals(d, data))) {
                return false;
            }
            data = d;
        }
        return true;
    }

    private String getCssClassFromLeague(League league) {
        if (league.equals(league.Cross)) {
            return "XImage";
        } else {
            return "OImage";
        }
    }
}
