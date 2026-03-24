package com.pao.laboratory05.audit;


public class AngajatService {
    private Angajat[] angajati;
    private AuditEntry[] auditLog;
    public AngajatService() {
        this.angajati = new Angajat[0];
        this.auditLog = new AuditEntry[0];
    }
    private static class Holder {
        private static final AngajatService INSTANCE = new AngajatService();
    }
    public static AngajatService getInstance() {
        return Holder.INSTANCE;
    }

    private void logAction(String action, String target) {
        String timestamp = java.time.LocalDateTime.now().toString();
        AuditEntry entry = new AuditEntry(action, target, timestamp);
        AuditEntry[] newLog = new AuditEntry[auditLog.length + 1];
        System.arraycopy(auditLog, 0, newLog, 0, auditLog.length);
        newLog[auditLog.length] = entry;
        auditLog = newLog;
    }
    public void addAngajat(Angajat a) {
        Angajat[] newAngajati = new Angajat[angajati.length + 1];
        System.arraycopy(angajati, 0, newAngajati, 0, angajati.length);
        newAngajati[angajati.length] = a;
        angajati = newAngajati;
        System.out.println("Angajat adaugat: " + a.getNume());
        logAction("ADD", a.getNume());
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
        logAction("FIND_BY_DEPT", numeDept);
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

    public void printAuditLog() {
        System.out.println("Audit Log:");
        for (AuditEntry entry : auditLog) {
            System.out.println(entry);
        }
    }
}
