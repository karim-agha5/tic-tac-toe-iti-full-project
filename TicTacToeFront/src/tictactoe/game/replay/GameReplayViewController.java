package tictactoe.game.replay;

import TicTacToeCommon.models.MoveModel;
import TicTacToeCommon.models.PlayerModel;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import tictactoe.resources.styles.Styles;
import tictactoe.router.RouteViewController;
import tictactoe.utils.GameRecordService;
import tictactoe.utils.UIHelper;

public class GameReplayViewController extends RouteViewController {

    @FXML
    private Region background;

    @FXML
    private Label gameTitle;

    @FXML
    private GridPane gameGrid;

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

    @FXML
    private Label player1Username;

    @FXML
    private Button backButton;

    @FXML
    private Label player2Username;

    private String gameFileId;

    private GameRecordService.Record moves;

    private PlayerModel player1Model;

    private PlayerModel player2Model;

    public GameReplayViewController() {
    }

    public GameReplayViewController(String gameFileId, PlayerModel player1Model, PlayerModel player2Model)
            throws IOException, ClassNotFoundException {
        this.gameFileId = gameFileId;
        this.moves = new GameRecordService().readGame(gameFileId);
        this.player1Model = player1Model;
        this.player2Model = player2Model;
    }

    @Override
    public URL getViewUri() {
        return getClass().getResource("GameReplayView.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        scene().getStylesheets().add(resourcesLoader().getCss(Styles.GAME_STYLE_STRING).toString());
        background.setEffect(UIHelper.createBlurEffect());
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                ImageView image = new ImageView();
                Node node = new StackPane(image);
                StackPane.setAlignment(image, Pos.CENTER);
                gameGrid.add(node, x, y);
            }
        }
        backButton.setOnAction(router()::pop);
        for (Rectangle finishLine : getFinishLines()) {
            finishLine.setVisible(false);
        }

        player1Username.setText(player1Model.getName());
        player2Username.setText(player2Model.getName());

        runTheRecord(moves.getMoves());

    }

    private Rectangle[] getFinishLines() {
        return new Rectangle[]{
            finishLineLeft, finishLineMiddleV, finishLineRight,
            finishLineTop, finishLineMiddleH, finishLineBottom,
            finishLineTopLeft, finishLineTopRight
        };
    }

    private void runTheRecord(List<MoveModel> list) {

        ObservableList<Node> gridChildren = gameGrid.getChildren();

        Thread thread = new Thread(() -> {

            MoveModel moveModel;
            StackPane stackPane;
            int rowIndex;
            int columnIndex;

            for (int i = 0; i < list.size(); i++) {

                moveModel = list.get(i);
                rowIndex = moveModel.getSpacePosition() / 3;
                columnIndex = moveModel.getSpacePosition() % 3;

                for (int j = 0; j < gridChildren.size(); j++) {

                    stackPane = (StackPane) gridChildren.get(j);

                    if (GridPane.getRowIndex(stackPane) == rowIndex
                            && GridPane.getColumnIndex(stackPane) == columnIndex) {

                        if (moveModel.getPlayerId().equals("1")) {
                            stackPane.getChildren().get(0).getStyleClass().add("XImage");
                        } else {
                            stackPane.getChildren().get(0).getStyleClass().add("OImage");
                        }
                    }

                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {

                    Logger.getLogger(GameReplayViewController.class.getName()).log(Level.SEVERE, null, ex);
                    break;
                }

            }

        });

        thread.start();
    }

}
