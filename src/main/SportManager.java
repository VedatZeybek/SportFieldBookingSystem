package main;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import modal.Facility;
import modal.Field;
import modal.SportType;

public class SportManager {
    private Map<SportType, List<Facility>> sportFacilities;

    public SportManager() {
        sportFacilities = new HashMap<>();
        for (SportType type : SportType.values()) {
            sportFacilities.put(type, new ArrayList<>());
        }
    }

    public List<Facility> getFacilities(SportType type) {
        return sportFacilities.get(type);
    }

    public void loadFieldsFromJson(String filename) {
        try {
            Gson gson = new Gson();
            Reader reader = new FileReader(filename);
            List<Map<String, Object>> records = gson.fromJson(reader, new TypeToken<List<Map<String, Object>>>() {}.getType());
            reader.close();

            for (Map<String, Object> record : records) {
                String sportType = (String) record.get("sport_type");
                String fieldName = (String) record.get("original_field");
                String facilityName = (String) record.get("facility_name");
                int price = ((Number) record.get("price")).intValue();
                String generatedCode = UUID.randomUUID().toString().substring(0, 6).toUpperCase();

                SportType type = SportType.fromString(sportType);
                Facility existing = null;
                for (Facility f : sportFacilities.get(type)) {
                    if (f.getName().equals(facilityName)) {
                        existing = f;
                        break;
                    }
                }
                if (existing == null) {
                    existing = new Facility(facilityName);
                    sportFacilities.get(type).add(existing);
                }
                existing.addField(new Field(generatedCode, fieldName, price));
            }
        } catch (Exception e) {
            System.err.println("Error loading JSON: " + e.getMessage());
}
}
}
