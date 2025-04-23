package service;

import java.util.List;

import main.ReservationManager;
import modal.Reservation;

public class ReservationViewService {
    private final ReservationManager reservationManager;
    
    public ReservationViewService(ReservationManager reservationManager) {
        this.reservationManager = reservationManager;
    }
    
    public void displayUserReservations(String userId) {
        List<Reservation> userReservations = reservationManager.getUserReservations(userId);
        
        if (userReservations.isEmpty()) {
            System.out.println("You have no reservations.");
            return;
        }
        
        System.out.println("\n=== YOUR RESERVATIONS ===");
        for (int i = 0; i < userReservations.size(); i++) {
            Reservation res = userReservations.get(i);
            System.out.println("\n" + (i + 1) + ". " + res.toString());
        }
    }
}