package edu.ncssm.chrisholley.pset2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The main Drawing Application
 * @author Brian Sea
 * @version 0.0.1
 * @since PSet 2
 */
public class DrawerApp extends Application {
    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new DrawingSpace(), 500, 500);
        stage.setTitle("Drawer!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}