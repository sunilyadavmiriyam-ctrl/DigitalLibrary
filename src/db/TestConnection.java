package db;

import java.sql.Connection;

public class TestConnection {

    public static void main(String[] args) {
        try (Connection connection = DBConnection.getConnection()) {
            System.out.println(connection.isValid(2)
                    ? "Connection Successful!"
                    : "Connection Failed!");
        } catch (Exception e) {
            System.out.println("Connection Failed!");
            e.printStackTrace();
        }
    }
}