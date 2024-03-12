package PROJEKT1;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public abstract class Osoba implements IPracownik, Comparable<Osoba>{

    //Vars
    protected String imie, nazwisko;
    protected int nrTelefonu, waga, pesel;
    protected static Set<Integer> peselSet = new HashSet<>(); //Set przechowujący numery pesel wszystkich obiektów

    //Constructors
    public Osoba(String imie, String nazwisko, int pesel, int nrTelefonu, int waga) throws nieUnikalnyPeselException{

        //Sprawdza czy osoba z danym peselem już istnieje i wyrzuca nieUnikalnyPeselException
        if(peselSet.contains(pesel)) throw new nieUnikalnyPeselException();
        else {
            peselSet.add(pesel);
            this.pesel = pesel;
        }
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.nrTelefonu = nrTelefonu;
        this.waga = waga;
    }


    //Methods
    public String getImie() {
        return imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public int getNrTelefonu() {
        return nrTelefonu;
    }

    public int getWaga() {
        return waga;
    }

    public int getPesel() {
        return pesel;
    }

    @Override
    public String toString() {
        return "Imie: " + getImie() + "\nNazwisko: " + getNazwisko() + "\nNr_Telefonu: " + getNrTelefonu() + "\nWaga: " + getWaga() + "\nPesel: " + getPesel();
    }

    @Override
    public int compareTo(Osoba o) {
        if(this.pobierzPensje() < o.pobierzPensje()) return 1;
        else if(this.pobierzPensje() > o.pobierzPensje()) return -1;
        else return 0;
    }
}
