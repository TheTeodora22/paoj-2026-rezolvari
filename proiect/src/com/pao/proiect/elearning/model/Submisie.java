package com.pao.proiect.elearning.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Submisie {
    private int id;
    private int studentId;
    private int temaId;
    private String continut;
    private LocalDateTime dataTrimiterii;
    private Double nota;

    public Submisie() {
    }

    public Submisie(int id, int studentId, int temaId, String continut, LocalDateTime dataTrimiterii) {
        this.id = id;
        this.studentId = studentId;
        this.temaId = temaId;
        this.continut = continut;
        this.dataTrimiterii = dataTrimiterii;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getTemaId() {
        return temaId;
    }

    public void setTemaId(int temaId) {
        this.temaId = temaId;
    }

    public String getContinut() {
        return continut;
    }

    public void setContinut(String continut) {
        this.continut = continut;
    }

    public LocalDateTime getDataTrimiterii() {
        return dataTrimiterii;
    }

    public void setDataTrimiterii(LocalDateTime dataTrimiterii) {
        this.dataTrimiterii = dataTrimiterii;
    }

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Submisie submisie = (Submisie) o;
        return id == submisie.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Submisie{id=" + id + ", studentId=" + studentId + ", temaId=" + temaId + ", nota=" + nota + "}";
    }
}
