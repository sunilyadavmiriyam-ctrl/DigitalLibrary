
package gui;

import java.awt.*;
import javax.swing.*;

public class AdminDashboard extends JFrame {

    private String adminEmail;

    public AdminDashboard(String adminEmail) {

        this.adminEmail = adminEmail;

        setTitle("Digital Library - Admin Dashboard");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // =========================
        // MAIN PANEL
        // =========================

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(240, 245, 250));

        // =========================
        // HEADING
        // =========================

        JLabel titleLabel = new JLabel("ADMIN DASHBOARD");

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 28)
        );

        titleLabel.setBounds(220, 30, 300, 40);

        panel.add(titleLabel);

        // =========================
        // WELCOME
        // =========================

        JLabel welcomeLabel = new JLabel(
                "Welcome, Admin"
        );

        welcomeLabel.setFont(
                new Font("Arial", Font.PLAIN, 18)
        );

        welcomeLabel.setBounds(270, 80, 200, 30);

        panel.add(welcomeLabel);

        // =========================
        // ADD BOOK
        // =========================

        JButton addBookButton = new JButton(
                "Add Book"
        );

        addBookButton.setBounds(
                100, 140, 200, 50
        );

        panel.add(addBookButton);

        addBookButton.addActionListener(e -> {

            new AddBookFrame().setVisible(true);

        });

        // =========================
        // UPDATE BOOK
        // =========================

        JButton updateBookButton = new JButton(
                "Update Book"
        );

        updateBookButton.setBounds(
                350, 140, 200, 50
        );

        panel.add(updateBookButton);

        updateBookButton.addActionListener(e -> {

            new UpdateBookFrame().setVisible(true);

        });

        // =========================
        // DELETE BOOK
        // =========================

        JButton deleteBookButton = new JButton(
                "Delete Book"
        );

        deleteBookButton.setBounds(
                100, 210, 200, 50
        );

        panel.add(deleteBookButton);

        deleteBookButton.addActionListener(e -> {

            new DeleteBookFrame().setVisible(true);

        });

        // =========================
        // VIEW BOOKS
        // =========================

        JButton viewBooksButton = new JButton(
                "View Books"
        );

        viewBooksButton.setBounds(
                350, 210, 200, 50
        );

        panel.add(viewBooksButton);

        viewBooksButton.addActionListener(e -> {

            new ViewBooksFrame().setVisible(true);

        });

        // =========================
        // SEARCH BOOKS
        // =========================

        JButton searchBooksButton = new JButton(
                "Search Books"
        );

        searchBooksButton.setBounds(
                100, 280, 200, 50
        );

        panel.add(searchBooksButton);

        searchBooksButton.addActionListener(e -> {

            try {

                new SearchBooksFrame().setVisible(true);

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Search Books screen is not available."
                );

            }

        });

        // =========================
        // MANAGE USERS
        // =========================

        JButton manageUsersButton = new JButton(
                "Manage Users"
        );

        manageUsersButton.setBounds(
                350, 280, 200, 50
        );

        panel.add(manageUsersButton);

        manageUsersButton.addActionListener(e -> {

            try {

                new ManageUsersFrame().setVisible(true);

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "ManageUsersFrame is not created yet."
                );

            }

        });

        // =========================
        // BORROWED BOOKS
        // =========================

        JButton borrowedBooksButton = new JButton(
                "Borrowed Books"
        );

        borrowedBooksButton.setBounds(
                100, 350, 200, 50
        );

        panel.add(borrowedBooksButton);

        borrowedBooksButton.addActionListener(e -> {

            try {

                new AdminBorrowedBooksFrame().setVisible(true);

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "AdminBorrowedBooksFrame is not created yet."
                );

            }

        });

        // =========================
        // LOGOUT
        // =========================

        JButton logoutButton = new JButton(
                "Logout"
        );

        logoutButton.setBounds(
                350, 350, 200, 50
        );

        panel.add(logoutButton);

        logoutButton.addActionListener(e -> {

            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to logout?",
                    "Logout",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {

                dispose();

                new LoginFrame().setVisible(true);
            }

        });

        // =========================
        // ADD PANEL
        // =========================

        add(panel);

        AnimationUtils.showWithFade(this);
    }

    // =========================
    // MAIN
    // =========================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            new AdminDashboard(
                    "admin@gmail.com"
            );

        });
    }
}
