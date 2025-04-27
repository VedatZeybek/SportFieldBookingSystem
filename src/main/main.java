package main;

import java.util.Scanner;

import service.AuthService;
import service.BookingService;
import service.DataService;
import service.MenuService;
import service.UserService;

public class main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Model sınıflarını oluştur
        SportManager sportManager = new SportManager();
        UserManager userManager = new UserManager();
        ReservationManager reservationManager = new ReservationManager();
        
        // Servis sınıflarını oluştur
        BookingService bookingService = new BookingService(sportManager, userManager, reservationManager);
        UserService userService = new UserService(userManager);
        AuthService authService = new AuthService(userManager);
        MenuService menuService = new MenuService(userManager, bookingService, userService, authService);
        DataService dataService = new DataService(sportManager, userManager, reservationManager);
        
        // Verileri yükle
        dataService.loadData();
        
        boolean running = true;
        while (running) {
            if (!authService.isUserLoggedIn()) {
                running = authService.showLoginMenu(scanner);
            } else {
                menuService.showMainMenu(scanner);
                if (!authService.isUserLoggedIn()) {
                    continue;
                } else {
                    running = false;
                }
            }
        }
        
        // Verileri kaydet
        dataService.saveData();
        
        System.out.println("Goodbye!");
        scanner.close();
    }
}