package edu.ncssm.chris.pset5;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The main Drawing Application
 * @author Brian Sea
 * @version 0.0.1
 * @since PSet 2
 */
public class DrawerApp extends Application {
    private WorkSpace ws;

    @Override
    public void start(Stage stage) throws IOException {
        ws = new WorkSpace();
        ws.addEventHandler(KeyEvent.ANY, new KeyHandler());

        Scene scene = new Scene(ws, 500, 500);
        stage.setTitle("Drawer!");
        stage.setScene(scene);
        stage.show();
    }
    private class KeyHandler implements EventHandler<KeyEvent> {

        @Override
        public void handle(KeyEvent event) {
            ws.handleKeyEvent(event);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}