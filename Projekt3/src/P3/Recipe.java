package P3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Recipe {
    //Variables
    private static final int MinIngridients = 2;
    /*private static ArrayList<Ingridient> listOfAllIngridients = new ArrayList<Ingridient>() {
        {
            add(new Ingridient("Sol",Ingridient.TASTE.SALTY));
            add(new Ingridient("Banan", Ingridient.TASTE.SWEET));
            add(new Ingridient("Bekon", Ingridient.TASTE.NEUTRAL));
            add(new Ingridient("Mieso", Ingridient.TASTE.NEUTRAL));
            add(new Ingridient("Brokul", Ingridient.TASTE.BITTER));
            add(new Ingridient("Cytryna", Ingridient.TASTE.SOUR));
            add(new Ingridient("Jagody", Ingridient.TASTE.SWEET));
            add(new Ingridient("Kalmar", Ingridient.TASTE.BITTER));
            add(new Ingridient("Kiwi", Ingridient.TASTE.SOUR));
            add(new Ingridient("Oscypek", Ingridient.TASTE.SALTY));
            add(new Ingridient("Ciasto", Ingridient.TASTE.NEUTRAL));
        }
    };*/
    private static ArrayList<Ingridient> listOfAllIngridients = new ArrayList<>();

    private ArrayList<Ingridient> ingridients;
    private String name;
    private String rating;

    //Contructors
    public Recipe(ArrayList<Ingridient> ingridients, String name) throws IOException {
        if(ingridients.size() < MinIngridients) {
            throw new IOException("Not enough Ingridients to create recipe!");
        } else {
            this.ingridients = ingridients;
            this.name = name;
            generateRating();
        }

    }

    //Methods
    public ArrayList<Ingridient> getIngridients() {
        return ingridients;
    }

    public String getName() {
        return name;
    }

    public String getRating() {
        return rating;
    }

    public void generateRating() {
        Map<Ingridient.TASTE,Integer> ingridientList = new HashMap<>();
        for(Ingridient x : ingridients) {
            if(ingridientList.containsKey(x.getTaste())){
                int value = ingridientList.get(x.getTaste()) + 1;
                ingridientList.replace(x.getTaste(),value);
            } else ingridientList.put(x.getTaste(),1);
        }
        if(ingridientList.size() >= Ingridient.TASTE.values().length) {
            rating = "To danie jest tak złe że aż ciężko na nie patrzeć!";
        } else if(ingridientList.size() >= Ingridient.TASTE.values().length-1) {
            rating = "To danie nie jest zbyt dobre";
        } else if(findMost(ingridientList).equals(Ingridient.TASTE.SWEET)) {
            rating = "To danie jest bardzo słodkie!";
        } else if(findMost(ingridientList).equals(Ingridient.TASTE.SALTY)) {
            rating = "To danie jest bardzo słone!";
        } else if (findMost(ingridientList).equals(Ingridient.TASTE.BITTER)) {
            rating = "To danie jest bardzo gorzkie!";
        } else if (findMost(ingridientList).equals(Ingridient.TASTE.SOUR)) {
            rating = "To danie jest bardzo kwaśne!";
        } else if (findMost(ingridientList).equals(Ingridient.TASTE.NEUTRAL)) {
            rating = "To danie jest neutralne";
        }
    }

    private Ingridient.TASTE findMost(Map<Ingridient.TASTE,Integer> list) {
        Map.Entry<Ingridient.TASTE,Integer> maxEntry = null;
        for(Map.Entry<Ingridient.TASTE,Integer> entry : list.entrySet()) {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
            {
                maxEntry = entry;
            }
        }
        return maxEntry.getKey();
    }

    public void addIngridient(Ingridient ingridient) {
        listOfAllIngridients.add(ingridient);
    }

    public static ArrayList<Ingridient> getListOfAllIngridients() {
        return listOfAllIngridients;
    }

    public static void addToListOfAllIngridients(Ingridient ingridient) {
        listOfAllIngridients.add(ingridient);
    }

    public static Ingridient searchIngidientByName(String name) throws IOException {
        for(Ingridient x : listOfAllIngridients) {
            if (x.getName().equals(name)) return x;
        }
        throw new IOException("Not found!");
    }
}
