package dao;

import java.util.Scanner;

import model.Book;

public class BookTest {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        BookDAO dao = new BookDAO();

        while (true) {

            System.out.println("\n===== DIGITAL LIBRARY =====");
            System.out.println("1. Add Book");
            System.out.println("2. View Books");
            System.out.println("3. Update Book");
            System.out.println("4. Delete Book");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            // =========================
            // ADD
            // =========================
            if (choice == 1) {

                System.out.print("Enter Title: ");
                String title = sc.nextLine();

                System.out.print("Enter Author: ");
                String author = sc.nextLine();

                System.out.print("Enter Category: ");
                String category = sc.nextLine();

                System.out.print("Enter Quantity: ");
                int quantity = sc.nextInt();

                Book book = new Book(
                    title,
                    author,
                    category,
                    quantity,
                    quantity
                );

                dao.addBook(book);
            }

            // =========================
            // VIEW
            // =========================
            else if (choice == 2) {

                System.out.println("\n===== BOOK LIST =====");
                dao.viewBooks();
            }

            // =========================
            // UPDATE
            // =========================
            else if (choice == 3) {

                System.out.print("Enter Book ID: ");
                int id = sc.nextInt();
                sc.nextLine();

                System.out.print("Enter New Title: ");
                String title = sc.nextLine();

                System.out.print("Enter New Author: ");
                String author = sc.nextLine();

                System.out.print("Enter New Category: ");
                String category = sc.nextLine();

                System.out.print("Enter New Quantity: ");
                int quantity = sc.nextInt();

                System.out.print("Enter Available Quantity: ");
                int available = sc.nextInt();

                Book book = new Book(
                    title,
                    author,
                    category,
                    quantity,
                    available
                );

                book.id = id;

                dao.updateBook(book);
            }

            // =========================
            // DELETE
            // =========================
            else if (choice == 4) {

                System.out.print("Enter Book ID to Delete: ");
                int id = sc.nextInt();

                dao.deleteBook(id);
            }

            // =========================
            // EXIT
            // =========================
            else if (choice == 5) {

                System.out.println("Thank you!");
                break;
            }

            else {
                System.out.println("Invalid Choice!");
            }
        }

        sc.close();
    }
}