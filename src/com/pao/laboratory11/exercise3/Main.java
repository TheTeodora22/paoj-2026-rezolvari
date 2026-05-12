package com.pao.laboratory11.exercise3;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        List<Transaction> data = new ArrayList<>();
        data.add(new Transaction(1, new BigDecimal("100.00"), LocalDate.of(2026, 5, 1), "RO", "WEB"));
        data.add(new Transaction(2, new BigDecimal("100.00"), LocalDate.of(2026, 5, 1), "RO", "ATM"));
        data.add(new Transaction(3, new BigDecimal("500.00"), LocalDate.of(2026, 5, 2), "DE", "APP"));
        data.add(new Transaction(4, new BigDecimal("250.00"), LocalDate.of(2026, 5, 2), "RO", "WEB"));
        data.add(new Transaction(5, new BigDecimal("500.00"), LocalDate.of(2026, 5, 3), "DE", "POS"));

        Snapshot snap = data.stream().collect(CustomCollectors.toSnapshot(3));

        System.out.println("\nTop din snapshot:");
        snap.getTopTransactions().forEach(System.out::println);

        System.out.println("\nTari dupa volum (desc):");
        snap.getCountByCountry().entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));

        System.out.println("\nCanale dupa numar (cresc):");
        snap.getCountByChannel().entrySet().stream()
                .sorted(Comparator.<Map.Entry<String, Long>>comparingLong(Map.Entry::getValue)
                        .thenComparing(Map.Entry::getKey))
                .forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));

        System.out.println("\nTotal volum:");
        System.out.println(snap.getTotalAmount());
    }
}
