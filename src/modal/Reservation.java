package modal;

import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Reservation {
    private String reservationId;
    private String userId;
    private String fieldCode;
    private String fieldName;
    private String facilityName;
    private SportType sportType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int fee;
    
    public Reservation(String userId, Field field, String facilityName, SportType sportType, 
                      LocalDateTime startTime, LocalDateTime endTime, int fee) {
        this.reservationId = UUID.randomUUID().toString().substring(0, 8);
        this.userId = userId;
        this.fieldCode = field.getCode();
        this.fieldName = field.getName();
        this.facilityName = facilityName;
        this.sportType = sportType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.fee = fee;
    }
    
    // Getter metodları
    public String getReservationId() { return reservationId; }
    public String getUserId() { return userId; }
    public String getFieldCode() { return fieldCode; }
    public String getFieldName() { return fieldName; }
    public String getFacilityName() { return facilityName; }
    public SportType getSportType() { return sportType; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public int getFee() { return fee; }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return String.format("Reservation #%s\nField: %s at %s\nTime: %s to %s\nFee: ₺%d",
                reservationId, fieldName, facilityName, 
                startTime.format(formatter), endTime.format(formatter), fee);
    }
}