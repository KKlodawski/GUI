package PROJEKT1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("start\n");


        try {


            // Architekci
            Architekt a1 = new Architekt("1","Arch",11,123123123,64, Architekt.Specjalizacja.A);
            Architekt a2 = new Architekt("2","Arche",12,321321321,76, Architekt.Specjalizacja.B);
            Architekt a3 = new Architekt("3","Archi",13,213213213,72, Architekt.Specjalizacja.E);
            Architekt a4 = new Architekt("4","Archu",14,312312312,86, Architekt.Specjalizacja.D);
            Architekt a5 = new Architekt("5","Archw",15,231231231,91, Architekt.Specjalizacja.A);
            Architekt a6 = new Architekt("6","Archo",1231,123,79, Architekt.Specjalizacja.C);

            //Brygadziści
            Brygardzista b1 = new Brygardzista("1","Bryg",21,456456456,111,"Pulpet",30);
            Brygardzista b2 = new Brygardzista("2","Bryge",22,465465465,108,"Mister",40);
            Brygardzista b3 = new Brygardzista("3","Brygu",23,546546546,89,"Aro",50);

            //Kopacze
            Kopacz k1 = new Kopacz("1","Kop",31,911111111,70);
            Kopacz k2 = new Kopacz("2","Kopi",32,912121212,123);
            Kopacz k3 = new Kopacz("3","Kope",33,913131313,141);
            Kopacz k4 = new Kopacz("4","Kopu",34,914141414,96);
            Kopacz k5 = new Kopacz("5","Kopa",35,915151515,106);
            Kopacz k6 = new Kopacz("6","Kopz",36,916161616,94);
            Kopacz k7 = new Kopacz("7","Kopc",37,917171717,86);
            Kopacz k8 = new Kopacz("8","Kopm",38,918181818,74);
            Kopacz k9 = new Kopacz("9","Kopo",39,919191919,89);
            Kopacz k10 = new Kopacz("10","Koper",310,991919191,74);
            Kopacz k11 = new Kopacz("11","Kopec",311,981818181,81);
            Kopacz k12 = new Kopacz("12","Kopet",312,971717171,65);
            Kopacz k13 = new Kopacz("13","Kopez",313,961616161,78);
            Kopacz k14 = new Kopacz("14","Kopes",314,951515151,90);

            //Brygady
            Brygada brygadaPierwsza = new Brygada(8);
            Brygada brygadaDruga = new Brygada(5);
            Brygada brygadaTrzecia = new Brygada(7);

            //Kwalifikacja do brygad
            // Pierwsza
            ArrayList<Osoba> kwalifikacja1 = new ArrayList<>();
            kwalifikacja1.add(a1);
            kwalifikacja1.add(a2);
            kwalifikacja1.add(a3);
            kwalifikacja1.add(k1);
            kwalifikacja1.add(k2);
            kwalifikacja1.add(k3);
            kwalifikacja1.add(k4);
            kwalifikacja1.add(k5);
            brygadaPierwsza.dodajPracownikow(kwalifikacja1);
            b1.setBrygada(brygadaPierwsza);
            //Druga
            brygadaDruga.dodajPracownika(a4);
            brygadaDruga.dodajPracownika(k6);
            brygadaDruga.dodajPracownika(k7);
            brygadaDruga.dodajPracownika(k8);
            brygadaDruga.dodajPracownika(k9);
            b2.setBrygada(brygadaDruga);
            //Trzecia
            ArrayList<Osoba> kwalifikacja3 = new ArrayList<>();
            kwalifikacja3.add(a5);
            kwalifikacja3.add(a6);
            kwalifikacja3.add(k10);
            kwalifikacja3.add(k11);
            kwalifikacja3.add(k12);
            kwalifikacja3.add(k13);
            kwalifikacja3.add(k14);
            brygadaTrzecia.dodajPracownikow(kwalifikacja3);
            b3.setBrygada(brygadaTrzecia);

            // Rozpoczęcie prac
            Thread p1 = new Thread(b1);
            Thread p2 = new Thread(b2);
            Thread p3 = new Thread(b3);
            Thread.sleep(2000);
            p1.start();
            Thread.sleep(2000);
            p2.start();
            Thread.sleep(2000);
            p3.start();



            //Podsumowania
            do {
                    if (p1.isInterrupted()) {
                        System.out.println("Podsumowanie brygady pierwszej");
                        System.out.println("Brygadzista " + b1.getImie() + " " + b1.getNazwisko() + " " + b1.getPseudonim());
                        b1.getBrygada().showArchitekrow();
                        b1.getBrygada().showIloscKopniecPracownikow();
                        System.out.println("Ilość kopnięć brygady: " + b1.getBrygada().getIloscMachniecLopataBrygady());

                        System.out.println(" === Zarobki ===");
                        ArrayList<Osoba> zar = new ArrayList<>();
                        zar.add(b1);
                        zar.addAll(b1.getBrygada().getKopacze());
                        Collections.sort(zar);
                        for (Osoba x : zar) {
                            System.out.println(x.getImie() + " " + x.getNazwisko() + " " + x.pobierzPensje());
                        }
                    }
            } while(p1.isAlive());
             do {
                    if (p2.isInterrupted()) {
                        System.out.println("Podsumowanie brygady drugiej");
                        System.out.println("Brygadzista " + b2.getImie() + " " + b2.getNazwisko() + " " + b2.getPseudonim());
                        b2.getBrygada().showArchitekrow();
                        b2.getBrygada().showIloscKopniecPracownikow();
                        System.out.println("Ilość kopnięć brygady: " + b2.getBrygada().getIloscMachniecLopataBrygady());

                        System.out.println(" === Zarobki ===");
                        ArrayList<Osoba> zar = new ArrayList<>();
                        zar.addAll(b2.getBrygada().getKopacze());
                        zar.add(b2);
                        Collections.sort(zar);
                        for (Osoba x : zar) {
                            System.out.println(x.getImie() + " " + x.getNazwisko() + " " + x.pobierzPensje());
                        }
                }
             } while(p2.isAlive());
            do {
                    if (p3.isInterrupted()) {
                        System.out.println("Podsumowanie brygady trzeciej");
                        System.out.println("Brygadzista " + b3.getImie() + " " + b3.getNazwisko() + " " + b3.getPseudonim());
                        b3.getBrygada().showArchitekrow();
                        b3.getBrygada().showIloscKopniecPracownikow();
                        System.out.println("Ilość kopnięć brygady: " + b3.getBrygada().getIloscMachniecLopataBrygady());

                        System.out.println(" === Zarobki ===");
                        ArrayList<Osoba> zar = new ArrayList<>();
                        zar.addAll(b3.getBrygada().getKopacze());
                        zar.add(b3);
                        Collections.sort(zar);
                        for (Osoba x : zar) {
                            System.out.println(x.getImie() + " " + x.getNazwisko() + " " + x.pobierzPensje());
                        }
                    }
                } while(p3.isAlive());

        } catch (nieUnikalnyPeselException | IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
