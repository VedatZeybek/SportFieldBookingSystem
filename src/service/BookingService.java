package service;

import modal.*;

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
        User currentUser = userManager.getCurrentUser();
        
        System.out.println("\n=== BOOK A FIELD ===");
        
        // 1. Spor tipini seç
        SportType selectedType = sportSelectionService.selectSport(scanner);
        if (selectedType == null) return;
        
        // 2. Tesis seç
        List<Facility> facilities = sportManager.getFacilities(selectedType);
        Facility selectedFacility = facilitySelectionService.selectFacility(scanner, facilities);
        if (selectedFacility == null) return;
        
        // 3. Saha seç
        List<Field> availableFields = selectedFacility.getAvailableFields();
        Field selectedField = fieldSelectionService.selectField(scanner, availableFields);
        if (selectedField == null) return;
        
        // Seçilen sahanın fiyatını al
        int fieldPrice = selectedField.getPrice();
        
        // 4. Bakiye kontrolü
        if (currentUser.getBalance() < fieldPrice) {
            System.out.println("Insufficient balance. Please add funds to your account.");
            return;
        }
        
        // 5. Zaman dilimi seç
        TimeSlot timeSlot = timeSlotService.selectTimeSlot(scanner);
        if (timeSlot == null) return;
        
        // 6. Control the collision
        String fieldCode = selectedField.getCode();
        if (!reservationManager.isFieldAvailable(fieldCode, timeSlot.getStartTime(), timeSlot.getEndTime())) {
            System.out.println("❌ This field is already booked for the selected time slot!");
            System.out.println("   Please select another time or field.");
            return; // veya başka bir işleme yönlendirin
        }
        
        
        // 7. Rezervasyon oluştur
        selectedField.reserve();
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
    }
    
    public void viewReservations() {
        User currentUser = userManager.getCurrentUser();
        reservationViewService.displayUserReservations(currentUser.getUserId());
    }
}