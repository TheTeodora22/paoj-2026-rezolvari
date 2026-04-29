package com.pao.laboratory09.exercise3;

import com.pao.laboratory09.exercise1.TipTranzactie;
import com.pao.laboratory09.exercise2.Tranzactie;
import java.util.concurrent.atomic.AtomicInteger;

public class ATMThread extends Thread {
    private static final int TRANZACTII_PER_ATM = 4;
    private static final AtomicInteger NEXT_ID = new AtomicInteger(1);

    private final int atmId;
    private final CoadaTranzactii coadaTranzactii;

    public ATMThread(CoadaTranzactii coadaTranzactii, int atmId) {
        this.atmId = atmId;
        this.coadaTranzactii = coadaTranzactii;
    }

    @Override
    public void run() {
        for (int i = 0; i < TRANZACTII_PER_ATM; i++) {
            int idTranzactie = NEXT_ID.getAndIncrement();
            double suma = 100.0 * atmId + (i + 1) * 37.5;
            String data = String.format("2024-03-%02d", idTranzactie);
            TipTranzactie tip = (idTranzactie % 2 == 0) ? TipTranzactie.DEBIT : TipTranzactie.CREDIT;
            Tranzactie tranzactie = new Tranzactie(idTranzactie, suma, data, tip);

            System.out.printf(
                    "[ATM-%d] trimite: Tranzactie #%d %.2f RON%n",
                    atmId,
                    idTranzactie,
                    suma
            );

            try {
                coadaTranzactii.adauga(tranzactie);
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
 }
