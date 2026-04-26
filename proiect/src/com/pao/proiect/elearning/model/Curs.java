package com.pao.proiect.elearning.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Curs {
    private int id;
    private String titlu;
    private Categorie categorie;
    private Instructor instructor;
    private final List<Lectie> lectii;
    private final List<Quiz> quizuri;
    private final List<Tema> teme;
    private final Set<Integer> studentiInscrisiIds;

    public Curs() {
        this.lectii = new ArrayList<>();
        this.quizuri = new ArrayList<>();
        this.teme = new ArrayList<>();
        this.studentiInscrisiIds = new HashSet<>();
    }

    public Curs(int id, String titlu, Categorie categorie, Instructor instructor) {
        this.id = id;
        this.titlu = titlu;
        this.categorie = categorie;
        this.instructor = instructor;
        this.lectii = new ArrayList<>();
        this.quizuri = new ArrayList<>();
        this.teme = new ArrayList<>();
        this.studentiInscrisiIds = new HashSet<>();
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

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public List<Lectie> getLectii() {
        return Collections.unmodifiableList(lectii);
    }

    public void adaugaLectie(Lectie lectie) {
        lectii.add(lectie);
        lectii.sort(Comparator.comparingInt(Lectie::getOrdine));
    }

    public List<Quiz> getQuizuri() {
        return Collections.unmodifiableList(quizuri);
    }

    public void adaugaQuiz(Quiz quiz) {
        quizuri.add(quiz);
    }

    public boolean stergeQuiz(int quizId) {
        return quizuri.removeIf(q -> q.getId() == quizId);
    }

    public List<Tema> getTeme() {
        return Collections.unmodifiableList(teme);
    }

    public void adaugaTema(Tema tema) {
        teme.add(tema);
    }

    public Set<Integer> getStudentiInscrisiIds() {
        return Collections.unmodifiableSet(studentiInscrisiIds);
    }

    public void inscrieStudent(int studentId) {
        studentiInscrisiIds.add(studentId);
    }

    public void renuntaStudent(int studentId) {
        studentiInscrisiIds.remove(studentId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Curs curs = (Curs) o;
        return id == curs.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Curs{id=" + id + ", titlu='" + titlu + "', categorie=" + categorie + "}";
    }
}
