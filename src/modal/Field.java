package modal;

public class Field {
    private String code;
    private String name;
    private int price;
    private boolean isAvailable;

    public Field(String code, String name, int price) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.isAvailable = true;
    }

    public boolean isAvailable() { return isAvailable; }
    public void reserve() { this.isAvailable = false; }
    public String getCode() { return code; }
    public String getName() { return name; }
    public int getPrice() { return price; }
}
