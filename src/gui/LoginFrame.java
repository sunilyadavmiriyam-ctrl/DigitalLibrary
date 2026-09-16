package gui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

import db.DBConnection;

public class LoginFrame extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;

    // =========================================
    // CONSTRUCTOR
    // =========================================

    public LoginFrame() {

        setTitle("Digital Library - Login");

        setSize(500, 400);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createUI();

        AnimationUtils.showWithFade(this);
    }

    // =========================================
    // CREATE UI
    // =========================================

    private void createUI() {

        setLayout(new BorderLayout());

        // =====================================
        // TITLE
        // =====================================

        JLabel titleLabel =
                new JLabel("📚 Digital Library");

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 30)
        );

        titleLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        titleLabel.setBorder(
                BorderFactory.createEmptyBorder(
                        25, 10, 20, 10
                )
        );

        add(titleLabel, BorderLayout.NORTH);


        // =====================================
        // FORM PANEL
        // =====================================

        JPanel formPanel =
                new JPanel(new GridLayout(4, 2, 10, 15));

        formPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 50, 20, 50
                )
        );


        // Email
        JLabel emailLabel =
                new JLabel("Email:");

        emailField =
                new JTextField();

        emailField.setFont(
                new Font("Arial", Font.PLAIN, 15)
        );


        // Password
        JLabel passwordLabel =
                new JLabel("Password:");

        passwordField =
                new JPasswordField();

        passwordField.setFont(
                new Font("Arial", Font.PLAIN, 15)
        );


        // Login Button
        JButton loginButton =
                new JButton("Login");

        loginButton.setFont(
                new Font("Arial", Font.BOLD, 15)
        );


        // Register Button
        JButton registerButton =
                new JButton("Register");

        registerButton.setFont(
                new Font("Arial", Font.BOLD, 15)
        );


        formPanel.add(emailLabel);
        formPanel.add(emailField);

        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        formPanel.add(loginButton);
        formPanel.add(registerButton);


        add(formPanel, BorderLayout.CENTER);


        // =====================================
        // BOTTOM PANEL
        // =====================================

        JPanel bottomPanel =
                new JPanel();

        JButton exitButton =
                new JButton("Exit");

        bottomPanel.add(exitButton);

        add(bottomPanel, BorderLayout.SOUTH);


        // =====================================
        // LOGIN ACTION
        // =====================================

        loginButton.addActionListener(e -> {

            loginUser();

        });


        // Press ENTER to Login
        passwordField.addActionListener(e -> {

            loginUser();

        });


        // =====================================
        // REGISTER ACTION
        // =====================================

        registerButton.addActionListener(e -> {

            JOptionPane.showMessageDialog(
                    this,
                    "Registration page will be added next."
            );

        });


        // =====================================
        // EXIT ACTION
        // =====================================

        exitButton.addActionListener(e -> {

            int result =
                    JOptionPane.showConfirmDialog(
                            this,
                            "Do you want to exit?",
                            "Exit",
                            JOptionPane.YES_NO_OPTION
                    );

            if (result == JOptionPane.YES_OPTION) {

                System.exit(0);

            }

        });
    }


    // =========================================
    // LOGIN USER
    // =========================================

    private void loginUser() {

        String email =
                emailField.getText().trim();

        String password =
                new String(
                        passwordField.getPassword()
                );


        // =====================================
        // VALIDATION
        // =====================================

        if (email.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter your email.",
                    "Login Error",
                    JOptionPane.WARNING_MESSAGE
            );

            emailField.requestFocus();

            return;
        }


        if (password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter your password.",
                    "Login Error",
                    JOptionPane.WARNING_MESSAGE
            );

            passwordField.requestFocus();

            return;
        }


        // =====================================
        // SQL
        // =====================================

        String sql =
                "SELECT id, name, email, password, role " +
                "FROM users " +
                "WHERE email = ? AND password = ?";


        try {

            Connection con =
                    DBConnection.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(sql);


            ps.setString(1, email);

            ps.setString(2, password);


            ResultSet rs =
                    ps.executeQuery();


            // =================================
            // USER FOUND
            // =================================

            if (rs.next()) {

                int userId =
                        rs.getInt("id");

                String userName =
                        rs.getString("name");

                String role =
                        rs.getString("role");


                JOptionPane.showMessageDialog(
                        this,
                        "Login Successful!\nWelcome "
                                + userName,
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );


                // =============================
                // CLOSE LOGIN
                // =============================

                dispose();


                // =============================
                // ADMIN
                // =============================

                if (role.equalsIgnoreCase("ADMIN")) {

                                        new AdminDashboard(email);

                }


                // =============================
                // NORMAL USER
                // =============================

                else {

                    new UserDashboard(
                            userId,
                            userName
                    );

                }

            }

            // =================================
            // INVALID LOGIN
            // =================================

            else {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid email or password.",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                );

                passwordField.setText("");

                passwordField.requestFocus();
            }


            // =================================
            // CLOSE DATABASE
            // =================================

            rs.close();

            ps.close();

            con.close();


        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database connection error:\n\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }


    // =========================================
    // MAIN METHOD
    // =========================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            new LoginFrame();

        });

    }
}