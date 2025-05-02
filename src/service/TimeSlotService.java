package service;


import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import modal.TimeSlot;

public class TimeSlotService {
    
    public TimeSlot selectTimeSlot(Scanner scanner) {
        System.out.println("\nSelect a Date:");
        LocalDate today = LocalDate.now();
        List<LocalDate> allowedDates = new ArrayList<>();
        for (int d = 0; d < 5; d++) {
            LocalDate day = today.plusDays(d);
            allowedDates.add(day);
            System.out.println((d + 1) + ". " + day.format(DateTimeFormatter.ISO_DATE));
        }

        int dateChoice = scanner.nextInt();
        if (dateChoice < 1 || dateChoice > 5) return null;
        LocalDate reservationDate = allowedDates.get(dateChoice - 1);

        System.out.println("\nSelect Hour Slot (Each is 1 hour):");
        int[] slots = {18, 19, 20, 21, 22};
        for (int h = 0; h < slots.length; h++) {
            System.out.println((h + 1) + ". " + slots[h] + ":00 - " + (slots[h] + 1) + ":00");
        }

        int hourChoice = scanner.nextInt();
        if (hourChoice < 1 || hourChoice > slots.length) return null;
        int selectedHour = slots[hourChoice - 1];
        
        LocalDateTime startTime = reservationDate.atTime(selectedHour, 0);
        LocalDateTime endTime = startTime.plusHours(1);
        
        return new TimeSlot(startTime, endTime);
    }
}