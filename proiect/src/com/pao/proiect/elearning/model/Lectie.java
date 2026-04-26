package com.pao.proiect.elearning.model;

import java.util.Objects;

public class Lectie {
    private int id;
    private String titlu;
    private String continut;
    private int ordine;

    public Lectie() {
    }

    public Lectie(int id, String titlu, String continut, int ordine) {
        this.id = id;
        this.titlu = titlu;
        this.continut = continut;
        this.ordine = ordine;
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

    public String getContinut() {
        return continut;
    }

    public void setContinut(String continut) {
        this.continut = continut;
    }

    public int getOrdine() {
        return ordine;
    }

    public void setOrdine(int ordine) {
        this.ordine = ordine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Lectie lectie = (Lectie) o;
        return id == lectie.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Lectie{id=" + id + ", titlu='" + titlu + "', ordine=" + ordine + "}";
    }
}
