package com.pao.laboratory09.exercise2;

import com.pao.laboratory09.exercise1.TipTranzactie;

public class Tranzactie {
    private int id;
    private double suma;
    private String data;
    private TipTranzactie tip;
    private int status;
    public Tranzactie(int id, double suma, String data, TipTranzactie tip) {
        this.id = id;
        this.suma = suma;
        this.data = data;
        this.tip = tip;
    }

    public Tranzactie(int id, double suma, String data, TipTranzactie tip, int status) {
        this.id = id;
        this.suma = suma;
        this.data = data;
        this.tip = tip;
        this.status = status;
    }
    public int getId() {
        return id;
    }
    public double getSuma() {
        return suma;
    }
    public String getData() {
        return data;
    }
    public TipTranzactie getTip() {
        return tip;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    @Override
    public String toString() {
        return String.format("%d %f %s %s %d", id, suma, data, tip);
    }
}
