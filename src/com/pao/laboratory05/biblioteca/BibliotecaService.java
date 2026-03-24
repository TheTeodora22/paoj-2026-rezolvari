package com.pao.laboratory05.biblioteca;

import java.util.Comparator;

public class BibliotecaService {

    private Carte[] carti;
    BibliotecaService() {
        this.carti = new Carte[0];
    }

    private static class Holder {
        private static final BibliotecaService INSTANCE = new BibliotecaService();
    }

    public static BibliotecaService getInstance() {
        return Holder.INSTANCE;
    }

    void addCarte(Carte carte) {
        Carte[] newCarti = new Carte[carti.length + 1];
        System.arraycopy(carti, 0, newCarti, 0, carti.length);
        newCarti[carti.length] = carte;
        carti = newCarti;
        System.out.println("Carte adaugata: " + carte.getTitlu());
    }

    void listSortedByRating() {
        Carte[] sortedCarti = carti.clone();
        java.util.Arrays.sort(sortedCarti);
        System.out.println("Sortate dupa rating:");
        int i = 1;
        for (Carte carte : sortedCarti) {
            System.out.println(i + ". " + carte);
            i++;
        }

    }
    void listSortedBy(Comparator<Carte> comparator) {
        Carte[] sortedCarti = carti.clone();
        java.util.Arrays.sort(sortedCarti, comparator);
        System.out.println("Sortate dupa comparator:");
        int i = 1;
        for (Carte carte : sortedCarti) {
            System.out.println(i + ". " + carte);
            i++;
        }
    }
}
