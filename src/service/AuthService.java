package service;

import java.util.Scanner;
import java.util.regex.Pattern;
import main.*;

public class AuthService {
    private final UserManager userManager;
    
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final String PHONE_REGEX = "^\\+?[0-9]{10,15}$"; 
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    
    public AuthService(UserManager userManager) {
        this.userManager = userManager;
    }
    
    public boolean showLoginMenu(Scanner scanner) {
        System.out.println("\n=== SPORTS FIELD BOOKING SYSTEM ===");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("0. Exit");
        System.out.print("Select an option: ");
        
        String input = scanner.nextLine();
        
        int choice = 0;
        try {
            choice = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return true;
        }
        
        switch (choice) {
            case 1:
                loginUser(scanner);
                return true;
            case 2:
                registerUser(scanner);
                return true;
            case 0:
                return false;
            default:
            	System.out.println("Invalid input. Please enter a number.");
                return true;
        }
    }
    
    public void loginUser(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        
        if (userManager.login(username, password)) {
            System.out.println("Login successful! Welcome, " + userManager.getCurrentUser().getFullName());
        } else {
            System.out.println("Invalid username or password. Please try again.");
        }
    }
    
    public void registerUser(Scanner scanner) {
        System.out.println("\n=== USER REGISTRATION ===");
        
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        
        String password;
        while (true) {
            System.out.print("Enter password (min 8 chars, must contain uppercase, lowercase, number and special char): ");
            password = scanner.nextLine();
            
            if (isValidPassword(password)) {
                System.out.print("Confirm password: ");
                String confirmPassword = scanner.nextLine();
                
                if (password.equals(confirmPassword)) {
                    break;
                } else {
                    System.out.println("Passwords do not match. Please try again.");
                }
            } else {
                System.out.println("Invalid password format! Password must contain at least:");
                System.out.println(" - 8 characters");
                System.out.println(" - 1 uppercase letter");
                System.out.println(" - 1 lowercase letter");
                System.out.println(" - 1 number");
                System.out.println(" - 1 special character (@#$%^&+=)");
            }
        }
        
        System.out.print("Enter full name: ");
        String fullName = scanner.nextLine();
        
        String email;
        while (true) {
            System.out.print("Enter email (example@domain.com): ");
            email = scanner.nextLine();
            
            if (isValidEmail(email)) {
                break;
            } else {
                System.out.println("Invalid email format! Please enter a valid email address.");
            }
        }
        
        String phoneNumber;
        while (true) {
            System.out.print("Enter phone number (10-15 digits, optional + prefix): ");
            phoneNumber = scanner.nextLine();
            
            if (isValidPhoneNumber(phoneNumber)) {
                break;
            } else {
                System.out.println("Invalid phone number format! Please enter a valid phone number.");
            }
        }
        
        if (userManager.registerUser(username, password, fullName, email, phoneNumber)) {
            System.out.println("Registration successful! Please login.");
        } else {
            System.out.println("Username already exists. Please choose another one.");
        }
    }
    
    private boolean isValidEmail(String email) {
        return Pattern.compile(EMAIL_REGEX).matcher(email).matches();
    }
    
    private boolean isValidPhoneNumber(String phoneNumber) {
        return Pattern.compile(PHONE_REGEX).matcher(phoneNumber).matches();
    }
    
    private boolean isValidPassword(String password) {
        return Pattern.compile(PASSWORD_REGEX).matcher(password).matches();
    }
    
    public void logout() {
        userManager.logout();
        System.out.println("Logged out successfully.");
    }
    
    public boolean isUserLoggedIn() {
        return userManager.isLoggedIn();
    }
}