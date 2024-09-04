package com.ja.optimgui;

import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class addCellCell extends TableCell<String, String > {

    HBox hbox = new HBox();
    Button button = new Button();
    String lastItem;

    public addCellCell(){
        super();
        hbox.getChildren().add(button);
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);  // No text in label of super class
        if (empty) {
            lastItem = null;
            setGraphic(null);
        } else {
            lastItem = item;
            setGraphic(hbox);
        }
    }
}
