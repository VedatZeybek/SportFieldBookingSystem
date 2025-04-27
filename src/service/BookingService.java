package service;
import modal.*;

import java.time.LocalDateTime;
import java.util.*;
import main.ReservationManager;
import main.SportManager;
import main.UserManager;

public class BookingService {
    private final SportManager sportManager;
    private final UserManager userManager;
    private final ReservationManager reservationManager;
    private final SportSelectionService sportSelectionService;
    private final FacilitySelectionService facilitySelectionService;
    private final FieldSelectionService fieldSelectionService;
    private final TimeSlotService timeSlotService;
    private final ReservationCreationService reservationCreationService;
    private final ReservationViewService reservationViewService;
    
    public BookingService(SportManager sportManager, UserManager userManager, ReservationManager reservationManager) {
        this.sportManager = sportManager;
        this.userManager = userManager;
        this.reservationManager = reservationManager;
        this.sportSelectionService = new SportSelectionService();
        this.facilitySelectionService = new FacilitySelectionService();
        this.fieldSelectionService = new FieldSelectionService();
        this.timeSlotService = new TimeSlotService();
        this.reservationCreationService = new ReservationCreationService(reservationManager);
        this.reservationViewService = new ReservationViewService(reservationManager);
    }
    
    public void bookField(Scanner scanner) {
        try {
            User currentUser = userManager.getCurrentUser();
            if (currentUser == null) {
                System.out.println("❌ Error: No user logged in. Please log in first.");
                return;
            }
            
            System.out.println("\n=== BOOK A FIELD ===");
            
            // 1. Spor tipini seç
            SportType selectedType = sportSelectionService.selectSport(scanner);
            if (selectedType == null) return;
            
            // 2. Tesis seç
            List<Facility> facilities = sportManager.getFacilities(selectedType);
            if (facilities == null || facilities.isEmpty()) {
                System.out.println("❌ No facilities available for this sport type.");
                return;
            }
            
            Facility selectedFacility = facilitySelectionService.selectFacility(scanner, facilities);
            if (selectedFacility == null) return;
            
            // 3. Saha seç
            List<Field> availableFields = selectedFacility.getAvailableFields();
            if (availableFields == null || availableFields.isEmpty()) {
                System.out.println("❌ No available fields at this facility.");
                return;
            }
            
            Field selectedField = fieldSelectionService.selectField(scanner, availableFields);
            if (selectedField == null) return;
            
            // Seçilen sahanın fiyatını al
            int fieldPrice = selectedField.getPrice();
            if (fieldPrice < 0) {
                System.out.println("❌ Invalid field price. Please contact support.");
                return;
            }
            
            // 4. Bakiye kontrolü
            if (currentUser.getBalance() < fieldPrice) {
                System.out.println("❌ Insufficient balance. Please add funds to your account.");
                return;
            }
            
            // 5. Zaman dilimi seç
            TimeSlot timeSlot = null;
            try {
                timeSlot = timeSlotService.selectTimeSlot(scanner);
            } catch (InputMismatchException e) {
                System.out.println("❌ Invalid time format entered. Please use the correct format.");
                scanner.nextLine(); // Scanner'ı temizle
                return;
            } catch (Exception e) {
                System.out.println("❌ Error selecting time slot: " + e.getMessage());
                return;
            }
            
            if (timeSlot == null) return;
            
            // Geçerli tarih/saat kontrolü
         // LocalDateTime için
            LocalDateTime currentTime = LocalDateTime.now();
            if (timeSlot.getStartTime().isBefore(currentTime)) {
                System.out.println("❌ Cannot book a field in the past. Please select a future time slot.");
                return;
            }
            // 6. Control the collision
            String fieldCode = selectedField.getCode();
            if (fieldCode == null || fieldCode.isEmpty()) {
                System.out.println("❌ Invalid field code. Please contact support.");
                return;
            }
            
            if (!reservationManager.isFieldAvailable(fieldCode, timeSlot.getStartTime(), timeSlot.getEndTime())) {
                System.out.println("❌ This field is already booked for the selected time slot!");
                System.out.println("   Please select another time or field.");
                return;
            }
            
            // 7. Rezervasyon oluştur
            try {
                currentUser.deductFromBalance(fieldPrice);
                reservationCreationService.createReservation(
                    currentUser, 
                    selectedField, 
                    selectedFacility, 
                    selectedType, 
                    timeSlot.getStartTime(), 
                    timeSlot.getEndTime(), 
                    fieldPrice
                );
            } catch (Exception e) {
                System.out.println("❌ Error creating reservation: " + e.getMessage());
                // Hata durumunda bakiyeyi geri yükle
                currentUser.addToBalance(fieldPrice);
            }
        } catch (InputMismatchException e) {
            System.out.println("❌ Invalid input format. Please enter the correct type of data.");
            scanner.nextLine(); // Scanner'ı temizle
        } catch (Exception e) {
            System.out.println("❌ An unexpected error occurred: " + e.getMessage());
        }
    }
    
    public void viewReservations() {
        try {
            User currentUser = userManager.getCurrentUser();
            if (currentUser == null) {
                System.out.println("❌ Error: No user logged in. Please log in first.");
                return;
            }
            reservationViewService.displayUserReservations(currentUser.getUserId());
        } catch (Exception e) {
            System.out.println("❌ Error viewing reservations: " + e.getMessage());
        }
    }
}