package model;

public class Book {

    public int id;
    public String title;
    public String author;
    public String category;
    public int quantity;
    public int available;
    public String bookUrl;

    public Book() {
    }

    public Book(String title, String author, String category,
                int quantity, int available) {

        this.title = title;
        this.author = author;
        this.category = category;
        this.quantity = quantity;
        this.available = available;
    }
}