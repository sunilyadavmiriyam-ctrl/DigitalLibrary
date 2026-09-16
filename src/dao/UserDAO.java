package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import db.DBConnection;
import model.User;

public class UserDAO {

    // =========================
    // REGISTER USER
    // =========================
    public boolean registerUser(User user) {

        String sql = "INSERT INTO users (name, email, password, role) "
                   + "VALUES (?, ?, ?, ?)";

        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, user.name);
            ps.setString(2, user.email);
            ps.setString(3, user.password);
            ps.setString(4, user.role);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Registration Successful!");
                return true;
            }

        } catch (Exception e) {
            System.out.println("Registration Failed!");
            e.printStackTrace();
        }

        return false;
    }


    // =========================
    // LOGIN USER
    // =========================
    public User loginUser(String email, String password) {

        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";

        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                User user = new User();

                user.id = rs.getInt("id");
                user.name = rs.getString("name");
                user.email = rs.getString("email");
                user.password = rs.getString("password");
                user.role = rs.getString("role");

                return user;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}