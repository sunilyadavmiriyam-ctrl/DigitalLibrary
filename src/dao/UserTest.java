package dao;

import java.util.Scanner;

import model.User;

public class UserTest {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        UserDAO dao = new UserDAO();

        while (true) {

            System.out.println("\n============================");
            System.out.println("      DIGITAL LIBRARY");
            System.out.println("============================");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            // REGISTER
            if (choice == 1) {

                System.out.println("\n--- USER REGISTRATION ---");

                System.out.print("Enter Name: ");
                String name = sc.nextLine();

                System.out.print("Enter Email: ");
                String email = sc.nextLine();

                System.out.print("Enter Password: ");
                String password = sc.nextLine();

                User user = new User(
                    name,
                    email,
                    password,
                    "USER"
                );

                dao.registerUser(user);
            }

            // LOGIN
            else if (choice == 2) {

                System.out.println("\n--- USER LOGIN ---");

                System.out.print("Enter Email: ");
                String email = sc.nextLine();

                System.out.print("Enter Password: ");
                String password = sc.nextLine();

                User user = dao.loginUser(email, password);

                if (user != null) {

                    System.out.println("\nLogin Successful!");
                    System.out.println("Welcome, " + user.name);
                    System.out.println("Email: " + user.email);
                    System.out.println("Role: " + user.role);

                } else {

                    System.out.println("\nInvalid Email or Password!");
                }
            }

            // EXIT
            else if (choice == 3) {

                System.out.println("\nThank you for using Digital Library!");
                break;
            }

            else {

                System.out.println("\nInvalid Choice!");
            }
        }

        sc.close();
    }
}