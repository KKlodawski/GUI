package PROJEKT1;

import java.io.IOException;
import java.util.Random;

public class Kopacz extends Osoba implements Runnable, IPracownik {

    //Vars
    private int iloscMachniecLopata = 0;
    private boolean czyZdolnyDoPracy = true;
    public Kopacz next;
    private final int kopWaitTime = new Random().nextInt(1500);
    private final int maxKopVal = new Random().nextInt(21) + 4;
    private int iloscMachniecLopataBrygady = 0;

    //Constuctors
    public Kopacz(String imie, String nazwisko, int pesel, int nrTelefonu, int waga) throws nieUnikalnyPeselException {
        super(imie, nazwisko, pesel, nrTelefonu, waga);
    }

    //Methods
    public boolean isCzyZdolnyDoPracy() {
        return czyZdolnyDoPracy;
    }

    private void kop() throws zlamanaLopataException {
        if(czyZdolnyDoPracy){
            // Wyrzuć wyjątek złamanej łopaty | 1% szansy na to, że łopata zostanie złamana podczas kopania
            if(new Random().nextInt(100) == 0) {
                throw new zlamanaLopataException(getImie() + ": Łopata była wadliwa i złamała się niespodziewanie w trakcie użytkowania");
            }
            // Wyrzuć wyjątek jeżeli kopacz kopał więcej niż 15 razy
            else if (iloscMachniecLopata >= 15){
                throw new zlamanaLopataException(getImie() + ": Łopata zużyła się i pękła");
            } else {
                iloscMachniecLopataBrygady++;
                iloscMachniecLopata++;
            }
        } else {throw new zlamanaLopataException("Kopacz " + getImie() + " nie jest zdolny do pracy");}
    }

    public void przerwij() {
        czyZdolnyDoPracy = false;
    }

    public void setIloscMachniecLopataBrygady(int iloscMachniecLopataBrygady) {
        this.iloscMachniecLopataBrygady = iloscMachniecLopataBrygady;
    }

    public int getIloscMachniecLopataBrygady() {
        return iloscMachniecLopataBrygady;
    }

    @Override
    public synchronized void run() {
        synchronized (this) {
            // Wątek trwa do momentu gdy nie zostanie przerwany lub bedzie kopał losową ilość razy lub nie bedzie zdolny do pracy
            while (!Thread.currentThread().isInterrupted() && (iloscMachniecLopata <= maxKopVal) && czyZdolnyDoPracy) {
                try {
                    // Jeżeli pozostał jeden pracownik nie czekaj na informacje
                    if(next != this) wait();
                    kop();
                    //Wyświetl kto aktualnie kopie
                    powiedzCoRobisz();
                    /*int time = new Random().nextInt(1500);
                    System.out.println(time/1000 + "sec");*/
                    Thread.sleep(kopWaitTime);

                } catch (zlamanaLopataException e) {
                    przerwij();
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    // W każdym przypadku powiadom następnego pracownika o zakończonej pracy
                    synchronized (next) {
                        next.notify();
                    }
                }
            }
            przerwij();
        }
    }

    public void setNext(Kopacz next) {
        this.next = next;
    }

    @Override
    public int pobierzPensje() {
        return iloscMachniecLopata*12;
    }

    @Override
    public int powiedzIleRazyKopales() {
        return iloscMachniecLopata;
    }

    @Override
    public void powiedzCoRobisz() {
        //System.out.println(Thread.currentThread().getState());
        if(Thread.currentThread().isAlive()) System.out.println("Kopacz " + getImie() + " machnął łopatą!");
        if(Thread.currentThread().getState() == Thread.State.TIMED_WAITING || Thread.currentThread().getState() == Thread.State.WAITING) System.out.println("Czekam!");
        else if(Thread.currentThread().isInterrupted()) System.out.println("Zakończyłem pracę!");
    }

    @Override
    public void zakonczDzialanie() {
        przerwij();
        Thread.currentThread().interrupt();
    }

    @Override
    public void dodajSieDoBrygady(Brygada b) throws IOException, ClassCastException {
        b.dodajPracownika(this);
    }
}

