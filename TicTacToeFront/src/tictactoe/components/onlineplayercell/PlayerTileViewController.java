/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package tictactoe.components.onlineplayercell;

import TicTacToeCommon.models.UserModel;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import tictactoe.base.ViewController;
import tictactoe.resources.images.Images;

public class PlayerTileViewController extends ViewController {

    private static final String CHECKED_IMAGE = "CheckedImage";
    @FXML
    private ImageView userImage;
    @FXML
    private ImageView checkedImage;
    @FXML
    private Label username;

    private UserModel player;

    @Override
    public URL getViewUri() {
        return getClass().getResource("PlayerTileView.fxml");
    }

    public void setPlayer(UserModel player) {
        this.player = player;
        setPlayerUI(player);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userImage.setImage(new Image(resourcesLoader().getImageAsStream(Images.USER_IC_STRING)));
        setPlayerUI(player);
    }

    private void setPlayerUI(UserModel player) {
        if (player != null) {
            username.setText(player.getName());
            view().setVisible(true);
        } else {
            view().setVisible(false);
        }
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
}
