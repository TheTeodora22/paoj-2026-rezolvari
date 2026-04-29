package com.pao.laboratory09.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex1.ser";

    public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește N din stdin, apoi cele N tranzacții (id suma data contSursa contDestinatie tip)
        // 2. Setează câmpul note = "procesat" pe fiecare tranzacție înainte de serializare
        // 3. Serializează lista de tranzacții în OUTPUT_FILE cu ObjectOutputStream (try-with-resources)
        // 4. Deserializează lista din OUTPUT_FILE cu ObjectInputStream (try-with-resources)
        // 5. Procesează comenzile din stdin până la EOF:
        //    - LIST          → afișează toate tranzacțiile, câte una pe linie
        //    - FILTER yyyy-MM → afișează tranzacțiile cu data care începe cu yyyy-MM
        //                       sau "Niciun rezultat." dacă nu există
        //    - NOTE id        → afișează "NOTE[id]: <valoarea câmpului note>"
        //                       sau "NOTE[id]: not found" dacă id-ul nu există
        //
        // Format linie tranzacție:
        //   [id] data tip: suma RON | contSursa -> contDestinatie
        //   Ex: [1] 2024-01-15 CREDIT: 1500.00 RON | RO01SRC1 -> RO01DST1

        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        List<Tranzactie> tranzactii = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            int id = scanner.nextInt();
            double suma = scanner.nextDouble();
            String data = scanner.next();
            String contSursa = scanner.next();
            String contDestinatie = scanner.next();
            String tip = scanner.next();
            TipTranzactie tipTranzactie = TipTranzactie.valueOf(tip);
            Tranzactie tranzactie = new Tranzactie(id, suma, data, contSursa, contDestinatie, tipTranzactie);
            tranzactii.add(tranzactie);
            tranzactie.setNote("procesat");
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(OUTPUT_FILE))) {
            oos.writeObject(tranzactii);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Tranzactie> tranzactiiDeserializate = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(OUTPUT_FILE))) {
            tranzactiiDeserializate = (List<Tranzactie>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        while (scanner.hasNext()) {
            String command = scanner.next();
            if (command.equals("LIST")) {
                for (Tranzactie tranzactie : tranzactiiDeserializate) {
                    System.out.println(tranzactie);
                }
            } else if (command.equals("FILTER")) {
                String data = scanner.next();
                boolean found = false;
                for (Tranzactie tranzactie : tranzactiiDeserializate) {
                    if (tranzactie.getData().startsWith(data)) {
                        System.out.println(tranzactie);
                        found = true;
                    }
                }
                if (!found) {
                    System.out.println("Niciun rezultat.");
                }
            } else if (command.equals("NOTE")) {
                int id = scanner.nextInt();
                boolean found = false;
                for (Tranzactie tranzactie : tranzactiiDeserializate) {
                    if (tranzactie.getId() == id) {
                        System.out.println("NOTE[" + id + "]: " + tranzactie.getNote());
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    System.out.println("NOTE[" + id + "]: not found");
                }
            }
}
}
}
