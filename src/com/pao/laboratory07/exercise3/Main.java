package com.pao.laboratory07.exercise3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import com.pao.laboratory07.exercise2.Comanda;
import com.pao.laboratory07.exercise2.ComandaGratuita;
import com.pao.laboratory07.exercise2.ComandaRedusa;
import com.pao.laboratory07.exercise2.ComandaStandard;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        List<Comanda> comenzi = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String tip = scanner.next();

            switch (tip) {
                case "STANDARD" -> {
                    String nume = scanner.next();
                    double pret = scanner.nextDouble();
                    String client = scanner.next();
                    comenzi.add(new ComandaStandard(nume, pret, client));
                }
                case "DISCOUNTED" -> {
                    String nume = scanner.next();
                    double pret = scanner.nextDouble();
                    int discountProcent = scanner.nextInt();
                    String client = scanner.next();
                    comenzi.add(new ComandaRedusa(nume, pret, discountProcent, client));
                }
                case "GIFT" -> {
                    String nume = scanner.next();
                    String client = scanner.next();
                    comenzi.add(new ComandaGratuita(nume, client));
                }
                default -> {
                    System.out.println("Tip de comanda necunoscut: " + tip);
                    return;
                }
            }
        }

        for (Comanda c : comenzi) {
            System.out.println(c.descriereCuClient());
        }
        System.out.println();

        while (true) {
            String comanda = scanner.next();

            switch (comanda) {
                case "STATS" -> {
                    System.out.println("--- STATS ---");

                    double sumaStandard = 0.0;
                    int nrStandard = 0;
                    double sumaDiscounted = 0.0;
                    int nrDiscounted = 0;
                    double sumaGift = 0.0;
                    int nrGift = 0;

                    for (Comanda c : comenzi) {
                        if (c instanceof ComandaStandard) {
                            sumaStandard += c.pretFinal();
                            nrStandard++;
                        } else if (c instanceof ComandaRedusa) {
                            sumaDiscounted += c.pretFinal();
                            nrDiscounted++;
                        } else if (c instanceof ComandaGratuita) {
                            sumaGift += c.pretFinal();
                            nrGift++;
                        }
                    }

                    double medieStandard = nrStandard == 0 ? 0.0 : sumaStandard / nrStandard;
                    double medieDiscounted = nrDiscounted == 0 ? 0.0 : sumaDiscounted / nrDiscounted;
                    double medieGift = nrGift == 0 ? 0.0 : sumaGift / nrGift;

                    System.out.printf("STANDARD: medie = %.2f lei%n", medieStandard);
                    System.out.printf("DISCOUNTED: medie = %.2f lei%n", medieDiscounted);
                    System.out.printf("GIFT: medie = %.2f lei%n", medieGift);
                    System.out.println();
                }

                case "FILTER" -> {
                    double threshold = scanner.nextDouble();
                    System.out.printf("--- FILTER (>= %.2f) ---%n", threshold);

                    for (Comanda c : comenzi) {
                        if (c.pretFinal() >= threshold) {
                            System.out.println(c.descriereCuClient());
                        }
                    }
                    System.out.println();
                }

                case "SORT" -> {
                    System.out.println("--- SORT (by client, then by pret) ---");

                    List<Comanda> sortate = new ArrayList<>(comenzi);
                    sortate.sort(
                            Comparator.comparing(Comanda::getClient)
                                      .thenComparingDouble(Comanda::pretFinal)
                    );

                    for (Comanda c : sortate) {
                        System.out.println(c.descriereCuClient());
                    }
                    System.out.println();
                }

                case "SPECIAL" -> {
                    System.out.println("--- SPECIAL (discount > 15%) ---");

                    for (Comanda c : comenzi) {
                        if (c instanceof ComandaRedusa cr && cr.getDiscountProcent() > 15) {
                            System.out.println(cr.descriereCuClient());
                        }
                    }
                }

                case "QUIT" -> {
                    return;
                }

                default -> {
                    System.out.println("Comanda necunoscuta: " + comanda);
                    return;
                }
            }
        }
    }
}