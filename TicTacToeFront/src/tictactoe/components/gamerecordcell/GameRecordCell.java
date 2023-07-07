/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictactoe.components.gamerecordcell;

import javafx.scene.control.ListCell;
import tictactoe.base.ViewController;

public class GameRecordCell extends ListCell<String> {

    private final GameRecordTileViewController gameRecordTileViewController;
    
    public GameRecordCell(ViewController parentController) {
        gameRecordTileViewController = new GameRecordTileViewController();
        setGraphic(gameRecordTileViewController.attachTo(parentController));
    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        gameRecordTileViewController.setGameRecord(item);
        gameRecordTileViewController.setSelected(isSelected());
    }

    @Override
    public void updateSelected(boolean selected) {
        super.updateSelected(selected);
        gameRecordTileViewController.setSelected(selected);
    }
    
    
}
