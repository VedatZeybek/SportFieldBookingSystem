package main;

import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.Reader;
import com.google.gson.reflect.TypeToken;

import modal.User;

import java.io.Writer;
import java.lang.reflect.Type;
import java.io.FileWriter;
import java.io.IOException;

public class UserManager {
    private Map<String, User> users; // username -> User
    private User currentUser;

    public UserManager() {
        this.users = new HashMap<>();
        this.currentUser = null;
    }

    public boolean registerUser(String username, String password, String fullName, String email, String phoneNumber) {
        if (users.containsKey(username)) {
            return false; // Kullanıcı adı zaten alınmış
        }
        
        User newUser = new User(username, password, fullName, email, phoneNumber);
        users.put(username, newUser);
        return true;
    }

    public boolean login(String username, String password) {
        User user = users.get(username);
        if (user != null && user.checkPassword(password)) {
            currentUser = user;
            return true;
        }
        return false;
    }

    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    // Bakiye yükleme işlemi
    public boolean addBalance(int amount) {
        if (currentUser != null && amount > 0) {
            currentUser.addToBalance(amount);
            return true;
        }
        return false;
    }
    
    // JSON'a kaydetme
    public void saveUsersToJson(String filename) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Writer writer = new FileWriter(filename);
            gson.toJson(users, writer);
            writer.close();
        } catch (IOException e) {
            System.err.println("Error saving users to JSON: " + e.getMessage());
        }
    }
    
    // JSON'dan yükleme
    public void loadUsersFromJson(String filename) {
        try {
            Gson gson = new Gson();
            Reader reader = new FileReader(filename);
            Type type = new TypeToken<Map<String, User>>(){}.getType();
            users = gson.fromJson(reader, type);
            reader.close();
            
            if (users == null) {
                users = new HashMap<>();
            }
        } catch (IOException e) {
            System.err.println("Error loading users from JSON: " + e.getMessage());
            users = new HashMap<>();
        }
    }
}