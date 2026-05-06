package com.pao.laboratory10.exercise2;

import com.pao.laboratory10.exercise1.Tranzactie;
import com.pao.laboratory10.exercise1.TipTranzactie;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește N din stdin, apoi cele N tranzacții (id suma data tip) — pot exista duplicate de id
        //    Stochează-le toate într-un ArrayList<Tranzactie> (cu duplicate, ordine inserare)
        //
        // 2. Procesează comenzile din stdin până la EOF:
        //
        //   UNIQUE_IDS      → LinkedHashSet<Integer> cu id-urile în ordinea primei apariții
        //                     afișează: "IDs unice (N): [1, 2, 3, ...]"
        //
        //   MONTHLY_REPORT  → TreeMap<String, ...> grupat pe yyyy-MM (substring 0-7 din data)
        //                     pentru fiecare lună, sumele CREDIT și DEBIT
        //                     format: "yyyy-MM: CREDIT X.XX RON, DEBIT Y.YY RON"
        //
        //   TOP n           → primele n tranzacții după suma descrescătoare (nu modifică lista)
        //                     afișează "Top n:" urmat de n linii
        //
        //   SORT_ASC        → Collections.sort cu suma crescătoare; afișează lista sortată
        //   SORT_DESC       → Collections.sort cu suma descrescătoare; afișează lista sortată
        //   REVERSE         → Collections.reverse; afișează lista
        //   MIN_MAX         → Collections.min/max după suma
        //                     "MIN: [id] data tip: suma RON"
        //                     "MAX: [id] data tip: suma RON"
        //
        //   CME_DEMO        → încearcă for(t : lista) lista.remove(t) în try-catch
        //                     afișează "ConcurrentModificationException prins: modificare in iteratie detectata."
        //
        // Format linie tranzacție: [id] data tip: suma RON
        //   Ex: [1] 2024-01-15 CREDIT: 1500.00 RON

        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        ArrayList<Tranzactie> tranzactii = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            int id = scanner.nextInt();
            double suma = scanner.nextDouble();
            String data = scanner.next();
            TipTranzactie tip = TipTranzactie.valueOf(scanner.next());
            tranzactii.add(new Tranzactie(id, suma, data, tip));
        }
        while (scanner.hasNext()) {
            String command = scanner.next();
            switch (command) { 
                case "UNIQUE_IDS":
                    LinkedHashSet<Integer> uniqueIds = new LinkedHashSet<>();
                    for (Tranzactie t : tranzactii) {
                        uniqueIds.add(t.getId());
                    }
                    System.out.println("IDs unice (" + uniqueIds.size() + "): " + uniqueIds);
                    break;
                case "MONTHLY_REPORT":
                    TreeMap<String, double[]> monthlyReport = new TreeMap<>();
                    for (Tranzactie t : tranzactii) {
                        String month = t.getData().substring(0, 7);
                        double[] sums = monthlyReport.computeIfAbsent(month, k -> new double[2]);
                        if (t.getTip() == TipTranzactie.CREDIT) {
                            sums[0] += t.getSuma();
                        } else {
                            sums[1] += t.getSuma();
                        }
                    }
                    for (Map.Entry<String, double[]> entry : monthlyReport.entrySet()) {
                        double[] sums = entry.getValue();
                        System.out.printf("%s: CREDIT %.2f RON, DEBIT %.2f RON%n", entry.getKey(), sums[0], sums[1]);
                    }
                    break;
                case "TOP":
                    int n = scanner.nextInt();
                    Collections.sort(tranzactii, (a, b) -> Double.compare(b.getSuma(), a.getSuma()));
                    System.out.println("Top n:");
                    for (int i = 0; i < n; i++) {
                        System.out.println(tranzactii.get(i));
                    }
                    break;
                case "SORT_ASC":
                    Collections.sort(tranzactii, (a, b) -> Double.compare(a.getSuma(), b.getSuma()));
                    for (Tranzactie t : tranzactii) {
                        System.out.println(t);
                    }
                    break;
                case "SORT_DESC":
                    Collections.sort(tranzactii, (a, b) -> Double.compare(b.getSuma(), a.getSuma()));
                    for (Tranzactie t : tranzactii) {
                        System.out.println(t);
                    }
                    break;
                case "REVERSE":
                    Collections.reverse(tranzactii);
                    for (Tranzactie t : tranzactii) {
                        System.out.println(t);
                    }
                    break;
                case "MIN_MAX":
                    Tranzactie min = Collections.min(tranzactii, Comparator.comparingDouble(Tranzactie::getSuma));
                    Tranzactie max = Collections.max(tranzactii, Comparator.comparingDouble(Tranzactie::getSuma));
                    System.out.println("MIN: " + min);
                    System.out.println("MAX: " + max);
                    break;
                case "CME_DEMO":
                    try {
                        for (Tranzactie t : tranzactii) {
                            tranzactii.remove(t);
                        }
                    } catch (ConcurrentModificationException e) {
                        System.out.println("ConcurrentModificationException prins: modificare in iteratie detectata.");
                    }
                    break;
            }
        }
    }
}
