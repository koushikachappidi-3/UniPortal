package db;

import utils.ConfigReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtils {

    public static Connection getConnection() {
        String url = ConfigReader.get("db.url");
        String user = ConfigReader.get("db.user");
        String password = ConfigReader.get("db.password");

        try {
            return DriverManager.getConnection(url, user, password);

        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to PostgreSQL at " + url + " as user " + user, e);
        }
    }
}

