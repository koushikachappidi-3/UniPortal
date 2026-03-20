package db;

import utils.ConfigReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtils {

    public static Connection getConnection() {
        try {
            String url = ConfigReader.get("db.url");
            String user = ConfigReader.get("db.user");
            String password = ConfigReader.get("db.password");

            return DriverManager.getConnection(url, user, password);

        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to PostgreSQL", e);
        }
    }
}

