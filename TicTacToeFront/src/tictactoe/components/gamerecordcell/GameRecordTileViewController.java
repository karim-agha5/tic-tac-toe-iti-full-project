/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package tictactoe.components.gamerecordcell;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.Future;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import tictactoe.base.ViewController;
import tictactoe.utils.GameRecordService;

public class GameRecordTileViewController extends ViewController {

    private static final String CHECKED_IMAGE = "CheckedImage";

    @FXML
    private Label gameLabel;
    @FXML
    private Label dateTimeLabel;
    @FXML
    private ImageView checkedImage;

    private GameRecordService.Record record;

    private String gameRecord;

    private Future<?> loadingOp;

    private GameRecordService gameRecordService;

    private DateFormat formatter;

    public GameRecordTileViewController() {
        this.gameRecordService = new GameRecordService();
        this.formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    }

    @Override
    public URL getViewUri() {
        return getClass().getResource("GameRecordTileView.fxml");
    }

    public void setGameRecord(String gameRecord) {
        if (!Objects.equals(this.gameRecord, gameRecord)) {
            if (loadingOp != null) {
                loadingOp.cancel(true);
            }
            this.gameRecord = gameRecord;
            if (gameRecord == null) {
                setGameRecordUI(null);
            } else {
                loadingOp = handle().submitJob(() -> {
                    try {
                        GameRecordService.Record record = gameRecordService.readGame(gameRecord);
                        if (!Thread.currentThread().isInterrupted()) {
                            this.record = record;
                            Platform.runLater(() -> {
                                setGameRecordUI(record);
                            });
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        Platform.runLater(() -> {
                            setErrorUI();
                        });

                    }
                });
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setGameRecordUI(record);
    }

    private void setGameRecordUI(GameRecordService.Record gameRecord) {
        if (gameRecord != null) {
            gameLabel.setText(record.getPlayer1().getName() + " vs. " + record.getPlayer2().getName());
            dateTimeLabel.setText(formatter.format(record.getCreatedAt()));
            view().setVisible(true);
        } else {
            view().setVisible(false);
        }
    }

    private void setErrorUI() {
        gameLabel.setText("Error");
        dateTimeLabel.setText("");
        view().setVisible(true);
    }

    public void setSelected(boolean selected) {
        List<String> cssClasses = checkedImage.getStyleClass();
        if (selected) {
            if (!cssClasses.contains(CHECKED_IMAGE)) {
                cssClasses.add(CHECKED_IMAGE);
            }
        } else {
            cssClasses.remove(CHECKED_IMAGE);
        }
    }

    @Override
    public void onClosed() {
        if (loadingOp != null) {
            loadingOp.cancel(true);
        }
    }

}
