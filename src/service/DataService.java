package service;

import main.ReservationManager;
import main.SportManager;
import main.UserManager;

public class DataService {
    private final SportManager sportManager;
    private final UserManager userManager;
    private final ReservationManager reservationManager;
    
    public DataService(SportManager sportManager, UserManager userManager, ReservationManager reservationManager) {
        this.sportManager = sportManager;
        this.userManager = userManager;
        this.reservationManager = reservationManager;
    }
    
    public void loadData() {
        // Veri dosyalarını yükleme
        sportManager.loadFieldsFromJson("Sports_Fields_With_Prices.json");
        userManager.loadUsersFromJson("Users.json");
        reservationManager.loadReservationsFromJson("Reservations.json");
    }
    
    public void saveData() {
        // Çıkış yaparken verileri kaydet
        userManager.saveUsersToJson("Users.json");
        reservationManager.saveReservationsToJson("Reservations.json");
    }
}