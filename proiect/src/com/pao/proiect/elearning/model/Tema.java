package com.pao.proiect.elearning.model;

import java.time.LocalDate;
import java.util.Objects;

public class Tema {
    private int id;
    private String titlu;
    private String descriere;
    private LocalDate termenLimita;

    public Tema() {
    }

    public Tema(int id, String titlu, String descriere, LocalDate termenLimita) {
        this.id = id;
        this.titlu = titlu;
        this.descriere = descriere;
        this.termenLimita = termenLimita;
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

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public LocalDate getTermenLimita() {
        return termenLimita;
    }

    public void setTermenLimita(LocalDate termenLimita) {
        this.termenLimita = termenLimita;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tema tema = (Tema) o;
        return id == tema.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Tema{id=" + id + ", titlu='" + titlu + "', termen=" + termenLimita + "}";
    }
}
