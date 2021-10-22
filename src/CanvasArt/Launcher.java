package CanvasArt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("ArtCanvas.fxml"));

        Scene scene = new Scene(root, 800,400);
        stage.setTitle("Static Art with Canvas Node");
        stage.setScene(scene);
        stage.show();

    }
}
