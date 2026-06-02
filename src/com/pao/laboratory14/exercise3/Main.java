package com.pao.laboratory14.exercise3;

import java.util.*;

/**
 * Bonus — Alocare Automata de Sali pentru Evenimente
 * <p>
 * Problema clasica de interviu: date N evenimente cu intervale [start, end],
 * gaseste numarul minim de sali necesare si atribuie fiecare eveniment la o sala.
 * <p>
 * Doua variante demonstrate:
 * Varianta 1 — greedy simplu O(N^2): prima sala disponibila
 * Varianta 2 — PriorityQueue O(N log N): min-heap de ore de final
 */
public class Main {

    record Eveniment(String nume, int startMin, int endMin) {
    }

    private static int toMin(String hhmm) {
        String[] p = hhmm.split(":");
        return Integer.parseInt(p[0]) * 60 + Integer.parseInt(p[1]);
    }

    private static String toHHMM(int min) {
        return String.format("%02d:%02d", min / 60, min % 60);
    }

    public static void main(String[] args) {
        List<Eveniment> evenimente = List.of(
                new Eveniment("PAOJ", toMin("08:00"), toMin("09:30")),
                new Eveniment("Concert Rock", toMin("09:00"), toMin("10:00")),
                new Eveniment("Gala VIP", toMin("09:00"), toMin("11:00")),
                new Eveniment("Standup", toMin("09:30"), toMin("10:30")),
                new Eveniment("Teatru", toMin("10:00"), toMin("11:00")),
                new Eveniment("Jazz", toMin("11:00"), toMin("12:00")),
                new Eveniment("Opera", toMin("11:00"), toMin("12:00")),
                new Eveniment("Festival", toMin("11:30"), toMin("12:30")));

        List<Eveniment> sortate = new ArrayList<>(evenimente);
        sortate.sort(Comparator.comparingInt(Eveniment::startMin));

        System.out.println("Varianta 1");
        int saliVarianta1 = varianta1(sortate);
        System.out.println();
        System.out.println("Sali minime: " + saliVarianta1);

        System.out.println();
        System.out.println("Varianta 2");
        int saliVarianta2 = varianta2(sortate);
        System.out.println("Sali minime: " + saliVarianta2);
    }

    private static int varianta1(List<Eveniment> sortate) {
        List<Integer> rooms = new ArrayList<>();

        for (Eveniment e : sortate) {
            int sala = -1;
            for (int i = 0; i < rooms.size(); i++) {
                if (rooms.get(i) <= e.startMin()) {
                    sala = i + 1;
                    rooms.set(i, e.endMin());
                    break;
                }
            }
            if (sala == -1) {
                rooms.add(e.endMin());
                sala = rooms.size();
            }

            System.out.printf(
                    "%s (%s - %s)  ->  Sala %d%n",
                    e.nume(),
                    toHHMM(e.startMin()),
                    toHHMM(e.endMin()),
                    sala);
        }

        return rooms.size();
    }

    private static int varianta2(List<Eveniment> sortate) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();

        for (Eveniment e : sortate) {
            if (!pq.isEmpty() && pq.peek() <= e.startMin()) {
                pq.poll();
            }
            pq.offer(e.endMin());
        }

        return pq.size();
    }
}
