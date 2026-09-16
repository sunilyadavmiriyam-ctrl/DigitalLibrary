package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URI;
import java.sql.*;

import db.DBConnection;

public class UserDashboard extends JFrame {

    private int userId;
    private String userName;

    private JTable bookTable;
    private DefaultTableModel tableModel;

    private JTextField searchField;

    // =========================================
    // CONSTRUCTOR
    // =========================================

    public UserDashboard(int userId, String userName) {

        this.userId = userId;
        this.userName = userName;

        setTitle("Digital Library - User Dashboard");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createUI();

        loadBooks();

        AnimationUtils.showWithFade(this);
    }

    // =========================================
    // CREATE USER INTERFACE
    // =========================================

    private void createUI() {

        setLayout(new BorderLayout());

        // =====================================
        // TOP PANEL
        // =====================================

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(
                15, 20, 15, 20
        ));

        JLabel titleLabel = new JLabel(
                "📚 Digital Library"
        );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 28)
        );

        JLabel welcomeLabel = new JLabel(
                "Welcome, " + userName
        );

        welcomeLabel.setFont(
                new Font("Arial", Font.BOLD, 16)
        );

        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(welcomeLabel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);


        // =====================================
        // LEFT MENU
        // =====================================

        JPanel menuPanel = new JPanel();

        menuPanel.setLayout(
                new GridLayout(6, 1, 10, 10)
        );

        menuPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 15, 20, 15
                )
        );

        JButton viewBooksButton =
                new JButton("📚 View Books");

        JButton borrowedButton =
                new JButton("📖 My Borrowed Books");

        JButton refreshButton =
                new JButton("🔄 Refresh");

        JButton searchButton =
                new JButton("🔍 Search");

        JButton borrowBookButton =
                new JButton("📥 Borrow Book");

        JButton returnBookButton =
                new JButton("📤 Return Book");

        JButton openBookButton =
                new JButton("📖 Open Book");

        JButton logoutButton =
                new JButton("🚪 Logout");


        menuPanel.add(viewBooksButton);
        menuPanel.add(borrowedButton);
        menuPanel.add(refreshButton);
        menuPanel.add(searchButton);
        menuPanel.add(borrowBookButton);
        menuPanel.add(returnBookButton);
        menuPanel.add(openBookButton);
        menuPanel.add(logoutButton);

        add(menuPanel, BorderLayout.WEST);


        // =====================================
        // CENTER PANEL
        // =====================================

        JPanel centerPanel = new JPanel(
                new BorderLayout(10, 10)
        );

        centerPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 10, 20, 20
                )
        );


        // =====================================
        // SEARCH PANEL
        // =====================================

        JPanel searchPanel = new JPanel(
                new BorderLayout(10, 10)
        );

        searchField = new JTextField();

        JButton searchBtn =
                new JButton("Search");

        searchPanel.add(
                new JLabel("Search Book: "),
                BorderLayout.WEST
        );

        searchPanel.add(
                searchField,
                BorderLayout.CENTER
        );

        searchPanel.add(
                searchBtn,
                BorderLayout.EAST
        );

        centerPanel.add(
                searchPanel,
                BorderLayout.NORTH
        );


        // =====================================
        // BOOK TABLE
        // =====================================

        tableModel = new DefaultTableModel();

        tableModel.addColumn("ID");
        tableModel.addColumn("Title");
        tableModel.addColumn("Author");
        tableModel.addColumn("Category");
        tableModel.addColumn("Quantity");
        tableModel.addColumn("Available");

        bookTable = new JTable(tableModel);

        bookTable.setRowHeight(30);

        bookTable.setFont(
                new Font("Arial", Font.PLAIN, 14)
        );

        bookTable.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        JScrollPane scrollPane =
                new JScrollPane(bookTable);

        centerPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        add(centerPanel, BorderLayout.CENTER);


        // =====================================
        // BUTTON ACTIONS
        // =====================================

        // View Books
        viewBooksButton.addActionListener(e -> {

            loadBooks();

        });


        // Refresh
        refreshButton.addActionListener(e -> {

            searchField.setText("");

            loadBooks();

        });


        // Search
        searchButton.addActionListener(e -> {

            searchBooks();

        });


        searchBtn.addActionListener(e -> {

            searchBooks();

        });


        // My Borrowed Books
        borrowedButton.addActionListener(e -> {

            new BorrowedBooksFrame(userId);

        });


                // Borrow selected book
                borrowBookButton.addActionListener(e -> borrowBook());


                // Return a borrowed book
                returnBookButton.addActionListener(e -> returnBook());


                // Open selected book details
                openBookButton.addActionListener(e -> openBook());


        // Logout
        logoutButton.addActionListener(e -> {

            int result = JOptionPane.showConfirmDialog(
                    this,
                    "Do you want to logout?",
                    "Logout",
                    JOptionPane.YES_NO_OPTION
            );

            if (result == JOptionPane.YES_OPTION) {

                dispose();

                // Change LoginFrame if your login class
                // has a different name.
                new LoginFrame();

            }

        });
    }


    // =========================================
    // LOAD ALL BOOKS
    // =========================================

    private void loadBooks() {

        tableModel.setRowCount(0);

        String sql =
                "SELECT id, title, author, category, " +
                "quantity, available " +
                "FROM books " +
                "ORDER BY id ASC";

        try {

            Connection con =
                    DBConnection.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ResultSet rs =
                    ps.executeQuery();

            while (rs.next()) {

                tableModel.addRow(
                        new Object[]{

                                rs.getInt("id"),

                                rs.getString("title"),

                                rs.getString("author"),

                                rs.getString("category"),

                                rs.getInt("quantity"),

                                rs.getInt("available")

                        }
                );
            }

            rs.close();
            ps.close();
            con.close();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load books.\n\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }


    // =========================================
    // SEARCH BOOKS
    // =========================================

    private void searchBooks() {

        String keyword =
                searchField.getText().trim();

        if (keyword.isEmpty()) {

            loadBooks();

            return;
        }

        tableModel.setRowCount(0);

        String sql =
                "SELECT id, title, author, category, " +
                "quantity, available " +
                "FROM books " +
                "WHERE title LIKE ? " +
                "OR author LIKE ? " +
                "OR category LIKE ? " +
                "ORDER BY id ASC";

        try {

            Connection con =
                    DBConnection.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(sql);

            String search =
                    "%" + keyword + "%";

            ps.setString(1, search);
            ps.setString(2, search);
            ps.setString(3, search);

            ResultSet rs =
                    ps.executeQuery();

            boolean found = false;

            while (rs.next()) {

                found = true;

                tableModel.addRow(
                        new Object[]{

                                rs.getInt("id"),

                                rs.getString("title"),

                                rs.getString("author"),

                                rs.getString("category"),

                                rs.getInt("quantity"),

                                rs.getInt("available")

                        }
                );
            }

            if (!found) {

                JOptionPane.showMessageDialog(
                        this,
                        "No books found for: "
                                + keyword,
                        "Search Result",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

            rs.close();
            ps.close();
            con.close();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Search Error:\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }


        // =========================================
        // BORROW BOOK
        // =========================================

        private void borrowBook() {

                int selectedRow = bookTable.getSelectedRow();

                if (selectedRow < 0) {
                        JOptionPane.showMessageDialog(
                                        this,
                                        "Select a book to borrow.",
                                        "Borrow Book",
                                        JOptionPane.WARNING_MESSAGE
                        );
                        return;
                }

                int bookId = (int) tableModel.getValueAt(selectedRow, 0);

                String sql =
                                "SELECT available FROM books WHERE id = ? FOR UPDATE";

                try (Connection con = DBConnection.getConnection()) {

                        con.setAutoCommit(false);

                        try (PreparedStatement bookStatement = con.prepareStatement(sql)) {

                                bookStatement.setInt(1, bookId);

                                try (ResultSet rs = bookStatement.executeQuery()) {

                                        if (!rs.next() || rs.getInt("available") <= 0) {
                                                con.rollback();
                                                JOptionPane.showMessageDialog(
                                                                this,
                                                                "This book is currently unavailable.",
                                                                "Borrow Book",
                                                                JOptionPane.WARNING_MESSAGE
                                                );
                                                return;
                                        }
                                }
                        }

                        try (PreparedStatement insertStatement = con.prepareStatement(
                                        "INSERT INTO borrowings "
                                                        + "(user_id, book_id, borrow_date, status) "
                                                        + "VALUES (?, ?, CURDATE(), 'BORROWED')")) {

                                insertStatement.setInt(1, userId);
                                insertStatement.setInt(2, bookId);
                                insertStatement.executeUpdate();
                        }

                        try (PreparedStatement updateStatement = con.prepareStatement(
                                        "UPDATE books SET available = available - 1 WHERE id = ?")) {

                                updateStatement.setInt(1, bookId);
                                updateStatement.executeUpdate();
                        }

                        con.commit();

                        int nextAction = JOptionPane.showOptionDialog(
                                        this,
                                        "Book borrowed successfully. What would you like to do next?",
                                        "Borrow Book",
                                        JOptionPane.DEFAULT_OPTION,
                                        JOptionPane.INFORMATION_MESSAGE,
                                        null,
                                        new String[]{"Open Book", "Return Book", "Close"},
                                        "Open Book"
                        );

                        if (nextAction == 0) {
                                openBook();
                        } else if (nextAction == 1) {
                                returnBook();
                        }

                        loadBooks();

                } catch (SQLException e) {
                        JOptionPane.showMessageDialog(
                                        this,
                                        "Unable to borrow book:\n" + e.getMessage(),
                                        "Database Error",
                                        JOptionPane.ERROR_MESSAGE
                        );
                }
        }


        // =========================================
        // RETURN BOOK
        // =========================================

        private void returnBook() {

                String borrowIdText = JOptionPane.showInputDialog(
                                this,
                                "Enter the Borrow ID from My Borrowed Books:"
                );

                if (borrowIdText == null || borrowIdText.trim().isEmpty()) {
                        return;
                }

                int borrowId;

                try {
                        borrowId = Integer.parseInt(borrowIdText.trim());
                } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(
                                        this,
                                        "Borrow ID must be a number.",
                                        "Return Book",
                                        JOptionPane.WARNING_MESSAGE
                        );
                        return;
                }

                try (Connection con = DBConnection.getConnection()) {

                        con.setAutoCommit(false);

                        int bookId;

                        try (PreparedStatement borrowStatement = con.prepareStatement(
                                        "SELECT book_id FROM borrowings "
                                                        + "WHERE id = ? AND user_id = ? "
                                                        + "AND status = 'BORROWED' FOR UPDATE")) {

                                borrowStatement.setInt(1, borrowId);
                                borrowStatement.setInt(2, userId);

                                try (ResultSet rs = borrowStatement.executeQuery()) {
                                        if (!rs.next()) {
                                                con.rollback();
                                                JOptionPane.showMessageDialog(
                                                                this,
                                                                "Active borrow record not found.",
                                                                "Return Book",
                                                                JOptionPane.WARNING_MESSAGE
                                                );
                                                return;
                                        }
                                        bookId = rs.getInt("book_id");
                                }
                        }

                        try (PreparedStatement returnStatement = con.prepareStatement(
                                        "UPDATE borrowings SET status = 'RETURNED', "
                                                        + "return_date = CURDATE() WHERE id = ?")) {

                                returnStatement.setInt(1, borrowId);
                                returnStatement.executeUpdate();
                        }

                        try (PreparedStatement bookStatement = con.prepareStatement(
                                        "UPDATE books SET available = available + 1 WHERE id = ?")) {

                                bookStatement.setInt(1, bookId);
                                bookStatement.executeUpdate();
                        }

                        con.commit();
                        loadBooks();

                        JOptionPane.showMessageDialog(
                                        this,
                                        "Book returned successfully.",
                                        "Return Book",
                                        JOptionPane.INFORMATION_MESSAGE
                        );

                } catch (SQLException e) {
                        JOptionPane.showMessageDialog(
                                        this,
                                        "Unable to return book:\n" + e.getMessage(),
                                        "Database Error",
                                        JOptionPane.ERROR_MESSAGE
                        );
                }
        }


        // =========================================
        // OPEN BOOK
        // =========================================

        private void openBook() {

                int selectedRow = bookTable.getSelectedRow();

                if (selectedRow < 0) {
                        JOptionPane.showMessageDialog(
                                        this,
                                        "Select a book to open.",
                                        "Open Book",
                                        JOptionPane.WARNING_MESSAGE
                        );
                        return;
                }

                int bookId = (int) tableModel.getValueAt(selectedRow, 0);
                String bookUrl = null;

                try (Connection con = DBConnection.getConnection();
                         PreparedStatement ps = con.prepareStatement(
                                         "SELECT book_url FROM books WHERE id = ?")) {

                        ps.setInt(1, bookId);

                        try (ResultSet rs = ps.executeQuery()) {
                                if (rs.next()) {
                                        bookUrl = rs.getString("book_url");
                                }
                        }
                } catch (SQLException e) {
                        JOptionPane.showMessageDialog(
                                        this,
                                        "Unable to load the book link.\n" + e.getMessage(),
                                        "Open Book",
                                        JOptionPane.ERROR_MESSAGE
                        );
                        return;
                }

                if (bookUrl == null || bookUrl.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(
                                        this,
                                        "No browser link is stored for this book.",
                                        "Open Book",
                                        JOptionPane.WARNING_MESSAGE
                        );
                        return;
                }

                if (!Desktop.isDesktopSupported()
                        || !Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    JOptionPane.showMessageDialog(
                            this,
                            "A web browser is not available on this computer.",
                            "Open Book",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                try {
                    Desktop.getDesktop().browse(new URI(bookUrl));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Unable to open the book link.\n" + e.getMessage(),
                            "Open Book",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
        }


    // =========================================
    // MAIN METHOD
    // =========================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            // Test user
            // Change these values for testing

            new UserDashboard(
                    1,
                    "Sunil"
            );

        });
    }
}