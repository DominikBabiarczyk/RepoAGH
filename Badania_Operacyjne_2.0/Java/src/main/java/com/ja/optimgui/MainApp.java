package com.ja.optimgui;

import com.ja.model.part.FactoryImpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainApp extends Application {

    private int swarmSize;
    private double inertia;
    private double c1;
    private double c2;
    private int iterStop;
    private MainViewController mainViewController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainView.fxml"));
        Parent root = (Parent) fxmlLoader.load();

        mainViewController =  fxmlLoader.getController();
        mainViewController.setMainApp(this);

        Scene scene = new Scene(root);
        primaryStage.setTitle("Badanie produkcji");
        Image programIcon = new Image("wuzekpsculka.png");
        primaryStage.getIcons().add(programIcon);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void calculate(){
        FactoryImpl factory = new FactoryImpl();

        swarmSize = mainViewController.getSwarmSize();
        inertia = mainViewController.getInertia();
        c1 = mainViewController.getC1();
        c2 = mainViewController.getC2();
        iterStop = mainViewController.getIterStop();

        factory.setSwarmSize(swarmSize);
        factory.setInertia(inertia);
        factory.setC1(c1);
        factory.setC2(c2);
        factory.setIterStop(iterStop);
        var history = factory.compute();

        System.out.println(history.getTotalCost());
        System.out.println(history.getProductionHistoryRounded());
    }

    public static void main(String[] args) {
        launch();
    }


}
