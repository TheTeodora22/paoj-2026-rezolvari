package com.pao.proiect.elearning.model;

public class Inscriere {
    private int id;
    private int studentId;
    private int cursId;
    private String dataInscriere;
    private boolean activ;

    public Inscriere() {
    }

    public Inscriere(int id, int studentId, int cursId, String dataInscriere, boolean activ) {
        this.id = id;
        this.studentId = studentId;
        this.cursId = cursId;
        this.dataInscriere = dataInscriere;
        this.activ = activ;
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

    public int getCursId() {
        return cursId;
    }

    public void setCursId(int cursId) {
        this.cursId = cursId;
    }

    public String getDataInscriere() {
        return dataInscriere;
    }

    public void setDataInscriere(String dataInscriere) {
        this.dataInscriere = dataInscriere;
    }

    public boolean isActiv() {
        return activ;
    }

    public void setActiv(boolean activ) {
        this.activ = activ;
    }

    @Override
    public String toString() {
        return "Inscriere{id=" + id + ", studentId=" + studentId + ", cursId=" + cursId
                + ", data='" + dataInscriere + "', activ=" + activ + "}";
    }
}
