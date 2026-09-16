package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import dao.BookDAO;
import model.Book;

public class SearchBooksFrame extends JFrame {

    private JTextField searchField;
    private JTable table;
    private DefaultTableModel model;

    public SearchBooksFrame() {

        setTitle("Digital Library - Search Books");

        setSize(800, 500);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLocationRelativeTo(null);

        setLayout(new BorderLayout());


        // =========================
        // SEARCH PANEL
        // =========================

        JPanel searchPanel = new JPanel();

        searchPanel.setLayout(new FlowLayout());

        JLabel searchLabel =
                new JLabel("Search Book:");

        searchField =
                new JTextField(25);

        JButton searchButton =
                new JButton("SEARCH");

        searchPanel.add(searchLabel);

        searchPanel.add(searchField);

        searchPanel.add(searchButton);

        add(searchPanel, BorderLayout.NORTH);


        // =========================
        // TABLE
        // =========================

        String[] columns = {
                "ID",
                "Title",
                "Author",
                "Category",
                "Quantity",
                "Available"
        };

        model = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);

        JScrollPane scrollPane =
                new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);


        // =========================
        // SEARCH BUTTON
        // =========================

        searchButton.addActionListener(e -> searchBooks());


        // Press ENTER to search
        searchField.addActionListener(e -> searchBooks());


        setVisible(true);
    }


    // =========================
    // SEARCH METHOD
    // =========================

    private void searchBooks() {

        String keyword =
                searchField.getText().trim();

        // Clear old results
        model.setRowCount(0);

        if (keyword.isEmpty()) {

            return;
        }

        BookDAO dao = new BookDAO();

        List<Book> books =
                dao.searchBooks(keyword);

        for (Book book : books) {

            Object[] row = {

                    book.id,
                    book.title,
                    book.author,
                    book.category,
                    book.quantity,
                    book.available

            };

            model.addRow(row);
        }
    }
}