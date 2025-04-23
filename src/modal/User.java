package modal;

import java.util.UUID;

public class User {
	private String userId;
	private String username;
	private String password; // Gerçek uygulamada şifreyi hashlenmiş şekilde saklayın
	private String fullName;
	private String email;
	private String phoneNumber;
	private int balance;

	public User(String username, String password, String fullName, String email, String phoneNumber) {
		this.userId = UUID.randomUUID().toString().substring(0, 8);
		this.username = username;
		this.password = password;
		this.fullName = fullName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.balance = 0; // Başlangıç bakiyesi
	}

	// Getter ve setter metodları
	public String getUserId() { return userId; }
	public String getUsername() { return username; }
	public boolean checkPassword(String password) { return this.password.equals(password); }
	public String getFullName() { return fullName; }
	public void setFullName(String fullName) { this.fullName = fullName; }
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	public String getPhoneNumber() { return phoneNumber; }
	public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
	public int getBalance() { return balance; }

	public void addToBalance(int amount) {
		if (amount > 0) {
			this.balance += amount;
		}
	}

	public boolean deductFromBalance(int amount) {
		if (amount > 0 && this.balance >= amount) {
			this.balance -= amount;
			return true;
		}
		return false;
	}
}