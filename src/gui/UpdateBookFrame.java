
package gui;

import java.awt.*;
import java.sql.*;
import javax.swing.*;

import db.DBConnection;

public class UpdateBookFrame extends JFrame {

    private JTextField idField;
    private JTextField titleField;
    private JTextField authorField;
    private JTextField categoryField;
    private JTextField quantityField;

    public UpdateBookFrame() {

        setTitle("Digital Library - Update Book");
        setSize(550, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(245, 247, 250));

        // ================= HEADING =================

        JLabel heading = new JLabel("✏ Update Book");
        heading.setBounds(150, 25, 250, 40);
        heading.setFont(new Font("Arial", Font.BOLD, 25));
        heading.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(heading);

        // ================= ID =================

        JLabel idLabel = new JLabel("Book ID:");
        idLabel.setBounds(60, 90, 120, 30);
        idLabel.setFont(new Font("Arial", Font.BOLD, 15));

        panel.add(idLabel);

        idField = new JTextField();
        idField.setBounds(180, 90, 180, 30);

        panel.add(idField);

        JButton loadButton = new JButton("Load");
        loadButton.setBounds(370, 90, 80, 30);

        panel.add(loadButton);

        // ================= TITLE =================

        JLabel titleLabel = new JLabel("Book Title:");
        titleLabel.setBounds(60, 150, 120, 30);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 15));

        panel.add(titleLabel);

        titleField = new JTextField();
        titleField.setBounds(180, 150, 270, 30);

        panel.add(titleField);

        // ================= AUTHOR =================

        JLabel authorLabel = new JLabel("Author:");
        authorLabel.setBounds(60, 200, 120, 30);
        authorLabel.setFont(new Font("Arial", Font.BOLD, 15));

        panel.add(authorLabel);

        authorField = new JTextField();
        authorField.setBounds(180, 200, 270, 30);

        panel.add(authorField);

        // ================= CATEGORY =================

        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setBounds(60, 250, 120, 30);
        categoryLabel.setFont(new Font("Arial", Font.BOLD, 15));

        panel.add(categoryLabel);

        categoryField = new JTextField();
        categoryField.setBounds(180, 250, 270, 30);

        panel.add(categoryField);

        // ================= QUANTITY =================

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setBounds(60, 300, 120, 30);
        quantityLabel.setFont(new Font("Arial", Font.BOLD, 15));

        panel.add(quantityLabel);

        quantityField = new JTextField();
        quantityField.setBounds(180, 300, 270, 30);

        panel.add(quantityField);

        // ================= UPDATE =================

        JButton updateButton = new JButton("Update Book");
        updateButton.setBounds(150, 370, 130, 40);
        updateButton.setFont(new Font("Arial", Font.BOLD, 14));

        panel.add(updateButton);

        // ================= CLEAR =================

        JButton clearButton = new JButton("Clear");
        clearButton.setBounds(290, 370, 100, 40);

        panel.add(clearButton);

        // ================= ACTIONS =================

        loadButton.addActionListener(e -> loadBook());

        updateButton.addActionListener(e -> updateBook());

        clearButton.addActionListener(e -> clearFields());

        add(panel);
    }

    // ================= LOAD BOOK =================

    private void loadBook() {

        String idText = idField.getText().trim();

        if (idText.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Enter Book ID."
            );

            return;
        }

        int id;

        try {

            id = Integer.parseInt(idText);

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Book ID must be a number."
            );

            return;
        }

        String sql =
                "SELECT title, author, category, quantity, available " +
                "FROM books WHERE id = ?";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                titleField.setText(
                        rs.getString("title")
                );

                authorField.setText(
                        rs.getString("author")
                );

                categoryField.setText(
                        rs.getString("category")
                );

                quantityField.setText(
                        String.valueOf(
                                rs.getInt("quantity")
                        )
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Book not found."
                );
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database Error:\n" + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    // ================= UPDATE BOOK =================

    private void updateBook() {

        String idText = idField.getText().trim();
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String category = categoryField.getText().trim();
        String quantityText = quantityField.getText().trim();

        if (idText.isEmpty() ||
            title.isEmpty() ||
            author.isEmpty() ||
            category.isEmpty() ||
            quantityText.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill all fields."
            );

            return;
        }

        int id;
        int newQuantity;

        try {

            id = Integer.parseInt(idText);
            newQuantity = Integer.parseInt(quantityText);

            if (newQuantity <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Quantity must be greater than 0."
                );

                return;
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "ID and Quantity must be numbers."
            );

            return;
        }

        /*
         * Get current quantity and available count.
         * This prevents available books from becoming incorrect.
         */

        String selectSql =
                "SELECT quantity, available FROM books WHERE id = ?";

        String updateSql =
                "UPDATE books SET title=?, author=?, category=?, " +
                "quantity=?, available=? WHERE id=?";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement selectPs =
                        con.prepareStatement(selectSql)
        ) {

            selectPs.setInt(1, id);

            ResultSet rs = selectPs.executeQuery();

            if (!rs.next()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Book not found."
                );

                return;
            }

            int oldQuantity =
                    rs.getInt("quantity");

            int oldAvailable =
                    rs.getInt("available");

            int borrowed =
                    oldQuantity - oldAvailable;

            if (newQuantity < borrowed) {

                JOptionPane.showMessageDialog(
                        this,
                        "Quantity cannot be less than " +
                        borrowed +
                        " because some copies are borrowed."
                );

                return;
            }

            int newAvailable =
                    newQuantity - borrowed;

            try (
                    PreparedStatement updatePs =
                            con.prepareStatement(updateSql)
            ) {

                updatePs.setString(1, title);
                updatePs.setString(2, author);
                updatePs.setString(3, category);
                updatePs.setInt(4, newQuantity);
                updatePs.setInt(5, newAvailable);
                updatePs.setInt(6, id);

                int result =
                        updatePs.executeUpdate();

                if (result > 0) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Book updated successfully!"
                    );

                }
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database Error:\n" + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    // ================= CLEAR =================

    private void clearFields() {

        idField.setText("");
        titleField.setText("");
        authorField.setText("");
        categoryField.setText("");
        quantityField.setText("");

        idField.requestFocus();
    }

    // ================= MAIN =================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            new UpdateBookFrame().setVisible(true);

        });
    }
}
