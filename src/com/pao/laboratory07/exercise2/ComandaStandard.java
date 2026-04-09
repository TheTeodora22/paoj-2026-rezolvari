package com.pao.laboratory07.exercise2;

import com.pao.laboratory07.exercise1.OrderState;

public final class ComandaStandard extends Comanda {
   

    public ComandaStandard(String nume, double pret) {
        super(nume, pret);
    }

    public ComandaStandard(String nume, double pret, String client) {
        super(nume, pret, client);
    }

    @Override
    public double pretFinal() {
        return pret;
    }

    @Override
    public String descriere() {
        return "STANDARD: " + nume + ", pret: " + String.format("%.2f", pretFinal()) + " lei [" + stareInitiala  + "]";
    }

    public String descriereCuStare()
    {
        return descriere() + "- client: " + client;
    }

    public String descriereCuClient()
    {
        return descriere() + "- client: " + client;
    }
}
