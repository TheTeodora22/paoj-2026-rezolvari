package com.pao.laboratory10.exercise1;

import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // TODO: Implementează conform Readme.md
        //
        // Folosește LinkedList<Tranzactie> ca structură internă.
        // Citește comenzi din stdin până la EOF:
        //
        //   ENQUEUE id suma data tip   → addLast  (niciun output)
        //   DEQUEUE                    → removeFirst sau "Coada goala."
        //                                format: "Procesat: [id] data tip: suma RON"
        //   PUSH id suma data tip      → addFirst  (niciun output)
        //   POP                        → removeFirst sau "Coada goala."
        //                                format: "Extras: [id] data tip: suma RON"
        //   REMOVE_DEBIT               → Iterator.remove() pe toate DEBIT
        //                                afișează "Eliminat N tranzactii DEBIT."
        //   REMOVE_BELOW threshold     → Iterator.remove() pe suma < threshold
        //                                afișează "Eliminat N tranzactii sub threshold RON."
        //   PRINT                      → afișează toate, câte una pe linie
        //   SIZE                       → "Dimensiune coada: N"
        //
        // Format linie tranzacție: [id] data tip: suma RON
        //   Ex: [1] 2024-01-10 CREDIT: 500.00 RON

        LinkedList<Tranzactie> coada = new LinkedList<>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String linie = scanner.nextLine();
            String[] p = linie.split(" ");
            String comanda = p[0];

            switch (comanda) {
                case "ENQUEUE" -> {
                    int id = Integer.parseInt(p[1]);
                    double suma = Double.parseDouble(p[2]);
                    String data = p[3];
                    TipTranzactie tip = TipTranzactie.valueOf(p[4]);
                    coada.addLast(new Tranzactie(id, suma, data, tip));
                }
                case "DEQUEUE" -> {
                    if (coada.isEmpty()) {
                        System.out.println("Coada goala.");
                    } else {
                        Tranzactie t = coada.removeFirst();
                        System.out.printf("Procesat: %s%n", t);
                    }
                }
                case "PUSH" -> {
                    int id = Integer.parseInt(p[1]);
                    double suma = Double.parseDouble(p[2]);
                    String data = p[3];
                    TipTranzactie tip = TipTranzactie.valueOf(p[4]);
                    coada.addFirst(new Tranzactie(id, suma, data, tip));
                }
                case "POP" -> {
                    if (coada.isEmpty()) {
                        System.out.println("Coada goala.");
                    } else {
                        Tranzactie t = coada.removeFirst();
                        System.out.printf("Extras: %s%n", t);
                    }
                }
                case "REMOVE_DEBIT" -> {
                    int count = 0;
                    var it = coada.iterator();
                    while (it.hasNext()) {
                        if (it.next().getTip() == TipTranzactie.DEBIT) {
                            it.remove();
                            count++;
                        }
                    }
                    System.out.printf("Eliminat %d tranzactii DEBIT.%n", count);
                }
                case "REMOVE_BELOW" -> {
                    double threshold = Double.parseDouble(p[1]);
                    int count = 0;
                    var it = coada.iterator();
                    while (it.hasNext()) {
                        if (it.next().getSuma() < threshold) {
                            it.remove();
                            count++;
                        }
                    }
                    System.out.printf("Eliminat %d tranzactii sub %.2f RON.%n", count, threshold);
                }
                case "PRINT" -> 
                {
                    for (Tranzactie t : coada) {
                        System.out.println(t);
                    }
                }
                case "SIZE" -> System.out.printf("Dimensiune coada: %d%n", coada.size());
            }
        }
    }
}
