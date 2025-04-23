package service;


import java.util.List;
import java.util.Scanner;

import modal.Field;

public class FieldSelectionService {
    
    public Field selectField(Scanner scanner, List<Field> availableFields) {
        if (availableFields.isEmpty()) {
            System.out.println("No available fields at this facility.");
            return null;
        }

        System.out.println("\nAvailable Fields:");
        for (int k = 0; k < availableFields.size(); k++) {
            Field field = availableFields.get(k);
            System.out.println((k + 1) + ". " + field.getName() +
                               " [Code: " + field.getCode() + "]" +
                               " - Price: ₺" + field.getPrice());
        }

        System.out.print("Select a Field to Reserve: ");
        int fieldChoice = scanner.nextInt();
        if (fieldChoice < 1 || fieldChoice > availableFields.size()) return null;

        return availableFields.get(fieldChoice - 1);
    }
}