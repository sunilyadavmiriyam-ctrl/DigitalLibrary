package model;

public class User {

    public int id;
    public String name;
    public String email;
    public String password;
    public String role;

    public User() {
    }

    public User(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}