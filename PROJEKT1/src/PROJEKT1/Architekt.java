package PROJEKT1;

import java.io.IOException;

public class Architekt extends Osoba implements IPracownik {



    //Vars
    public enum Specjalizacja {
        A,B,C,D,E;
    }
    private Specjalizacja specjalizacja;

    //Constructors

    public Architekt(String imie, String nazwisko, int pesel, int nrTelefonu, int waga, Specjalizacja specjalizacja) throws nieUnikalnyPeselException {
        super(imie, nazwisko, pesel, nrTelefonu, waga);
        this.specjalizacja = specjalizacja;
    }


    //Methods
    public Specjalizacja getSpecjalizacja() {
        return specjalizacja;
    }

    @Override
    public String toString() {
        return super.toString() + "\nSpecjalizacja: " + getSpecjalizacja() + "\n";
    }

    @Override
    public int pobierzPensje() {
        switch (specjalizacja) {
            case A -> {return 100;}
            case B -> {return 200;}
            case C -> {return 300;}
            case D -> {return 350;}
            case E -> {return 565;}
            default -> {return 0;}
        }
    }

    @Override
    public int powiedzIleRazyKopales() {
        return 0;
    }

    @Override
    public void powiedzCoRobisz() {
        if(Thread.currentThread().isAlive()) System.out.println("Pracuję!");
        else if(Thread.currentThread().isInterrupted()) System.out.println("Zakończyłem pracę!");
    }

    @Override
    public void zakonczDzialanie() {
        Thread.currentThread().interrupt(); // ??
    }

    @Override
    public void dodajSieDoBrygady(Brygada b) throws IOException, ClassCastException {
        b.dodajPracownika(this);
    }
}
