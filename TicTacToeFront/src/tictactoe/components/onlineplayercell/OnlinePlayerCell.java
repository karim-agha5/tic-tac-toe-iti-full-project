/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictactoe.components.onlineplayercell;

import TicTacToeCommon.models.UserModel;
import javafx.scene.control.ListCell;
import tictactoe.base.ViewController;

public class OnlinePlayerCell extends ListCell<UserModel> {

    private final PlayerTileViewController playerTileViewController;
    
    public OnlinePlayerCell(ViewController parentController) {
        playerTileViewController = new PlayerTileViewController();
        setGraphic(playerTileViewController.attachTo(parentController));
    }

    @Override
    public void updateItem(UserModel item, boolean empty) {
        super.updateItem(item, empty);
        playerTileViewController.setPlayer(item);
        playerTileViewController.setSelected(isSelected());
    }

    @Override
    public void updateSelected(boolean selected) {
        super.updateSelected(selected);
        playerTileViewController.setSelected(selected);
    }
    
    
}
