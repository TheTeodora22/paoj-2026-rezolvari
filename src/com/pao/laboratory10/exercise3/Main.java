package com.pao.laboratory10.exercise3;

import com.pao.laboratory10.exercise1.TipTranzactie;
import com.pao.laboratory10.exercise3.TranzactieDemo;   

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<TranzactieDemo> tranzactii = List.of(
                new TranzactieDemo(1, 1500.00, "2025-01-15", TipTranzactie.CREDIT, "CONT_A"),
                new TranzactieDemo(2, 750.50, "2025-01-22", TipTranzactie.DEBIT, "CONT_B"),
                new TranzactieDemo(3, 200.00, "2025-02-05", TipTranzactie.CREDIT, "CONT_A"),
                new TranzactieDemo(4, 1200.00, "2025-02-18", TipTranzactie.DEBIT, "CONT_C"),
                new TranzactieDemo(5, 500.00, "2025-03-10", TipTranzactie.CREDIT, "CONT_D"),
                new TranzactieDemo(6, 300.00, "2025-03-22", TipTranzactie.DEBIT, "CONT_B"),
                new TranzactieDemo(7, 1800.00, "2025-01-30", TipTranzactie.CREDIT, "CONT_E"),
                new TranzactieDemo(8, 650.00, "2025-02-12", TipTranzactie.DEBIT, "CONT_A"),
                new TranzactieDemo(9, 980.00, "2025-03-05", TipTranzactie.CREDIT, "CONT_C"),
                new TranzactieDemo(10, 430.00, "2025-03-27", TipTranzactie.DEBIT, "CONT_D")
        );

        System.out.println("1) Tranzactii CREDIT:");
        tranzactii.stream()
                .filter(t -> t.getTip() == TipTranzactie.CREDIT)
                .forEach(System.out::println);
                

        System.out.println("\n2) Total procesat:");
        double totalProcesat = tranzactii.stream()
                .mapToDouble(TranzactieDemo::getSuma)
                .sum();
        System.out.printf("Total procesat: %.2f RON%n", totalProcesat);

        System.out.println("\n3) Total per luna:");
        Map<String, Double> totalPeLuna = tranzactii.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getData().substring(0, 7),
                        TreeMap::new,
                        Collectors.summingDouble(TranzactieDemo::getSuma)
                ));
        totalPeLuna.forEach((luna, suma) -> System.out.printf("%s: %.2f RON%n", luna, suma));

        System.out.println("\n4) Top 3 tranzactii:");
        tranzactii.stream()
                .sorted((a, b) -> Double.compare(b.getSuma(), a.getSuma()))
                .limit(3)
                .forEach(System.out::println);

        System.out.println("\n5) Conturi sursa unice:");
        List<String> conturiUnice = tranzactii.stream()
                .map(TranzactieDemo::getContSursa)
                .distinct()
                .collect(Collectors.toList());
        System.out.println("Conturi sursa unice: " + conturiUnice);

        System.out.println("\n6) Suma medie:");
        double sumaMedie = tranzactii.stream()
                .mapToDouble(TranzactieDemo::getSuma)
                .average()
                .orElse(0.0);
        System.out.printf("Suma medie: %.2f RON%n", sumaMedie);

        System.out.println("\n7) Extras de cont pe luni:");
        Map<String, List<TranzactieDemo>> extrasPeLuna = tranzactii.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getData().substring(0, 7),
                        TreeMap::new,
                        Collectors.toList()
                ));
        extrasPeLuna.forEach((luna, lista) -> {
            double total = lista.stream().mapToDouble(TranzactieDemo::getSuma).sum();
            System.out.printf("EXTRAS DE CONT - %s: %d tranzactii, total: %.2f RON%n", luna, lista.size(), total);
        });
    }
}
