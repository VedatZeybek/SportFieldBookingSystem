package modal;

public class Field {
    private String code;
    private String name;
    private int price;

    public Field(String code, String name, int price) {
        this.code = code;
        this.name = name;
        this.price = price;
    }

    public String getCode() { 
    	return code; 
    	}
    
    public String getName() { 
    	return name; 
    	}
    
    public int getPrice() { 
    	return price; 
    	}
}
