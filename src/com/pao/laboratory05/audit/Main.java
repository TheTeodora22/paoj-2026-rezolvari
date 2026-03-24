package com.pao.laboratory05.audit;

/**
 * Exercise 3 — Angajați
 *
 * Cerințele complete se află în:
 *   src/com/pao/laboratory05/Readme.md  →  secțiunea "Exercise 3 — Angajați"
 *
 * Creează fișierele de la zero în acest pachet, apoi rulează Main.java
 * pentru a verifica output-ul așteptat din Readme.
 */

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        while (true) {
    System.out.println("\n===== Gestionare Angajați =====");
    System.out.println("1. Adaugă angajat");
    System.out.println("2. Listare după salariu");
    System.out.println("3. Caută după departament");
    System.out.println("4. Afișează audit log");
    System.out.println("0. Ieșire");
    System.out.print("Opțiune: ");
    Scanner scanner = new Scanner(System.in);
    int optiune = scanner.nextInt();
    switch (optiune) {
        case 1:
            String nume, numeDepartament, locatieDepartament;
            double salariu;
            System.out.print("Nume: ");
            nume = scanner.next();
            System.out.print("Departament nume ");
            numeDepartament = scanner.next();
            System.out.print("Departament locatie ");
            locatieDepartament = scanner.next();
            System.out.print("Salariu: ");
            salariu = scanner.nextDouble();
            Departament dept = new Departament(numeDepartament, locatieDepartament);
            Angajat angajat = new Angajat(nume, dept, salariu);
            AngajatService.getInstance().addAngajat(angajat);
            break;
        case 2:
            AngajatService.getInstance().listBySalary();
            break;
        case 3:
            String numeDept = scanner.next();
            AngajatService.getInstance().findByDepartment(numeDept);
            break;
        case 4:
             AngajatService.getInstance().printAuditLog();
             break;
        case 0:
            System.out.println("La revedere!");
            return;
        default:
            break;
    }
    }
    }
}
