package com.pao.proiect.elearning.model;

public class Instructor extends Utilizator {
    private String departament;

    public Instructor() {
    }

    public Instructor(int id, String username, String email, String parola, String departament) {
        super(id, username, email, parola);
        this.departament = departament;
    }

    @Override
    public String getRol() {
        return "INSTRUCTOR";
    }

    public String getDepartament() {
        return departament;
    }

    public void setDepartament(String departament) {
        this.departament = departament;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o) && o instanceof Instructor;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "Instructor{" + super.toString() + ", departament='" + departament + "'}";
    }
}
