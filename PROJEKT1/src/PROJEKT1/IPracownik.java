package PROJEKT1;

import java.io.IOException;

public interface IPracownik {

    int pobierzPensje();
    int powiedzIleRazyKopales();
    void powiedzCoRobisz();
    void zakonczDzialanie();
    void dodajSieDoBrygady(Brygada b) throws IOException;

}
