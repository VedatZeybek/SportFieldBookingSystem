package service;


import java.util.InputMismatchException;
import java.util.Scanner;

import main.UserManager;
import modal.User;

public class UserService {
    private final UserManager userManager;
    
    public UserService(UserManager userManager) {
        this.userManager = userManager;
    }
    
    public void addBalance(Scanner scanner) {
        User currentUser = userManager.getCurrentUser();

        System.out.println("\n=== ADD BALANCE ===");
        System.out.println("Current Balance: ₺" + currentUser.getBalance());
        System.out.println("\nSelect an amount to add:");
        System.out.println("1. ₺100");
        System.out.println("2. ₺200");
        System.out.println("3. ₺500");
        System.out.println("4. Custom amount");
        System.out.println("0. Back to Main Menu");

        int choice;
        try {
            choice = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.nextLine(); // Hatalı girişi temizle
            return;
        }

        int amount = 0;

        switch (choice) {
            case 1:
                amount = 100;
                break;
            case 2:
                amount = 200;
                break;
            case 3:
                amount = 500;
                break;
            case 4:
                System.out.print("Enter amount to add: ₺");
                try {
                    amount = scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Invalid amount. Please enter a numeric value.");
                    scanner.nextLine(); // Hatalı girişi temizle
                    return;
                }
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid choice. Please select a valid option.");
                return;
        }

        if (amount <= 0) {
            System.out.println("Invalid amount. Please enter a positive number.");
            return;
        }

        if (userManager.addBalance(amount)) {
            System.out.println("Successfully added ₺" + amount + " to your account.");
            System.out.println("New Balance: ₺" + currentUser.getBalance());
        } else {
            System.out.println("Failed to add balance. Please try again.");
        }
    }

    
    public void editProfile(Scanner scanner) {
        User currentUser = userManager.getCurrentUser();
        
        System.out.println("\n=== EDIT PROFILE ===");
        System.out.println("1. Change Full Name (Current: " + currentUser.getFullName() + ")");
        System.out.println("2. Change Email (Current: " + currentUser.getEmail() + ")");
        System.out.println("3. Change Phone Number (Current: " + currentUser.getPhoneNumber() + ")");
        System.out.println("0. Back to Main Menu");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1:
                System.out.print("Enter new full name: ");
                String fullName = scanner.nextLine();
                currentUser.setFullName(fullName);
                System.out.println("Full name updated successfully.");
                break;
            case 2:
                System.out.print("Enter new email: ");
                String email = scanner.nextLine();
                currentUser.setEmail(email);
                System.out.println("Email updated successfully.");
                break;
            case 3:
                System.out.print("Enter new phone number: ");
                String phoneNumber = scanner.nextLine();
                currentUser.setPhoneNumber(phoneNumber);
                System.out.println("Phone number updated successfully.");
                break;
        }
    }
}