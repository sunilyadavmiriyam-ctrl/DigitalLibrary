
package gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import db.DBConnection;

public class AdminBorrowedBooksFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    public AdminBorrowedBooksFrame() {

        setTitle("Digital Library - Admin Borrowed Books");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // =========================
        // MAIN PANEL
        // =========================

        JPanel panel = new JPanel(new BorderLayout());

        // =========================
        // TITLE
        // =========================

        JLabel titleLabel = new JLabel(
                "ALL BORROWED BOOKS",
                SwingConstants.CENTER
        );

        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));

        panel.add(titleLabel, BorderLayout.NORTH);

        // =========================
        // TABLE
        // =========================

        String[] columns = {
                "Borrow ID",
                "User ID",
                "Book ID",
                "Borrow Date",
                "Return Date",
                "Status"
        };

        model = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);

        table.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(table);

        panel.add(scrollPane, BorderLayout.CENTER);

        // =========================
        // BUTTONS
        // =========================

        JPanel bottomPanel = new JPanel();

        JButton refreshButton = new JButton("Refresh");
        JButton closeButton = new JButton("Close");

        bottomPanel.add(refreshButton);
        bottomPanel.add(closeButton);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        // =========================
        // REFRESH
        // =========================

        refreshButton.addActionListener(e -> loadBorrowedBooks());

        // =========================
        // CLOSE
        // =========================

        closeButton.addActionListener(e -> dispose());

        add(panel);

        // Load data when screen opens
        loadBorrowedBooks();
    }

    // =====================================================
    // LOAD BORROWED BOOKS FROM DATABASE
    // =====================================================

    private void loadBorrowedBooks() {

        model.setRowCount(0);

        String sql = "SELECT id, user_id, book_id, borrow_date, return_date, status "
                   + "FROM borrowings "
                   + "ORDER BY id DESC";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int borrowId = rs.getInt("id");
                int userId = rs.getInt("user_id");
                int bookId = rs.getInt("book_id");

                String borrowDate = rs.getString("borrow_date");
                String returnDate = rs.getString("return_date");
                String status = rs.getString("status");

                model.addRow(new Object[] {
                        borrowId,
                        userId,
                        bookId,
                        borrowDate,
                        returnDate,
                        status
                });
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load borrowed books.\n\n"
                    + "Make sure the 'borrowings' table exists.\n\n"
                    + "Error: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    // =====================================================
    // MAIN METHOD
    // =====================================================

    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(() -> {

            new AdminBorrowedBooksFrame().setVisible(true);

        });
    }
}
