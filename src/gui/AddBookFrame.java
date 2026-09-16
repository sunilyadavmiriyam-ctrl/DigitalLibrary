
package gui;

import java.awt.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import javax.swing.*;

import db.DBConnection;

public class AddBookFrame extends JFrame {

    private JTextField titleField;
    private JTextField authorField;
    private JTextField categoryField;
    private JTextField quantityField;

    public AddBookFrame() {

        setTitle("Digital Library - Add Book");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(245, 247, 250));

        // ================= TITLE =================

        JLabel heading = new JLabel("➕ Add New Book");
        heading.setBounds(120, 25, 260, 40);
        heading.setFont(new Font("Arial", Font.BOLD, 25));
        heading.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(heading);

        // ================= BOOK TITLE =================

        JLabel titleLabel = new JLabel("Book Title:");
        titleLabel.setBounds(60, 100, 120, 30);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 15));

        panel.add(titleLabel);

        titleField = new JTextField();
        titleField.setBounds(180, 100, 240, 30);

        panel.add(titleField);

        // ================= AUTHOR =================

        JLabel authorLabel = new JLabel("Author:");
        authorLabel.setBounds(60, 150, 120, 30);
        authorLabel.setFont(new Font("Arial", Font.BOLD, 15));

        panel.add(authorLabel);

        authorField = new JTextField();
        authorField.setBounds(180, 150, 240, 30);

        panel.add(authorField);

        // ================= CATEGORY =================

        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setBounds(60, 200, 120, 30);
        categoryLabel.setFont(new Font("Arial", Font.BOLD, 15));

        panel.add(categoryLabel);

        categoryField = new JTextField();
        categoryField.setBounds(180, 200, 240, 30);

        panel.add(categoryField);

        // ================= QUANTITY =================

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setBounds(60, 250, 120, 30);
        quantityLabel.setFont(new Font("Arial", Font.BOLD, 15));

        panel.add(quantityLabel);

        quantityField = new JTextField();
        quantityField.setBounds(180, 250, 240, 30);

        panel.add(quantityField);

        // ================= ADD BUTTON =================

        JButton addButton = new JButton("Add Book");
        addButton.setBounds(150, 315, 100, 40);
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setFocusPainted(false);

        panel.add(addButton);

        // ================= CLEAR BUTTON =================

        JButton clearButton = new JButton("Clear");
        clearButton.setBounds(260, 315, 100, 40);
        clearButton.setFont(new Font("Arial", Font.BOLD, 14));
        clearButton.setFocusPainted(false);

        panel.add(clearButton);

        // ================= ACTIONS =================

        addButton.addActionListener(e -> addBook());

        clearButton.addActionListener(e -> clearFields());

        add(panel);
    }

    // ================= ADD BOOK =================

    private void addBook() {

        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String category = categoryField.getText().trim();
        String quantityText = quantityField.getText().trim();

        // Validation

        if (title.isEmpty() ||
            author.isEmpty() ||
            category.isEmpty() ||
            quantityText.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill all fields."
            );

            return;
        }

        int quantity;

        try {

            quantity = Integer.parseInt(quantityText);

            if (quantity <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Quantity must be greater than 0."
                );

                return;
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Quantity must be a number."
            );

            return;
        }

        String sql =
                "INSERT INTO books " +
            "(title, author, category, quantity, available, book_url) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

        String bookUrl = "https://www.google.com/search?tbm=bks&q="
            + URLEncoder.encode(title + " " + author, StandardCharsets.UTF_8);

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setString(1, title);
            ps.setString(2, author);
            ps.setString(3, category);
            ps.setInt(4, quantity);

            // Initially all books are available
            ps.setInt(5, quantity);
            ps.setString(6, bookUrl);

            int result = ps.executeUpdate();

            if (result > 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Book added successfully!"
                );

                clearFields();
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database Error:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    // ================= CLEAR =================

    private void clearFields() {

        titleField.setText("");
        authorField.setText("");
        categoryField.setText("");
        quantityField.setText("");

        titleField.requestFocus();
    }

    // ================= MAIN =================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            new AddBookFrame().setVisible(true);

        });
    }
}
