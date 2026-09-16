
package gui;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import db.DBConnection;

public class ViewBooksFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    public ViewBooksFrame() {

        setTitle("Digital Library - View Books");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel =
                new JPanel(new BorderLayout());

        mainPanel.setBackground(
                new Color(245, 247, 250)
        );

        // ================= TITLE =================

        JLabel title =
                new JLabel(
                        "📚 Library Books",
                        SwingConstants.CENTER
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        25
                )
        );

        title.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 10, 15, 10
                )
        );

        mainPanel.add(
                title,
                BorderLayout.NORTH
        );

        // ================= TABLE =================

        String[] columns = {
                "ID",
                "Title",
                "Author",
                "Category",
                "Quantity",
                "Available"
        };

        model =
                new DefaultTableModel(
                        columns,
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {

                        return false;
                    }
                };

        table = new JTable(model);

        table.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        table.setRowHeight(30);

        table.getTableHeader().setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        JScrollPane scrollPane =
                new JScrollPane(table);

        mainPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        // ================= BOTTOM =================

        JPanel bottomPanel =
                new JPanel();

        JButton refreshButton =
                new JButton("🔄 Refresh");

        JButton closeButton =
                new JButton("Close");

        refreshButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        closeButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        bottomPanel.add(refreshButton);
        bottomPanel.add(closeButton);

        mainPanel.add(
                bottomPanel,
                BorderLayout.SOUTH
        );

        // ================= ACTIONS =================

        refreshButton.addActionListener(
                e -> loadBooks()
        );

        closeButton.addActionListener(
                e -> dispose()
        );

        add(mainPanel);

        // Load books automatically
        loadBooks();
    }

    // ================= LOAD BOOKS =================

    private void loadBooks() {

        model.setRowCount(0);

        String sql =
                "SELECT id, title, author, category, " +
                "quantity, available FROM books " +
                "ORDER BY id ASC";

        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql);

                ResultSet rs =
                        ps.executeQuery()
        ) {

            while (rs.next()) {

                model.addRow(
                        new Object[] {

                                rs.getInt("id"),

                                rs.getString("title"),

                                rs.getString("author"),

                                rs.getString("category"),

                                rs.getInt("quantity"),

                                rs.getInt("available")
                        }
                );
            }

            if (model.getRowCount() == 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "No books found in the database."
                );
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error loading books:\n" +
                    e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    // ================= MAIN =================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            new ViewBooksFrame().setVisible(true);

        });
    }
}
