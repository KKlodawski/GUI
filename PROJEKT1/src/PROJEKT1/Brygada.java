package PROJEKT1;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class Brygada {

    //Vars
    private Brygardzista brygardzista;
    protected Set<Osoba> kopacze = new HashSet<>();
    final private int maksymalnaIloscPracownikow;
    private int iloscMachniecLopataBrygady = 0;
    private int ilArchitektow = 0;
    private int ilKopaczy = 0;

    //Constructors
    public Brygada(int maksymalnaIloscPracownikow) {
        this.maksymalnaIloscPracownikow = maksymalnaIloscPracownikow;
    }

    //Methods
    public void setBrygardzista(Brygardzista brygardzista) {
        this.brygardzista = brygardzista;
    }

    public boolean czyPelnaBrygada(){
        if(kopacze.size() >= maksymalnaIloscPracownikow) return true;
        else return false;
    }

    public void dodajPracownika(Osoba pracownik) throws IOException, ClassCastException {
        //Sprawdza czy nie próbujemy dodać brygadzisty jako pracownika i wyrzuca stosowny wyjątek
        if(pracownik.getClass() == Brygardzista.class) throw new ClassCastException("Invalid Osoba!");
        //Sprawdza czy brygada nie jest pełna w przciwnym wypadku wyrzuca stosowny wyjątek
        else if(!czyPelnaBrygada()) {
            // Dodaje osobę do brygady oraz zwiększa liczniki odpowiadające za ilość danego typu pracownika w brygadzie
            if(pracownik.getClass() == Architekt.class) ilArchitektow++;
            if(pracownik.getClass() == Kopacz.class) ilKopaczy++;
            kopacze.add(pracownik);
        } else throw new IOException("Brygada jest pełna!");
    }

    public int getileArchitektów(){
        return ilArchitektow;
    }

    public int getIloscMachniecLopataBrygady() {
        return iloscMachniecLopataBrygady;
    }

    public void setIloscMachniecLopataBrygady(int iloscMachniecLopataBrygady) {
        this.iloscMachniecLopataBrygady = iloscMachniecLopataBrygady;
    }

    public int getileKopaczy() {return ilKopaczy;}

    public Set<Osoba> getKopacze() {
        return kopacze;
    }

    public void showArchitekrow() {
        getKopacze()
                .stream()
                .filter(s -> s instanceof Architekt)
                .map(s -> (Architekt ) s)
                .map(s -> s.getImie() + " " + s.getNazwisko() + "\n")
                .forEach(System.out::print);
    }

    public void showIloscKopniecPracownikow() {
        getKopacze()
                .stream()
                .filter(s -> s instanceof Kopacz)
                .map(s -> (Kopacz ) s)
                .map(s -> s.getImie() + " " + s.getNazwisko() + " kopnął: " + s.powiedzIleRazyKopales() + " razy! \n")
                .forEach(System.out::print);
    }

    public void dodajPracownikow(Collection<Osoba> pracownicy) throws IOException, ClassCastException {
        // Sprawdza czy brygada jest pełna i wyrzuca stosowny wyjątek
        if(czyPelnaBrygada()) throw new IOException("Brygada jest pełna");
        // Sprawdza czy jest wystarczająca ilość miejsc w brygadzie by dodać wszystkie osoby i wyrzuca stosowny wyjątek
        else if(pracownicy.size() > maksymalnaIloscPracownikow+kopacze.size()) throw new IOException("Nie wystarczająca ilość miejsc w brygadzie dla " + pracownicy.size() + " osób");
        else {
            // Sprawdza czy nie próbujemy dodać brygadzisty jako pracownika i wyrzuca stosowny wyjątek
            for (Osoba x : pracownicy) {
                if (x.getClass() == Brygardzista.class) throw new ClassCastException("Invalid Osoba");
            }
            // Dodaje listę osób do brygady oraz zwiększa liczniki odpowiadające za ilość danego typu pracownika w brygadzie
            for (Osoba x : pracownicy) {
                if (x.getClass() == Architekt.class) ilArchitektow++;
                if (x.getClass() == Kopacz.class) ilKopaczy++;
                kopacze.add(x);
            }
        }
    }

}
