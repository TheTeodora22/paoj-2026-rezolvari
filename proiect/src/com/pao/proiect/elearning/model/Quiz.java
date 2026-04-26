package com.pao.proiect.elearning.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Quiz {
    private int id;
    private String titlu;
    private final List<Intrebare> intrebari;

    public Quiz() {
        this.intrebari = new ArrayList<>();
    }

    public Quiz(int id, String titlu) {
        this.id = id;
        this.titlu = titlu;
        this.intrebari = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitlu() {
        return titlu;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public List<Intrebare> getIntrebari() {
        return Collections.unmodifiableList(intrebari);
    }

    public void adaugaIntrebare(Intrebare intrebare) {
        intrebari.add(intrebare);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Quiz quiz = (Quiz) o;
        return id == quiz.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Quiz{id=" + id + ", titlu='" + titlu + "', nrIntrebari=" + intrebari.size() + "}";
    }
}
