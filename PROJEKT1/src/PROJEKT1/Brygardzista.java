package PROJEKT1;

import java.io.IOException;
import java.util.ArrayList;

public class Brygardzista extends Kopacz implements Runnable, IPracownik {

    //Vars
    private String pseudonim;
    private final int dlugoscZmiany;
    private Brygada brygada;

    //Constructors
    public Brygardzista(String imie, String nazwisko, int pesel, int nrTelefonu, int waga, String pseudonim, int dlugoscZmiany) throws nieUnikalnyPeselException {
        super(imie, nazwisko, pesel, nrTelefonu, waga);
        this.pseudonim = pseudonim;
        this.dlugoscZmiany = dlugoscZmiany;

    }

    //Methods
    public void setBrygada(Brygada brygada) {
        this.brygada = brygada;
        this.brygada.setBrygardzista(this);
    }

    private boolean sprawdzCzyBrygadaNiezdolnaDoPracy() {
        int tmp = 0;
        for (Osoba x : brygada.kopacze) {
            if(x.getClass() == Kopacz.class) {
                if(!((Kopacz) x).isCzyZdolnyDoPracy()) tmp++;
            }
        }
        if(tmp==brygada.getileKopaczy()) return true;
        else return false;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                //Lista pracowników którzy są kopaczami
                ArrayList<Kopacz> next = wykl.wykl(brygada);
                Osoba first = null;

               // Przypisuje array kopaczy i podaje każdemu osobę która bedzie kopała po niej
                for (Osoba x : brygada.getKopacze())  if (x.getClass() == Kopacz.class) next.add(((Kopacz) x));
                for(int i = 0 ; i < next.size(); i++){
                        if(first == null) first = next.get(0);
                        if (i == 0) next.get(i).setNext(next.get(next.size()-1));
                        else {
                            next.get(i).setNext(next.get(i - 1));
                        }

                }
                //Zaczyna wątki każdego kopacza w brygadzie
                for (Kopacz x : next) new Thread(x).start();
                synchronized (first) {
                    first.notify();
                }


                // Pętla odpowiada za długość zmiany i wyrzuca odpowiedni wyjątek po zakończeniu jej
                for (int i = 0; i <= dlugoscZmiany*100; i++) {
                    for(int j = 0; j < next.size(); j++) {//System.out.println(next.get(j).isCzyZdolnyDoPracy());
                        if(!next.get(j).isCzyZdolnyDoPracy()) {
                            /*System.out.println(j);
                            System.out.println(next.size());
                            System.out.println("===Pre===");
                            System.out.println(next.get(j).imie);
                            System.out.println(next.get(j).next.imie);*/
                            Kopacz tmp = next.get(j).next;
                            //System.out.println("===TMP : " + tmp.imie + " ===");
                            if(next.size()-1 == j) {
                                /*System.out.println("===POST===");
                                System.out.println(next.get(0).imie);
                                System.out.println(next.get(0).next.imie);*/
                                next.get(0).next = tmp;
                                /*System.out.println("===POST-INIC===");
                                System.out.println(next.get(0).imie);
                                System.out.println(next.get(0).next.imie);
                                System.out.println("hi1");*/
                            }
                            else {
                                /*System.out.println("===POST===");
                                System.out.println(next.get(j+1).imie);
                                System.out.println(next.get(j+1).next.imie);*/
                                next.get(j + 1).next = tmp;
                                /*System.out.println("===POST-INIC===");
                                System.out.println(next.get(j+1).imie);
                                System.out.println(next.get(j+1).next.imie);
                                System.out.println("Hi2");*/
                            }
                            /*synchronized(next.get(j)) {
                                next.get(j).notify();
                            }*/
                            next.remove(j);
                            //System.out.println("X");
                        }
                    }
                    //Jeżeli brygada będzie nie zdolna do pracy przed zakończeniem zmiany wyrzuca stosowny wyjątek
                    if (sprawdzCzyBrygadaNiezdolnaDoPracy()) {
                        throw new BrygadaNieZdolnaDoPracy("Wszyscy kopacze w tej brygadzie są niezdolni do pracy!");
                    }
                    updateil();
                    Thread.sleep(10);
                }
                throw new BrygadaNieZdolnaDoPracy("Koniec Zmiany!");


            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrygadaNieZdolnaDoPracy brygadaNieZdolnaDoPracy) {
                // Pętla przerywa praę wszystkich pracowników
                for (Osoba x : brygada.getKopacze()) {
                    if (x.getClass() == Kopacz.class) ((Kopacz) x).zakonczDzialanie();
                }
                // W przypadku brygady nie zdolnej do pracy zakończ wątek
                Thread.currentThread().interrupt();
                brygadaNieZdolnaDoPracy.printStackTrace();
            }
        }
    }

    private void updateil() {
        brygada.setIloscMachniecLopataBrygady(0);
        int tmp = brygada.getKopacze()
                .stream()
                .filter(s -> s instanceof Kopacz)
                .map(s -> (Kopacz) s)
                .mapToInt(Kopacz::getIloscMachniecLopataBrygady).sum();
        brygada.setIloscMachniecLopataBrygady(tmp);
    }

    public String getPseudonim() {
        return pseudonim;
    }

    public int getilMachniecLopataBrygady() {
        updateil();
        return brygada.getIloscMachniecLopataBrygady();
    }

    public int getDlugoscZmiany() {
        return dlugoscZmiany;
    }

    public Brygada getBrygada() {
        return brygada;
    }

    private WyklKopacz wykl =  (Brygada z) -> {
        ArrayList<Kopacz> osb = new ArrayList<>();
        for (Osoba x : z.getKopacze())  if (x.getClass() == Kopacz.class) osb.add(((Kopacz) x));
        return osb;
    } ;

    @Override
    public int pobierzPensje() {
        return brygada.getIloscMachniecLopataBrygady()*8;
    }

    @Override
    public int powiedzIleRazyKopales() {
        return brygada.getIloscMachniecLopataBrygady();
    }

    @Override
    public void powiedzCoRobisz() {
        if(Thread.currentThread().isAlive()) System.out.println("Nadzoruje!");
        else System.out.println("Zakończyłem pracę!");
    }

    @Override
    public void zakonczDzialanie() {
        Thread.currentThread().interrupt();
    }

    @Override
    public void dodajSieDoBrygady(Brygada b) throws IOException, ClassCastException {
        b.dodajPracownika(this);
        //?
        //setBrygada(b);
    }

}
