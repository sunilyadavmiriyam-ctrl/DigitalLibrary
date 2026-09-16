package gui;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.*;

import db.DBConnection;

public class DeleteBookFrame extends JFrame {

    private JTextField idField;
    private JTextArea bookInfo;
    private JButton searchButton, deleteButton, closeButton;

    public DeleteBookFrame() {

        setTitle("Delete Book");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main panel
        JPanel panel = new JPanel();
        panel.setLayout(null);

        // Book ID
        JLabel idLabel = new JLabel("Book ID:");
        idLabel.setBounds(50, 40, 100, 30);
        panel.add(idLabel);

        idField = new JTextField();
        idField.setBounds(150, 40, 180, 30);
        panel.add(idField);

        // Search button
        searchButton = new JButton("Search");
        searchButton.setBounds(340, 40, 90, 30);
        panel.add(searchButton);

        // Book information
        JLabel infoLabel = new JLabel("Book Information:");
        infoLabel.setBounds(50, 90, 150, 30);
        panel.add(infoLabel);

        bookInfo = new JTextArea();
        bookInfo.setEditable(false);
        bookInfo.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(bookInfo);
        scrollPane.setBounds(50, 125, 380, 120);
        panel.add(scrollPane);

        // Delete button
        deleteButton = new JButton("Delete Book");
        deleteButton.setBounds(120, 280, 120, 35);
        deleteButton.setEnabled(false);
        panel.add(deleteButton);

        // Close button
        closeButton = new JButton("Close");
        closeButton.setBounds(260, 280, 100, 35);
        panel.add(closeButton);

        add(panel);

        // Search action
        searchButton.addActionListener(e -> searchBook());

        // Delete action
        deleteButton.addActionListener(e -> deleteBook());

        // Close action
        closeButton.addActionListener(e -> dispose());
    }

    // =========================
    // SEARCH BOOK
    // =========================
    private void searchBook() {

        String idText = idField.getText().trim();

        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter Book ID."
            );
            return;
        }

        try {

            int id = Integer.parseInt(idText);

            String sql = "SELECT * FROM books WHERE id = ?";

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                bookInfo.setText(
                        "ID: " + rs.getInt("id") +
                        "\nTitle: " + rs.getString("title") +
                        "\nAuthor: " + rs.getString("author") +
                        "\nCategory: " + rs.getString("category") +
                        "\nQuantity: " + rs.getInt("quantity") +
                        "\nAvailable: " + rs.getInt("available")
                );

                deleteButton.setEnabled(true);

            } else {

                bookInfo.setText("Book not found.");
                deleteButton.setEnabled(false);

            }

            rs.close();
            ps.close();

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Book ID must be a number."
            );

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error: " + ex.getMessage()
            );
        }
    }

    // =========================
    // DELETE BOOK
    // =========================
    private void deleteBook() {

        String idText = idField.getText().trim();

        try {

            int id = Integer.parseInt(idText);

            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this book?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice != JOptionPane.YES_OPTION) {
                return;
            }

            String sql = "DELETE FROM books WHERE id = ?";

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Book deleted successfully!"
                );

                bookInfo.setText("");
                idField.setText("");
                deleteButton.setEnabled(false);

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Book not found."
                );
            }

            ps.close();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error: " + ex.getMessage()
            );
        }
    }

    // =========================
    // MAIN
    // =========================
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new DeleteBookFrame().setVisible(true);
        });
    }
}