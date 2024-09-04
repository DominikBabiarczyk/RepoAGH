package com.ja.optimgui;

import com.ja.optimgui.math.MVector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        //launch();

        MVector dupa = new MVector(2);
        MVector uwu = new MVector(new double[]{2.0, 3.0});
        dupa.setElement(0,3);
        System.out.println(dupa);
        System.out.println(uwu);





    }
}