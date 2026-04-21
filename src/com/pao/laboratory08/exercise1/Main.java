package com.pao.laboratory08.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) throws Exception {
        List<Student> studenti = citesteStudenti(FILE_PATH);

        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        String cmdLine = stdin.readLine();
        if (cmdLine == null) {
            return;
        }
        cmdLine = cmdLine.trim();

        if ("PRINT".equals(cmdLine)) {
            for (Student s : studenti) {
                System.out.println(s);
            }
            return;
        }

        String[] parts = cmdLine.split(" ", 2);
        String tip = parts[0];
        if(parts.length < 2) {
            return;
        }
        String nume = parts[1].trim();

        Student gasit = gasesteDupaNume(studenti, nume);
        if (gasit == null) {
            return;
        }

        if ("SHALLOW".equals(tip)) {
            Student clona = (Student) gasit.clone();
            clona.getAdresa().setOras("MODIFICAT");
            System.out.println("Original: " + gasit);
            System.out.println("Clona: " + clona);
        } else if ("DEEP".equals(tip)) {
            Student clona = (Student) gasit.deepClone();
            clona.getAdresa().setOras("MODIFICAT");
            System.out.println("Original: " + gasit);
            System.out.println("Clona: " + clona);
        }
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

    private static Student gasesteDupaNume(List<Student> studenti, String nume) {
        for (Student s : studenti) {
            if (s.getNume().equals(nume)) {
                return s;
            }
        }
        return null;
    }
}
