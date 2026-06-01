package com.pao.laboratory14.exercise2;

import com.pao.laboratory14.exercise1.TipBilet;
import com.pao.laboratory14.exercise2.model.Eveniment;
import com.pao.laboratory14.exercise2.repository.EvenimentRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, IOException {
        EvenimentRepository repo = new EvenimentRepository();
        repo.initSchema();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }

            String[] tok = line.split("\\s+");
            switch (tok[0]) {
                case "ADD" -> {
                    TipBilet tip = TipBilet.valueOf(tok[tok.length - 1]);
                    int capacitate = Integer.parseInt(tok[tok.length - 2]);
                    String data = tok[tok.length - 3];
                    StringBuilder nume = new StringBuilder(tok[1]);
                    for (int i = 2; i < tok.length - 3; i++) {
                        nume.append(' ').append(tok[i]);
                    }
                    Eveniment eveniment = new Eveniment(nume.toString(), data, capacitate, tip);
                    repo.save(eveniment);
                    System.out.printf("Adaugat: [%d] %s%n", eveniment.getId(), eveniment.getNume());
                }
                case "LIST" -> {
                    for (Eveniment e : repo.findAll()) {
                        System.out.printf(
                                "[%d] %s | %s | cap=%d | %s%n",
                                e.getId(),
                                e.getNume(),
                                e.getData(),
                                e.getCapacitate(),
                                e.getTip().name());
                    }
                }
                case "DELETE" -> {
                    int id = Integer.parseInt(tok[1]);
                    if (repo.deleteImpl(id) > 0) {
                        System.out.println("Sters: " + id);
                    } else {
                        System.out.println("Nu exista: " + id);
                    }
                }
                case "COUNT" -> System.out.println("Total: " + repo.count());
            }
        }
    }
}
