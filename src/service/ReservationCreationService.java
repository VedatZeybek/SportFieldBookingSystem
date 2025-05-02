package service;


import java.time.*;
import java.time.format.DateTimeFormatter;

import main.ReservationManager;
import modal.Facility;
import modal.Field;
import modal.Reservation;
import modal.SportType;
import modal.User;

public class ReservationCreationService {
    private final ReservationManager reservationManager;
    
    public ReservationCreationService(ReservationManager reservationManager) {
        this.reservationManager = reservationManager;
    }
    
    public void createReservation(User user, Field field, Facility facility, 
                                 SportType sportType, LocalDateTime startTime, 
                                 LocalDateTime endTime, int fee) {
        Reservation reservation = new Reservation(
            user.getUserId(),
            field,
            facility.getName(),
            sportType,
            startTime,
            endTime,
            fee
        );
        
        if (reservationManager.addReservation(reservation))
        {
        	displayReservationConfirmation(field.getName(), startTime, endTime, fee, user.getBalance());
        }
    }
    
    private void displayReservationConfirmation(String fieldName, LocalDateTime startTime, 
                                              LocalDateTime endTime, int fee, int remainingBalance) {
        System.out.println("\n✅ Reservation successful for field: " + fieldName);
        System.out.println("📅 Time Slot: " + startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + 
                         " to " + endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        System.out.println("💳 ₺" + fee + " has been deducted. Remaining Balance: ₺" + remainingBalance);
    }
}