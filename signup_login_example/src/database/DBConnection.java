package database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    static final String DB_NAME = "demo"; // database name
    static final String USER = "root"; // database user name
    static final String PASSWORD = ""; // database password
    static final String TIME_ZONE_CONFIG = "?useTimezone=true&serverTimezone=UTC"; // timezone property config
    static final String URL = "jdbc:mysql://localhost:3306/" + DB_NAME + TIME_ZONE_CONFIG; // full url for database connection


    // creating instance of our DBConnectionClass
    static DBConnection instance = new DBConnection();

    //getting our connection method ie singleton(Design Pattern)
    public static DBConnection getInstance() {
        return instance;
    }

    // creating the connection method
    public Connection getConnection() {
        // try with resources
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            System.out.println("Could`nt connect to the database: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

    }
}
