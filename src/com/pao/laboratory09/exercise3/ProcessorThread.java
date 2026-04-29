package com.pao.laboratory09.exercise3;

import com.pao.laboratory09.exercise2.Tranzactie;

public class ProcessorThread implements Runnable {
    public volatile boolean activ = true;
    private final CoadaTranzactii coadaTranzactii;

    public ProcessorThread(CoadaTranzactii coadaTranzactii) {
        this.coadaTranzactii = coadaTranzactii;
    }

    @Override
    public void run() {
        while (activ) {
            try {
                Tranzactie tranzactie = coadaTranzactii.extrage();
                System.out.printf(
                        "[Processor] Factura #%d - %.2f RON | %s%n",
                        tranzactie.getId(),
                        tranzactie.getSuma(),
                        tranzactie.getData()
                );
                Thread.sleep(80);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void start() {
        new Thread(this).start();
    }
}
