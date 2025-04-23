package modal;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Facility {
    private String name;
    private LinkedList<Field> fields;

    public Facility(String name) {
        this.name = name;
        this.fields = new LinkedList<>();
    }

    public void addField(Field field) { fields.add(field); }
    public String getName() { return name; }
    public List<Field> getAvailableFields() {
        List<Field> available = new ArrayList<>();
        for (Field f : fields) if (f.isAvailable()) available.add(f);
        return available;
    }
}
