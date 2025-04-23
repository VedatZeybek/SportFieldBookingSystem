package service;

import java.util.Scanner;

import modal.SportType;

public class SportSelectionService {
    
    public SportType selectSport(Scanner scanner) {
        System.out.println("Select a Sport Type:");
        int i = 1;
        for (SportType type : SportType.values()) {
            System.out.println(i + ". " + type);
            i++;
        }
        System.out.println("0. Back to Main Menu");

        int sportChoice = scanner.nextInt();
        if (sportChoice == 0) return null;
        if (sportChoice < 1 || sportChoice > SportType.values().length) return null;

        return SportType.values()[sportChoice - 1];
    }
}