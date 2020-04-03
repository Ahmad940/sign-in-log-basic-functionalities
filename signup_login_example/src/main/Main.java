package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage stage;
    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/resources/fxml/main.fxml"));

            Scene scene = new Scene(root);
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Login");
            primaryStage.show();
            stage = primaryStage;
        } catch (IOException e) {
            System.out.println("Could`nt load fxml file");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }
}
