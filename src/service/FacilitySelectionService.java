package service;


import java.util.List;
import java.util.Scanner;

import modal.Facility;

public class FacilitySelectionService {
    
    public Facility selectFacility(Scanner scanner, List<Facility> facilities) {
        if (facilities.isEmpty()) {
            System.out.println("No facilities available for this sport type.");
            return null;
        }

        System.out.println("\nAvailable Facilities:");
        for (int j = 0; j < facilities.size(); j++) {
            System.out.println((j + 1) + ". " + facilities.get(j).getName());
        }

        System.out.print("Select a Facility: ");
        int facilityChoice = scanner.nextInt();
        if (facilityChoice < 1 || facilityChoice > facilities.size()) return null;

        return facilities.get(facilityChoice - 1);
    }
}