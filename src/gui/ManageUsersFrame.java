
package gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
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

public class ManageUsersFrame extends JFrame {

    private JTable userTable;
    private DefaultTableModel tableModel;

    public ManageUsersFrame() {

        setTitle("Digital Library - Manage Users");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // =========================
        // MAIN PANEL
        // =========================

        JPanel mainPanel = new JPanel(new BorderLayout());

        // =========================
        // TITLE
        // =========================

        JLabel titleLabel = new JLabel(
                "MANAGE USERS",
                SwingConstants.CENTER
        );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 26)
        );

        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // =========================
        // TABLE
        // =========================

        String[] columns = {
                "ID",
                "Name",
                "Email",
                "Role"
        };

        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(
                    int row,
                    int column) {

                return false;
            }
        };

        userTable = new JTable(tableModel);

        userTable.setRowHeight(30);

        JScrollPane scrollPane =
                new JScrollPane(userTable);

        mainPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        // =========================
        // BUTTON PANEL
        // =========================

        JPanel buttonPanel =
                new JPanel(new GridLayout(1, 3, 10, 10));

        JButton refreshButton =
                new JButton("Refresh");

        JButton deleteButton =
                new JButton("Delete User");

        JButton closeButton =
                new JButton("Close");

        buttonPanel.add(refreshButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(closeButton);

        mainPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        // =========================
        // REFRESH BUTTON
        // =========================

        refreshButton.addActionListener(e -> {

            loadUsers();

        });

        // =========================
        // DELETE USER BUTTON
        // =========================

        deleteButton.addActionListener(e -> {

            deleteSelectedUser();

        });

        // =========================
        // CLOSE BUTTON
        // =========================

        closeButton.addActionListener(e -> {

            dispose();

        });

        // =========================
        // ADD PANEL
        // =========================

        add(mainPanel);

        // Load users when window opens
        loadUsers();
    }

    // =====================================================
    // LOAD USERS
    // =====================================================

    private void loadUsers() {

        tableModel.setRowCount(0);

        String sql =
                "SELECT id, name, email, role "
                + "FROM users "
                + "ORDER BY id";

        try {

            Connection con =
                    DBConnection.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ResultSet rs =
                    ps.executeQuery();

            while (rs.next()) {

                int id =
                        rs.getInt("id");

                String name =
                        rs.getString("name");

                String email =
                        rs.getString("email");

                String role =
                        rs.getString("role");

                tableModel.addRow(
                        new Object[]{
                                id,
                                name,
                                email,
                                role
                        }
                );
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load users.\n\n"
                    + "Error: "
                    + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    // =====================================================
    // DELETE SELECTED USER
    // =====================================================

    private void deleteSelectedUser() {

        int selectedRow =
                userTable.getSelectedRow();

        // No user selected
        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a user first.",
                    "No User Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int userId =
                (int) tableModel.getValueAt(
                        selectedRow,
                        0
                );

        String userName =
                tableModel.getValueAt(
                        selectedRow,
                        1
                ).toString();

        String userRole =
                tableModel.getValueAt(
                        selectedRow,
                        3
                ).toString();

        // Don't allow admin deletion
        if (userRole.equalsIgnoreCase("ADMIN")) {

            JOptionPane.showMessageDialog(
                    this,
                    "Admin users cannot be deleted.",
                    "Delete Not Allowed",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int choice =
                JOptionPane.showConfirmDialog(
                        this,
                        "Delete user:\n"
                        + userName
                        + " ?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                );

        if (choice != JOptionPane.YES_OPTION) {

            return;
        }

        // =========================
        // DELETE FROM DATABASE
        // =========================

        String sql =
                "DELETE FROM users WHERE id = ?";

        try {

            Connection con =
                    DBConnection.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, userId);

            int result =
                    ps.executeUpdate();

            ps.close();
            con.close();

            if (result > 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "User deleted successfully."
                );

                loadUsers();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "User could not be deleted."
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error deleting user:\n"
                    + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    // =====================================================
    // MAIN
    // =====================================================

    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(() -> {

            new ManageUsersFrame()
                    .setVisible(true);

        });
    }
}
