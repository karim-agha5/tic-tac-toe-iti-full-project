/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictactoe.utils;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;

public abstract class UIHelper {
    public static Effect createBlurEffect() {
        ColorAdjust adj = new ColorAdjust(0, -0.3, -0.3, -0.6);
        GaussianBlur blur = new GaussianBlur(30);
        adj.setInput(blur);
        return adj;
    }
}
