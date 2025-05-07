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

import modal.Field;
import modal.Reservation;

import java.io.Writer;
import java.lang.reflect.Type;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;


public class ReservationManager {
    private Map<String, TreeSet<Reservation>> fieldReservations;

    public ReservationManager() {
        this.fieldReservations = new HashMap<>();
    }

    public boolean addReservation(Reservation reservation) {
        String fieldCode = reservation.getFieldCode();
        fieldReservations.putIfAbsent(fieldCode, new TreeSet<>());
        TreeSet<Reservation> reservations = fieldReservations.get(fieldCode);

        if (isFieldAvailable(fieldCode, reservation.getStartTime(), reservation.getEndTime())) {
            reservations.add(reservation);
            System.out.println("[System] ✅ Reservation added successfully.");
            return true;
        } else {
            System.out.println("[System] ❌ Reservation conflict for field: " 
                + reservation.getFieldName() + " at " 
                + reservation.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            return false;
        }
    }

    public boolean isFieldAvailable(String fieldCode, LocalDateTime startTime, LocalDateTime endTime) {
        TreeSet<Reservation> reservations = fieldReservations.get(fieldCode);
        if (reservations == null) return true;

        // Verimli arama: önceki ve sonraki rezervasyonları kontrol et

        Field dummyField = new Field(fieldCode, "Dummy Field Name", 0); // fieldCode zorunlu, diğeri uydurulabilir
        Reservation dummy = new Reservation("DUMMY", dummyField, "", null, startTime, endTime, 0);
        Reservation floor = reservations.floor(dummy);
        Reservation ceiling = reservations.ceiling(dummy);

        if (floor != null && !floor.getEndTime().isBefore(startTime)) {
            return false;
        }
        if (ceiling != null && !endTime.isBefore(ceiling.getStartTime())) {
            return false;
        }

        return true;
    }

    public List<Reservation> getUserReservations(String userId) {
        List<Reservation> result = new ArrayList<>();
        for (TreeSet<Reservation> set : fieldReservations.values()) {
            for (Reservation r : set) {
                if (r.getUserId().equals(userId)) {
                    result.add(r);
                }
            }
        }
        return result;
    }

    public void saveReservationsToJson(String filename) {
        try (Writer writer = new FileWriter(filename)) {
            Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .setPrettyPrinting()
                .create();

            List<Reservation> allReservations = new ArrayList<>();
            for (TreeSet<Reservation> set : fieldReservations.values()) {
                allReservations.addAll(set);
            }

            gson.toJson(allReservations, writer);
        } catch (IOException e) {
            System.err.println("❌ Error saving reservations: " + e.getMessage());
        }
    }

    public void loadReservationsFromJson(String filename) {
        try {
            File file = new File(filename);
            if (!file.exists()) {
                System.out.println("[System] 📄 No reservations file found.");
                return;
            }

            Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

            try (Reader reader = new FileReader(filename)) {
                Type listType = new TypeToken<List<Reservation>>(){}.getType();
                List<Reservation> reservations = gson.fromJson(reader, listType);
                fieldReservations.clear();
                for (Reservation r : reservations) {
                    addReservation(r); // addReservation zaten çakışmayı kontrol ediyor
                }
                System.out.println("[System] ✅ Reservations loaded successfully.");
            }

        } catch (Exception e) {
            System.err.println("❌ Error loading reservations: " + e.getMessage());
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

