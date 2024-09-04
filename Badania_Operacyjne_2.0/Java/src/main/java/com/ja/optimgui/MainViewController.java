package com.ja.optimgui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.util.Callback;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TextField c1Box;

    @FXML
    private Label c1ErrorLabel;

    @FXML
    private TextField c2Box;

    @FXML
    private Label c2ErrorLabel;

    @FXML
    private Button calcButton;

    @FXML
    private Tab dataInputTab;

    @FXML
    private TextField inertiaBox;

    @FXML
    private Label inertiaErrorLabel;

    @FXML
    private Slider inertiaSlider;

    @FXML
    private TextField iterStopBox;

    @FXML
    private Label iterStopErrorLabel;

    @FXML
    private ListView<String> penaltyListView;

    @FXML
    private ListView<String> prodListView;

    @FXML
    private Tab prodOutputTab;

    @FXML
    private Tab reportTab;

    @FXML
    private ListView<String> storageListView;

    @FXML
    private TextField swarmSizeBox;

    @FXML
    private Label swarmSizeErrorLabel;

    @FXML
    private TableView<prodInputRow> prodTable;

    @FXML
    private TableColumn<prodInputRow, String> prodTableDayCol;

    @FXML
    private TableColumn<prodInputRow, String> prodTableNeedCol;

    @FXML
    private TableColumn<prodInputRow, String> prodTableOptionsCol;

    @FXML
    private TableColumn<prodInputRow, String> prodTableTruckPenaltyCol;

    @Getter
    private int swarmSize;
    @Getter
    private double inertia;
    @Getter
    private double c1;
    @Getter
    private double c2;
    @Getter
    private int iterStop;

    @Setter
    private MainApp mainApp;

    private enum DataType{
        CHECK_INT,
        CHECK_DOUBLE
    }

    boolean dataPanelEvaluateKey(KeyEvent event, TextField textBox, Label errorLabel, DataType type, int lowerBound, int upperBound){
        if(event.getCode().equals(KeyCode.ENTER)){
            String boxString = textBox.getText();

            boolean isProper = false;

            if(type == DataType.CHECK_DOUBLE){
                isProper = checkValueDouble(boxString, lowerBound, upperBound);
            } else if (type == DataType.CHECK_INT) {
                isProper = checkValueInt(boxString, lowerBound, upperBound);
            }else{
                throw new IllegalStateException();
            }

            if(isProper){
                textBox.setPromptText(boxString);
                textBox.setText("");
                anchorPane.requestFocus();
            }else{
                textBox.setText("");
                //anchorPane.requestFocus();
                errorLabel.setVisible(true);
                delay(2000, () -> errorLabel.setVisible(false));
            }
            return isProper;
        }

        if(event.getCode().equals(KeyCode.ESCAPE)){
            textBox.setText("");
            anchorPane.requestFocus();
        }

        return false;
    }

    boolean dataPanelEvaluateOnExit(TextField textBox, Label errorLabel, DataType type, int lowerBound, int upperBound){
        String boxString = textBox.getText();

        if(boxString.isEmpty()){
            return false;
        }

        boolean isProper = false;

        if(type == DataType.CHECK_DOUBLE){
            isProper = checkValueDouble(boxString, lowerBound, upperBound);
        } else if (type == DataType.CHECK_INT) {
            isProper = checkValueInt(boxString, lowerBound, upperBound);
        }else{
            throw new IllegalStateException();
        }

        if(isProper){
            textBox.setPromptText(boxString);
            textBox.setText("");
            anchorPane.requestFocus();
        }else{
            textBox.setText("");
            //anchorPane.requestFocus();
            errorLabel.setVisible(true);
            delay(2000, () -> errorLabel.setVisible(false));
        }
        return isProper;
    }

    @FXML
    void anchorPaneCLicked(MouseEvent event) {
        anchorPane.requestFocus();
    }

    @FXML
    void c1BoxKeyPressed(KeyEvent event) {
        if(this.dataPanelEvaluateKey(event, c1Box, c1ErrorLabel, DataType.CHECK_DOUBLE, 0, 1000)){
            c1 = Double.parseDouble(c1Box.getPromptText());
        }
    }

    @FXML
    void c2BoxKeypressed(KeyEvent event) {
        if(this.dataPanelEvaluateKey(event, c2Box, c2ErrorLabel, DataType.CHECK_DOUBLE, 0, 1000)){
            c2 = Double.parseDouble(c2Box.getPromptText());
        }
    }

    @FXML
    void inertiaBoxKeyPressed(KeyEvent event) {
        if(this.dataPanelEvaluateKey(event, inertiaBox, inertiaErrorLabel, DataType.CHECK_DOUBLE, 0, 1)){
            inertia = Double.parseDouble(inertiaBox.getPromptText());
            inertiaSlider.adjustValue(inertia);
        }
    }

    @FXML
    void iterStopBoxKeyPressed(KeyEvent event) {
        if(this.dataPanelEvaluateKey(event, iterStopBox, iterStopErrorLabel, DataType.CHECK_INT, 0, 1000)){
            iterStop = Integer.parseInt(iterStopBox.getPromptText());
        }
    }

    @FXML
    void swarmSizeBoxKeyPressed(KeyEvent event) {
        if(this.dataPanelEvaluateKey(event, swarmSizeBox, swarmSizeErrorLabel, DataType.CHECK_INT, 0, 1000)){
            swarmSize = Integer.parseInt(swarmSizeBox.getPromptText());
        }
    }

    public static boolean checkValueDouble(String text, int lowerBound, int upperBound){
        try {
            double val = Double.parseDouble(text);
            return val >= lowerBound && val <= upperBound;

        } catch(NumberFormatException e){
            return false;
        }
    }

    public static boolean checkValueInt(String text, int lowerBound, int upperBound){
        try {
            int val = Integer.parseInt(text);
            return val >= lowerBound && val <= upperBound;

        } catch(NumberFormatException e){
            return false;
        }
    }

    @FXML
    void calcButtonPressed(ActionEvent event) {
        mainApp.calculate();
    }


    @Override
    public String toString() {
        return "MainViewController{" +
                "swarmSize=" + swarmSize +
                ", inertia=" + inertia +
                ", c1=" + c1 +
                ", c2=" + c2 +
                ", iterStop=" + iterStop +
                '}';
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //prodListView.getItems().add();

        swarmSizeErrorLabel.setVisible(false);
        inertiaErrorLabel.setVisible(false);
        c1ErrorLabel.setVisible(false);
        c2ErrorLabel.setVisible(false);
        iterStopErrorLabel.setVisible(false);

        swarmSize = 20;
        swarmSizeBox.setPromptText(String.valueOf(20));

        inertia = 0;
        inertiaBox.setPromptText(String.valueOf(0));

        c1 = 1;
        c1Box.setPromptText(String.valueOf(1));

        c2 = 1;
        c2Box.setPromptText(String.valueOf(1));

        iterStop = 30;
        iterStopBox.setPromptText(String.valueOf(30));

        prodTableDayCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<prodInputRow, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<prodInputRow, String> p) {
                return new SimpleStringProperty(String.valueOf(p.getValue().getDay())) ;
            }
        });

        prodTable.getItems().add(new prodInputRow());

        inertiaSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double sliderVal = Double.parseDouble(String.format(Locale.ENGLISH ,"%.2f", newValue));
                inertia = sliderVal;
                inertiaBox.setPromptText(String.valueOf(sliderVal));
            }
        });

        swarmSizeBox.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue){
                    if(dataPanelEvaluateOnExit(swarmSizeBox, swarmSizeErrorLabel, DataType.CHECK_INT, 0, 1000)){
                        swarmSize = Integer.parseInt(swarmSizeBox.getPromptText());
                    }
                    swarmSizeBox.setText("");
                    anchorPane.requestFocus();
                }
            }
        });

        inertiaBox.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue){
                    if(dataPanelEvaluateOnExit(inertiaBox, inertiaErrorLabel, DataType.CHECK_DOUBLE, 0, 1)){
                        inertia = Double.parseDouble(inertiaBox.getPromptText());
                        inertiaSlider.adjustValue(inertia);
                    }
                    c1Box.setText("");
                    anchorPane.requestFocus();
                }
            }
        });

        c1Box.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue){
                    if(dataPanelEvaluateOnExit(c1Box, c1ErrorLabel, DataType.CHECK_DOUBLE, 0, 1000)){
                        c1 = Integer.parseInt(c1Box.getPromptText());
                    }
                    c1Box.setText("");
                    anchorPane.requestFocus();
                }
            }
        });

        c2Box.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue){
                    if(dataPanelEvaluateOnExit(c2Box, c2ErrorLabel, DataType.CHECK_DOUBLE, 0, 1000)){
                        c2 = Integer.parseInt(c2Box.getPromptText());
                    }
                    c2Box.setText("");
                    anchorPane.requestFocus();
                }
            }
        });

        iterStopBox.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue){
                    if(dataPanelEvaluateOnExit(iterStopBox, iterStopErrorLabel, DataType.CHECK_INT, 0, 1000)){
                        iterStop = Integer.parseInt(iterStopBox.getPromptText());
                    }
                    iterStopBox.setText("");
                    anchorPane.requestFocus();
                }
            }
        });

    }

    public static void delay(long millis, Runnable continuation) {
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try { Thread.sleep(millis); }
                catch (InterruptedException e) { }
                return null;
            }
        };
        sleeper.setOnSucceeded(event -> continuation.run());
        new Thread(sleeper).start();
    }


}
