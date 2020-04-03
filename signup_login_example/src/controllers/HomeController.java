package controllers;

import database.DBConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HomeController {
    @FXML
    private Label fName;

    @FXML
    private Label lName;

    @FXML
    private Label email;

    String firstName = null;
    String lastName = null;
    String mail = null;

    public void initialize(){
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            Statement statement = connection.createStatement();
            String query = "select * from users";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                firstName = resultSet.getString(1);
                lastName = resultSet.getString(2);
                mail = resultSet.getString(3);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        fName.setText(firstName);
        lName.setText(lastName);
        email.setText(mail);
    }
}
