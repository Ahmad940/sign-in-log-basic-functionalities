package controllers;

import com.jfoenix.controls.JFXButton;
import database.DBConnection;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import main.Main;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class SignController {
    @FXML
    private AnchorPane mainPane;

    @FXML
    private TextField fName;

    @FXML
    private TextField lName;

    @FXML
    private TextField emailfield;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label logError;

    @FXML
    private JFXButton btn_signin;

    @FXML
    private HBox loading;

    @FXML
    public void setKeyRelease() {
        String firstName = fName.getText();
        String lastName = lName.getText();
        String email = emailfield.getText();
        String passField = passwordField.getText();

        if (firstName.length() != 0 && lastName.length() != 0 && email.length() != 0 && passField.length() != 0) {
            btn_signin.setDisable(false);
        } else {
            btn_signin.setDisable(true);
        }

    }

    @FXML
    public void log(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
//                    stage.close();

        try {
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/resources/fxml/main.fxml")));
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void sign_in(ActionEvent event){
        Connection connection = DBConnection.getInstance().getConnection();
        String sqlQuery = "INSERT INTO `Users` (`firstname`, `lastname`, `email`, `password`) VALUES (?, ?, ?, ?)";
        try {
            String encodedPass = DigestUtils.shaHex(passwordField.getText());
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, fName.getText());
            preparedStatement.setString(2, lName.getText());
            preparedStatement.setString(3, emailfield.getText());
            preparedStatement.setString(4, encodedPass);

            int status = preparedStatement.executeUpdate();
            if (status>0){
                System.out.println("success");
                System.out.println("log in");
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
//                    stage.close();

                try {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("Welcome ...");
                    alert.setContentText("Account created successfully");
                    styleAlert(alert);
                    alert.showAndWait();

                    Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/resources/fxml/main.fxml")));
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    System.out.println(e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
            else System.out.println("Error occured");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("could`nt close connection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void styleAlert(Alert alert) {
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        DialogPane dialogPane = alert.getDialogPane();
//        dialogPane.getStylesheets().add(AlertMaker.class.getResource("/resources/dark-theme.css").toExternalForm());
//        dialogPane.getStyleClass().add("custom-alert");
    }
    double xOffset, yOffset;

    @FXML
    public void initialize(){
        btn_signin.setDisable(true);
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

}
