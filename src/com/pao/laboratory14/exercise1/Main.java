package com.pao.laboratory14.exercise1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine().trim());

        List<Bilet> bilete = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String[] tok = br.readLine().trim().split("\\s+");
            int id = Integer.parseInt(tok[0]);
            double pret = Double.parseDouble(tok[tok.length - 1]);
            TipBilet tip = TipBilet.valueOf(tok[tok.length - 2]);
            StringBuilder eveniment = new StringBuilder(tok[1]);
            for (int j = 2; j < tok.length - 2; j++) {
                eveniment.append(' ').append(tok[j]);
            }
            bilete.add(new Bilet(id, eveniment.toString(), tip, pret));
        }

        String comanda = br.readLine().trim();
        RaportVanzari raport = bilete.stream().collect(RaportVanzariCollector.toRaport());

        afiseazaRaportSimplu(raport);
        if ("RAPORT_COMPLET".equals(comanda)) {
            System.out.println("---");
            System.out.printf("Total: %s RON%n", String.format("%.2f", raport.getTotalGlobal()));
            System.out.printf("Medie: %s RON%n", String.format("%.2f", raport.getMedieGlobala()));
            System.out.println("Cel mai popular: " + raport.getTipCelMaiPopular().name());
        }
    }

    private static void afiseazaRaportSimplu(RaportVanzari raport) {
        Arrays.stream(TipBilet.values())
                .filter(t -> raport.getNumarPerTip().containsKey(t))
                .forEach(t -> System.out.printf(
                        "%s: count=%d incasari=%s RON%n",
                        t.name(),
                        raport.getNumarPerTip().get(t),
                        String.format("%.2f", raport.getIncasariPerTip().get(t))));
    }
}
