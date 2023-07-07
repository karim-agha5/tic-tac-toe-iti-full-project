/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.game.result;

import TicTacToeCommon.models.UserModel;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import tictactoe.resources.styles.Styles;
import tictactoe.resources.videos.Videos;
import tictactoe.router.RouteViewController;
import tictactoe.utils.UIHelper;

/**
 * FXML Controller class
 *
 * @author ITI
 */
public class GameResultViewController extends RouteViewController {

    @FXML
    private MediaView videoView;
    @FXML
    private Button backButton;
    @FXML
    private Region background;
    @FXML
    private Label playerLabel;

    private final Boolean isWin;

    private final UserModel player;

    public GameResultViewController(UserModel player, boolean isWin) {
        this.isWin = isWin;
        this.player = player;
    }

    public GameResultViewController(boolean isWin) {
        this.isWin = isWin;
        this.player = null;
    }

    public GameResultViewController() {
        this.isWin = null;
        this.player = null;
    }

    @Override
    public URL getViewUri() {
        return getClass().getResource("GameResultView.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        scene().getStylesheets().add(resourcesLoader().getCss(Styles.GAME_RESULT_STYLE_STRING).toString());
        background.setEffect(UIHelper.createBlurEffect());
        final URL file;
        if (isWin != null) {
            String playerName = "You";
            if (player != null) {
                playerName = player.getName();
            }
            playerLabel.setText(playerName + " " + (isWin ? "Won!" : "Lost!"));
            file = resourcesLoader().getVideo(isWin ? Videos.WINNER_STRING : Videos.LOSER_STRING);
        } else {
            playerLabel.setText("Draw!");
            file = resourcesLoader().getVideo(Videos.DRAW_STRING);
        }
        Media media = new Media(file.toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
        videoView.setMediaPlayer(mediaPlayer);
        backButton.setOnAction((e) -> {
            router().pop();
        });
    }

    @Override
    public void onClosed() {
        videoView.getMediaPlayer().dispose();
    }
}
