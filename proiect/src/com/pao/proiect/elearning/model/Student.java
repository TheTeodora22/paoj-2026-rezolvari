package com.pao.proiect.elearning.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Student extends Utilizator {
    private String grupa;
    private final List<Integer> cursuriInscrisIds;

    public Student() {
        this.cursuriInscrisIds = new ArrayList<>();
    }

    public Student(int id, String username, String email, String parola, String grupa) {
        super(id, username, email, parola);
        this.grupa = grupa;
        this.cursuriInscrisIds = new ArrayList<>();
    }

    @Override
    public String getRol() {
        return "STUDENT";
    }

    public String getGrupa() {
        return grupa;
    }

    public void setGrupa(String grupa) {
        this.grupa = grupa;
    }

    public List<Integer> getCursuriInscrisIds() {
        return Collections.unmodifiableList(cursuriInscrisIds);
    }

    public void inscrieLaCurs(int cursId) {
        if (!cursuriInscrisIds.contains(cursId)) {
            cursuriInscrisIds.add(cursId);
        }
    }

    public void renuntaLaCurs(int cursId) {
        cursuriInscrisIds.remove(Integer.valueOf(cursId));
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o) && o instanceof Student;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "Student{" + super.toString() + ", grupa='" + grupa + "', cursuri=" + cursuriInscrisIds + "}";
    }
}
