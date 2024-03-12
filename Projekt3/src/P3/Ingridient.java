package P3;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Ingridient {
    private String name;
    public static enum TASTE {
        SWEET, SALTY, BITTER, SOUR, NEUTRAL
    }
    private TASTE taste;

    public Ingridient(String name, TASTE taste) {
        this.name = name;
        this.taste = taste;
    }

    public String getName() {
        return name;
    }

    public TASTE getTaste() {
        return taste;
    }

    @Override
    public String toString() {
        return "Name:" + name + " taste:" + taste;
    }
}
