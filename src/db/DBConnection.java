package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/digital_library";

    private static final String USER = "root";

    private static final String PASSWORD = "root@123";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found.", e);
        }

        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        System.out.println("Database Connected Successfully!");
        return connection;
    }
}