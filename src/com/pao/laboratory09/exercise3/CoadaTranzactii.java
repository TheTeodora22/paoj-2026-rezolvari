package com.pao.laboratory09.exercise3;

import com.pao.laboratory09.exercise2.Tranzactie;
import java.util.ArrayDeque;
import java.util.Deque;

public class CoadaTranzactii {
    private static final int CAPACITATE_MAXIMA = 5;
    private final Deque<Tranzactie> tranzactii;

    public CoadaTranzactii() {
        this.tranzactii = new ArrayDeque<>();
    }

    public synchronized void adauga(Tranzactie tranzactie) throws InterruptedException {
        while (tranzactii.size() >= CAPACITATE_MAXIMA) {
            wait();
        }
        tranzactii.addLast(tranzactie);
        notifyAll();
    }

    public synchronized Tranzactie extrage() throws InterruptedException {
        while (tranzactii.isEmpty()) {
            wait();
        }
        Tranzactie tranzactie = tranzactii.removeFirst();
        notifyAll();
        return tranzactie;
    }
    public synchronized int size() {
        return tranzactii.size();
    }
}
