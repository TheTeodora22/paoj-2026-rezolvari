package com.pao.proiect.elearning.model;

import java.time.LocalDate;
import java.util.Objects;

public final class Certificat {
    private final int id;
    private final int studentId;
    private final int cursId;
    private final LocalDate dataEmitere;
    private final String codUnic;

    public Certificat(int id, int studentId, int cursId, LocalDate dataEmitere, String codUnic) {
        this.id = id;
        this.studentId = studentId;
        this.cursId = cursId;
        this.dataEmitere = dataEmitere;
        this.codUnic = Objects.requireNonNull(codUnic, "codUnic");
    }

    public int getId() {
        return id;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getCursId() {
        return cursId;
    }

    public LocalDate getDataEmitere() {
        return dataEmitere;
    }

    public String getCodUnic() {
        return codUnic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Certificat that = (Certificat) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Certificat{id=" + id + ", studentId=" + studentId + ", cursId=" + cursId
                + ", codUnic='" + codUnic + "'}";
    }
}
