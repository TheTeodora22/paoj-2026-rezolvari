package com.pao.laboratory08.exercise2;

import com.pao.laboratory08.exercise1.Adresa;
import com.pao.laboratory08.exercise1.Student;

import java.io.*;
import java.util.*;

public class Main {
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) throws Exception {
        List<Student> studenti = citesteStudenti(FILE_PATH);

        Scanner scanner = new Scanner(System.in);
        int prag = scanner.nextInt();

        List<Student> filtrati = new ArrayList<>();
        for (Student s : studenti) {
            if (s.getVarsta() >= prag) {
                filtrati.add(s);
            }
        }

        try (BufferedWriter fout = new BufferedWriter(new FileWriter("src/com/pao/laboratory08/exercise2/rezultate.txt"))) {
            for (Student s : filtrati) {
                fout.write(s.toString());
                fout.newLine();
            }
        }

        System.out.println("Filtru: varsta >= " + prag);
        System.out.println("Rezultate: " + filtrati.size() + " studenti");
        System.out.println();
        for (Student s : filtrati) {
            System.out.println(s);
        }
        System.out.println();
        System.out.println("Scris in: rezultate.txt");
    }

    private static List<Student> citesteStudenti(String cale) throws Exception {
        List<Student> lista = new ArrayList<>();
        try (BufferedReader fin = new BufferedReader(new FileReader(cale))) {
            String linie;
            while ((linie = fin.readLine()) != null) {
                linie = linie.trim();
                if (linie.isEmpty()) {
                    continue;
                }
                String[] csv = linie.split(",");
                String nume = csv[0].trim();
                int varsta = Integer.parseInt(csv[1].trim());
                String oras = csv[2].trim();
                String strada = csv[3].trim();
                lista.add(new Student(nume, varsta, new Adresa(oras, strada)));
            }
        }
        return lista;
    }
}
