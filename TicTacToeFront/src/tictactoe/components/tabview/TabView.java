/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictactoe.components.tabview;

import com.sun.javafx.collections.TrackableObservableList;
import java.util.List;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

public class TabView extends BorderPane {
    private static final String TAB_CLASS = "Tab";
    private static final String SELECTED_CLASS = "SelectedTab";
    private static final String UNSELECTED_CLASS = "UnselectedTab";
    private final HBox tabHeader = new HBox();
    private final ObservableList<Tab> tabs;
    private int selected = -1;

    public TabView(List<Tab> tabs) {
        this.tabs = new TrackableObservableList<Tab>(tabs) {
            @Override
            protected void onChanged(ListChangeListener.Change<Tab> c) {
                setupTabs(c.getList());
            }
        };
        setupView();
    }

    private void setupView() {
        setTop(tabHeader);
        tabHeader.setAlignment(Pos.CENTER);
        tabHeader.setSpacing(10);
        setupTabs(tabs);
    }
    
    private void setupTabs(List<Tab> tabs) {
        tabHeader.getChildren().clear();
        List<Node> tabNodes = tabHeader.getChildren();
        for (Tab tab : tabs) {
            Node node = createTab(tab);
            tabNodes.add(node);
            HBox.setHgrow(node, Priority.ALWAYS);
        }
        setSelected(selected);
    }

    private void setSelected(Tab selectedTab) {
        setSelected(tabs.indexOf(selectedTab));
    }

    private void setSelected(int selectedIndex) {
        unselect(selected);
        selected = Math.min(Math.max(selectedIndex, 0), tabs.size() - 1);
        select(selected);
    }
    
    private void unselect(int currentSelected) {
        if (currentSelected >= 0 && currentSelected < tabs.size()) {
            Node node = tabHeader.getChildren().get(currentSelected);
            List<String> classes = node.getStyleClass();
            classes.remove(SELECTED_CLASS);
            if (!classes.contains(UNSELECTED_CLASS)) {
                classes.add(UNSELECTED_CLASS);
            }
        }
    }
    
    private void select(int currentSelected) {
        if (currentSelected >= 0 && currentSelected < tabs.size()) {
            Node node = tabHeader.getChildren().get(currentSelected);
            List<String> classes = node.getStyleClass();
            classes.remove(UNSELECTED_CLASS);
            if (!classes.contains(SELECTED_CLASS)) {
                classes.add(SELECTED_CLASS);
            }
            Node view = tabs.get(selected).view;
            StackPane viewGroup = new StackPane(view);
            StackPane.setAlignment(view, Pos.CENTER);
            viewGroup.setPadding(new Insets(20, 0, 0, 0));
            setCenter(viewGroup);
        } else {
            setCenter(null);
        }
    }

    private Node createTab(Tab tab) {
        Button tabLabel = new Button(tab.label);
        tabLabel.setOnAction((e) -> {
            setSelected(tab);
        });
        tabLabel.getStyleClass().add(TAB_CLASS);
        tabLabel.getStyleClass().add(UNSELECTED_CLASS);
        return tabLabel;
    }

    public static class Tab {

        private final Node view;
        private final String label;

        public Tab(String label, Node view) {
            this.label = label;
            this.view = view;
        }
    }
}
