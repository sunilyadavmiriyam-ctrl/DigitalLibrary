package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

import db.DBConnection;

public class BorrowedBooksFrame extends JFrame {

        private JTable table;
        private DefaultTableModel model;
        private int userId;
        private JLabel totalLabel;

        public BorrowedBooksFrame(int userId) {

                this.userId = userId;

                setTitle("My Borrowed Books");
                setSize(850, 500);
                setLocationRelativeTo(null);
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                JLabel title = new JLabel("My Borrowed Books", SwingConstants.CENTER);
                title.setFont(new Font("Arial", Font.BOLD, 28));

                model = new DefaultTableModel(
                                new String[]{"Borrow ID", "Book ID", "Book Title", "Author",
                                                "Category", "Borrow Date", "Return Date", "Status"},
                                0
                );

                table = new JTable(model);
                table.setRowHeight(30);
                table.setFont(new Font("Arial", Font.PLAIN, 14));
                table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

                JButton refreshButton = new JButton("Refresh");
                refreshButton.setFont(new Font("Arial", Font.BOLD, 14));

                totalLabel = new JLabel();
                refreshButton.addActionListener(e -> loadBorrowedBooks());

                JPanel bottomPanel = new JPanel();
                bottomPanel.add(totalLabel);
                bottomPanel.add(refreshButton);

                add(title, BorderLayout.NORTH);
                add(new JScrollPane(table), BorderLayout.CENTER);
                add(bottomPanel, BorderLayout.SOUTH);

                loadBorrowedBooks();
                setVisible(true);
        }

        private void loadBorrowedBooks() {

                model.setRowCount(0);

                String sql =
                                "SELECT bb.id, b.id AS book_id, b.title, b.author, b.category, "
                                                + "bb.borrow_date, bb.return_date, bb.status "
                                                + "FROM borrowings bb "
                                                + "JOIN books b ON bb.book_id = b.id "
                                                + "WHERE bb.user_id = ? "
                                                + "ORDER BY bb.id ASC";

                try (Connection con = DBConnection.getConnection();
                         PreparedStatement ps = con.prepareStatement(sql)) {

                        ps.setInt(1, userId);

                        try (ResultSet rs = ps.executeQuery()) {
                                while (rs.next()) {
                                        model.addRow(new Object[]{
                                                        rs.getInt("id"),
                                                        rs.getInt("book_id"),
                                                        rs.getString("title"),
                                                        rs.getString("author"),
                                                        rs.getString("category"),
                                                        rs.getDate("borrow_date"),
                                                        rs.getDate("return_date"),
                                                        rs.getString("status")
                                        });
                                }
                        }

                        totalLabel.setText("Total borrowed books: " + model.getRowCount());

                } catch (SQLException e) {
                        JOptionPane.showMessageDialog(
                                        this,
                                        "Database Error:\n" + e.getMessage(),
                                        "Error",
                                        JOptionPane.ERROR_MESSAGE
                        );
                }
        }

        public static void main(String[] args) {
                SwingUtilities.invokeLater(() -> new BorrowedBooksFrame(1));
        }
}