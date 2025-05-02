package main;


import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.FileReader;
import java.io.Reader;
import com.google.gson.reflect.TypeToken;

import modal.Reservation;

import java.io.Writer;
import java.lang.reflect.Type;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

public class ReservationManager {
    private Set<Reservation> reservations;

    public ReservationManager() {
        this.reservations = new TreeSet<>();
    }
    
    public boolean addReservation(Reservation reservation) {
        if (isFieldAvailable(reservation.getFieldCode(), reservation.getStartTime(), reservation.getEndTime())) {
            reservations.add(reservation);
            System.out.println("[System] ✅ Reservation added successfully.");
            return true;
        } else {
            System.out.println("[System] ❌ Reservation conflicts with an existing booking for field: " 
                + reservation.getFieldName() + " at time: " 
                + reservation.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            return false;
        }
    }
    

    public boolean isFieldAvailable(String fieldCode, LocalDateTime startTime, LocalDateTime endTime) {
        for (Reservation reservation : reservations) {
            if (reservation.getFieldCode().equals(fieldCode)) {
                boolean overlap = !(endTime.isBefore(reservation.getStartTime()) || 
                                   startTime.isAfter(reservation.getEndTime()));
                if (overlap) {
                    return false;
                }
            }
        }
        return true;
    }

    public List<Reservation> getUserReservations(String userId) {
        List<Reservation> userReservations = new ArrayList<>();
        for (Reservation r : reservations) {
            if (r.getUserId().equals(userId)) {
                userReservations.add(r);
            }
        }
        return userReservations;
    }

    public void saveReservationsToJson(String filename) {
        try {
            Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .setPrettyPrinting()
                .create();
            Writer writer = new FileWriter(filename);
            gson.toJson(new ArrayList<>(reservations), writer);
            writer.close();
        } catch (IOException e) {
            System.err.println("❌ Error saving reservations to JSON: " + e.getMessage());
            System.out.println("[System] ❌ Unable to save reservations. Please check if the file path is correct or writable.");
        }
    }

    public void loadReservationsFromJson(String filename) {
        try {
            File file = new File(filename);
            if (!file.exists()) {
                System.out.println("[System] 📄 Reservations file not found. A new one will be created upon save.");
                reservations = new TreeSet<>();
                return;
            }

            Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
            Reader reader = new FileReader(filename);
            Type type = new TypeToken<List<Reservation>>(){}.getType();
            List<Reservation> list = gson.fromJson(reader, type);
            reader.close();

            this.reservations = new TreeSet<>();
            if (list != null) {
                for (Reservation reservation : list) {
                    if (isFieldAvailable(reservation.getFieldCode(), reservation.getStartTime(), reservation.getEndTime())) {
                        this.reservations.add(reservation);
                    } else {
                        System.out.println("[System] ⚠️ Skipped reservation #" + reservation.getReservationId() 
                            + " due to conflict for field: " + reservation.getFieldName());
                    }
                }
            }
            System.out.println("[System] ✅ Reservations loaded successfully.");
        } catch (IOException e) {
            System.err.println("❌ Error loading reservations from JSON: " + e.getMessage());
            System.out.println("[System] ❌ Failed to load reservations. Please ensure '" + filename + "' exists and is readable.");
            this.reservations = new TreeSet<>();
        } catch (Exception e) {
            System.err.println("❌ Unexpected error: " + e.getMessage());
            System.out.println("[System] ❌ Data format error. Please make sure the JSON content is valid.");
            this.reservations = new TreeSet<>();
        }
    }
}


class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
    @Override
    public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    @Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        return LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
   }


}

