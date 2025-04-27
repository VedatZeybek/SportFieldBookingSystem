package service;

import java.util.Scanner;

import main.UserManager;
import modal.User;

public class MenuService {
    private final UserManager userManager;
    private final BookingService bookingService;
    private final UserService userService;
    private final AuthService authService;

    public MenuService(UserManager userManager, BookingService bookingService,
                       UserService userService, AuthService authService) {
        this.userManager = userManager;
        this.bookingService = bookingService;
        this.userService = userService;
        this.authService = authService;
    }

    public void showMainMenu(Scanner scanner) {
        User currentUser = userManager.getCurrentUser();
        System.out.println("\n=== SPORTS FIELD BOOKING SYSTEM ===");
        System.out.println("Welcome, " + currentUser.getFullName());
        System.out.println("Current Balance: ₺" + currentUser.getBalance());

        while (true) {
            System.out.println("\n1. Book a Field");
            System.out.println("2. View My Reservations");
            System.out.println("3. Add Balance");
            System.out.println("4. Edit Profile");
            System.out.println("9. Logout");
            System.out.println("0. Exit");
            System.out.print("Select an option: ");

            String input = scanner.nextLine();
            int choice;

            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue ; // Menüye dön
            }

            switch (choice) {
                case 1:
                    bookingService.bookField(scanner);
                    break;
                case 2:
                    bookingService.viewReservations();
                    break;
                case 3:
                    userService.addBalance(scanner);
                    break;
                case 4:
                    userService.editProfile(scanner);
                    break;
                case 9:
                    authService.logout();
                    return; // Menüden çık
                case 0:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Please choose a valid menu item.");
            }
        }
    }
}
