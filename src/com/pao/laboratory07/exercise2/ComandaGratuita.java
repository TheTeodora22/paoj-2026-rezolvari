package com.pao.laboratory07.exercise2;

import com.pao.laboratory07.exercise1.OrderState;

public final class ComandaGratuita extends Comanda {

    public ComandaGratuita(String nume) {
        super(nume, 0.0);
    }

    public ComandaGratuita(String nume, String client) {
        super(nume, 0.0, client);
    }

    @Override
    public double pretFinal() {
        return 0.0;
    }

    @Override
    public String descriere() {
        return "GIFT: " + nume + ", gratuit [" +  stareInitiala + "]";
    }

    public String descriereCuClient()
    {
        return descriere() + "- client: " + client;
    }
}