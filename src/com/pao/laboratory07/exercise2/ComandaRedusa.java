package com.pao.laboratory07.exercise2;

import com.pao.laboratory07.exercise1.OrderState;

public final class ComandaRedusa extends Comanda {
    private int discountProcent;

    public ComandaRedusa(String nume, double pret, int discountProcent) {
        super(nume, pret);
        this.discountProcent = discountProcent;
    }

    public ComandaRedusa(String nume, double pret, int discountProcent, String client) {
        super(nume, pret, client);
        this.discountProcent = discountProcent;
    }

    public int getDiscountProcent() {
        return discountProcent;
    }

    @Override
    public double pretFinal() {
        return pret * (1 - discountProcent / 100.0);
    }

    @Override
    public String descriere() {
        return "DISCOUNTED: " + nume + ", pret: " + String.format("%.2f", pretFinal())
                + " lei (-" + getDiscountProcent() + "%) ["  + stareInitiala + "]";
    }

    public String descriereCuClient()
    {
        return descriere() + "- client: " + client;
    }
}