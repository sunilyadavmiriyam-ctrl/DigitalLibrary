package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import db.DBConnection;
import model.Book;

public class BookDAO {

    // =========================
    // ADD BOOK
    // =========================

    public void addBook(Book book) {

        String sql = "INSERT INTO books(title, author, category, quantity, available) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, book.title);
            ps.setString(2, book.author);
            ps.setString(3, book.category);
            ps.setInt(4, book.quantity);
            ps.setInt(5, book.available);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Book Added Successfully!");
            }

            ps.close();
            con.close();

        } catch (Exception e) {

            System.out.println("Error while adding book!");
            e.printStackTrace();
        }
    }


    // =========================
    // VIEW BOOKS - CONSOLE
    // =========================

    public void viewBooks() {

        String sql = "SELECT * FROM books";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                System.out.println(
                    "ID: " + rs.getInt("id") +
                    " | Title: " + rs.getString("title") +
                    " | Author: " + rs.getString("author") +
                    " | Category: " + rs.getString("category") +
                    " | Quantity: " + rs.getInt("quantity") +
                    " | Available: " + rs.getInt("available")
                );
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {

            System.out.println("Error while viewing books!");
            e.printStackTrace();
        }
    }


    // =========================
    // GET ALL BOOKS - FOR GUI
    // =========================

    public List<Book> getAllBooks() {

        List<Book> books = new ArrayList<>();

        String sql = "SELECT * FROM books";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Book book = new Book();

                book.id = rs.getInt("id");
                book.title = rs.getString("title");
                book.author = rs.getString("author");
                book.category = rs.getString("category");
                book.quantity = rs.getInt("quantity");
                book.available = rs.getInt("available");

                books.add(book);
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {

            System.out.println("Error while getting books!");
            e.printStackTrace();
        }

        return books;
    }


    // =========================
    // SEARCH BOOKS
    // =========================

    public List<Book> searchBooks(String keyword) {

        List<Book> books = new ArrayList<>();

        String sql = "SELECT * FROM books "
                   + "WHERE title LIKE ? "
                   + "OR author LIKE ? "
                   + "OR category LIKE ?";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            String search = "%" + keyword + "%";

            ps.setString(1, search);
            ps.setString(2, search);
            ps.setString(3, search);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Book book = new Book();

                book.id = rs.getInt("id");
                book.title = rs.getString("title");
                book.author = rs.getString("author");
                book.category = rs.getString("category");
                book.quantity = rs.getInt("quantity");
                book.available = rs.getInt("available");

                books.add(book);
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {

            System.out.println("Error while searching books!");
            e.printStackTrace();
        }

        return books;
    }


    // =========================
    // UPDATE BOOK
    // =========================

    public void updateBook(Book book) {

        String sql = "UPDATE books SET title=?, author=?, category=?, "
                   + "quantity=?, available=? WHERE id=?";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, book.title);
            ps.setString(2, book.author);
            ps.setString(3, book.category);
            ps.setInt(4, book.quantity);
            ps.setInt(5, book.available);
            ps.setInt(6, book.id);

            int rows = ps.executeUpdate();

            if (rows > 0) {

                System.out.println("Book Updated Successfully!");

            } else {

                System.out.println("Book ID Not Found!");
            }

            ps.close();
            con.close();

        } catch (Exception e) {

            System.out.println("Error while updating book!");
            e.printStackTrace();
        }
    }


    // =========================
    // DELETE BOOK
    // =========================

    public void deleteBook(int id) {

        String sql = "DELETE FROM books WHERE id=?";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {

                System.out.println("Book Deleted Successfully!");

            } else {

                System.out.println("Book ID Not Found!");
            }

            ps.close();
            con.close();

        } catch (Exception e) {

            System.out.println("Error while deleting book!");
            e.printStackTrace();
        }
    }

}