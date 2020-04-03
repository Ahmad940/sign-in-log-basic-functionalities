package controllers;

import com.jfoenix.controls.JFXButton;
import database.DBConnection;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.Main;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainController {


    static MainController instance;
    Boolean log = null;
    @FXML
    private JFXButton btnSign;
    @FXML
    private JFXButton btn_login;
    @FXML
    private Label logError;
    @FXML
    private StackPane pane;
    @FXML
    private AnchorPane mainPane;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private HBox loading;

    public static MainController getInstance() {
        return instance;
    }

    public static double xOffset, yOffset;

    public String email() {
        return emailField.getText();
    }


    public void initialize() {
        btn_login.setDisable(true);
        makeStageDrageable();
    }

    private void makeStageDrageable() {
        mainPane.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        mainPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Main.stage.setX(event.getScreenX() - xOffset);
                Main.stage.setY(event.getScreenY() - yOffset);
                Main.stage.setOpacity(0.7f);
            }
        });
        mainPane.setOnDragDone((e) -> {
            Main.stage.setOpacity(1.0f);
        });
        mainPane.setOnMouseReleased((e) -> {
            Main.stage.setOpacity(1.0f);
        });

    }


    public void login(ActionEvent event) {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            String encodedPassword = DigestUtils.shaHex(passwordField.getText());
            String sqlQuery = "select * from Users where email = ? and password = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, emailField.getText());
            preparedStatement.setString(2, encodedPassword);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                logError.setText("Credentials incorrect");
            } else {
                logError.setText("redirecting ...");
                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(5), loading);
                fadeTransition.setFromValue(0);
                fadeTransition.setToValue(1);
                loading.setVisible(true);
                fadeTransition.play();
                fadeTransition.setOnFinished(event1 -> {
                    loading.setVisible(false);
                    Node node = (Node) event.getSource();
                    Stage stage = (Stage) node.getScene().getWindow();
//                    stage.close();

                    try {
                        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/resources/fxml/home.fxml")));
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                        e.printStackTrace();
                    }
                });
            }

        } catch (SQLException e) {
            System.out.println("Error occurred");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("Could`nt close: " + e.getMessage());
            e.printStackTrace();
        }

    }

    @FXML
    public void setKeyRelease() {
        String nameField = emailField.getText();
        String passField = passwordField.getText();
//        Boolean nameFieldEmpty = nameField.isEmpty();
//        btn_login.setDisable(nameFieldEmpty);

        if (nameField.length() != 0 && passField.length() != 0) {
            btn_login.setDisable(false);
        } else {
            btn_login.setDisable(true);
        }

    }

    @FXML
    public void sign(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
//                    stage.close();

        try {
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/resources/fxml/sign.fxml")));
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
