package com.pao.laboratory07.exercise2;

import com.pao.laboratory07.exercise1.OrderState;

public abstract sealed class Comanda permits ComandaStandard, ComandaRedusa, ComandaGratuita {
    protected String nume;
    protected double pret;
    protected String client;
    protected OrderState stareInitiala;
    
    public Comanda(String nume, double pret) {
        this.nume = nume;
        this.pret = pret;
        this.client = null;
        this.stareInitiala = OrderState.PLACED;
    }

    public Comanda(String nume, double pret, String client) {
        this.nume = nume;
        this.pret = pret;
        this.client = client;
        this.stareInitiala = OrderState.PLACED;
    }


    public String getNume() {
        return nume;
    }

    public double getPret() {
        return pret;
    }

    public OrderState getStareInitiala() {
        return stareInitiala;
    }

    public String getClient() {
        return client;
    }

    public abstract double pretFinal();
    public abstract String descriere();
    public abstract String descriereCuClient();
}
