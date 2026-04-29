package com.pao.laboratory09.exercise3;

public class Main {
    public static void main(String[] args) {
        CoadaTranzactii coadaTranzactii = new CoadaTranzactii();
        ATMThread atm1 = new ATMThread(coadaTranzactii, 1);
        ATMThread atm2 = new ATMThread(coadaTranzactii, 2);
        ATMThread atm3 = new ATMThread(coadaTranzactii, 3);
        ProcessorThread processorThread = new ProcessorThread(coadaTranzactii);
        Thread firProcessor = new Thread(processorThread);

        atm1.start();
        atm2.start();
        atm3.start();
        firProcessor.start();

        try {
            atm1.join();
            atm2.join();
            atm3.join();

            processorThread.activ = false;

            synchronized (coadaTranzactii) {
                coadaTranzactii.notifyAll();
            }

            firProcessor.interrupt();
            firProcessor.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Toate tranzactiile procesate. Total: 12");
    }
}
