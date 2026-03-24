package com.pao.laboratory05.angajati;


public class AngajatService {
    private Angajat[] angajati;
    public AngajatService() {
        this.angajati = new Angajat[0];
    }
    private static class Holder {
        private static final AngajatService INSTANCE = new AngajatService();
    }
    public static AngajatService getInstance() {
        return Holder.INSTANCE;
    }

    public void addAngajat(Angajat a) {
        Angajat[] newAngajati = new Angajat[angajati.length + 1];
        System.arraycopy(angajati, 0, newAngajati, 0, angajati.length);
        newAngajati[angajati.length] = a;
        angajati = newAngajati;
        System.out.println("Angajat adaugat: " + a.getNume());

    }

    public void listBySalary() {
        Angajat[] sortedAngajati = angajati.clone();
        java.util.Arrays.sort(sortedAngajati);
        System.out.println("Angajati sortati dupa salariu:");
        for (Angajat a : sortedAngajati) {
            System.out.println(a);
        }
    }

    public void findByDepartment(String numeDept) {
        System.out.println("Angajati din departamentul " + numeDept + ":");
        Boolean found = false;
        for (Angajat a : angajati) {
            if (a.getDepartament().nume().equalsIgnoreCase(numeDept)) {
                System.out.println(a);
                found = true;
            }
        }
        if(found == false) {
            System.out.println("Niciun angajat în departamentul: " + numeDept);
            
        }
    }
}
