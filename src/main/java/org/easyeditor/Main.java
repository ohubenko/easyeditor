package org.easyeditor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/org.easyeditor/main.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        primaryStage.setTitle("EasyEditor");
        primaryStage.getIcons().add(new Image("file:icon.png"));
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }
}
