/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package tictactoe_server;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import tictactoe_server.managers.ServerSocketManager;

/**
 *
 * @author m-essam
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private Button StartButton;
    @FXML
    private BorderPane borderPane;

    private final ServerSocketManager serverSocketManager;

    private final XYChart.Series<String, Number> series = new XYChart.Series<>();

    public FXMLDocumentController(ServerSocketManager serverSocketManager) {
        this.serverSocketManager = serverSocketManager;
        series.setName("Users");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        CategoryAxis categoryAxis = new CategoryAxis();
        NumberAxis NumberAxis = new NumberAxis();
        BarChart barChart = new BarChart<>(categoryAxis, NumberAxis);

        categoryAxis.setLabel("Statistic");
        NumberAxis.setLabel("Number");

        series.getData().add(new XYChart.Data("Total Users", serverSocketManager.getAllUsers().getValue()));
        series.getData().add(new XYChart.Data("Active Clients", serverSocketManager.getActiveUsers().getValue()));

        serverSocketManager.getAllUsers().addListener((all) -> {
            Platform.runLater(() -> {
                series.getData().get(0).setYValue(all);
            });
        });

        serverSocketManager.getActiveUsers().addListener((active) -> {
            Platform.runLater(() -> {
                series.getData().get(1).setYValue(active);
            });
        });

        serverSocketManager.getServerStatus().addListener((isActive) -> {
            Platform.runLater(() -> {
                if (isActive) {
                    StartButton.setText("Stop");
                } else {
                    StartButton.setText("Start");
                }
            });
        });

        barChart.getData().add(series);

        borderPane.setCenter(barChart);

        StartButton.setOnAction((event) -> {
            if (serverSocketManager.getServerStatus().getValue()) {
                serverSocketManager.stop();
            } else {
                serverSocketManager.start();
            }
        });
    }

}
