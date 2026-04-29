package com.pao.laboratory09.exercise2;

import com.pao.laboratory09.exercise1.TipTranzactie;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex2.bin";
    private static final int RECORD_SIZE = 32;
    private static final int STATUS_PENDING = 0;
    private static final int STATUS_PROCESSED = 1;
    private static final int STATUS_REJECTED = 2;

    public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește N din stdin, apoi cele N tranzacții (id suma data tip)
        // 2. Scrie toate înregistrările în OUTPUT_FILE cu DataOutputStream (format binar, RECORD_SIZE=32 bytes/înreg.)
        //    - bytes 0-3:   id (int, little-endian via ByteBuffer)
        //    - bytes 4-11:  suma (double, little-endian via ByteBuffer)
        //    - bytes 12-21: data (String, 10 chars ASCII, paddat cu spații la dreapta)
        //    - byte 22:     tip (0=CREDIT, 1=DEBIT)
        //    - byte 23:     status (0=PENDING, 1=PROCESSED, 2=REJECTED)
        //    - bytes 24-31: padding (zerouri)
        // 3. Procesează comenzile din stdin până la EOF cu RandomAccessFile:
        //    - READ idx       → seek(idx * RECORD_SIZE), citește și afișează înregistrarea
        //    - UPDATE idx ST  → seek(idx * RECORD_SIZE + 23), scrie noul status (0/1/2)
        //                       afișează "Updated [idx]: STATUS"
        //    - PRINT_ALL      → citește și afișează toate înregistrările
        //
        // Format linie output:
        //   [idx] id=<id> data=<data> tip=<CREDIT|DEBIT> suma=<suma:.2f> RON status=<STATUS>

        try (Scanner scanner = new Scanner(System.in)) {
            int N = scanner.nextInt();
            List<Tranzactie> tranzactii = new ArrayList<>();
            for (int i = 0; i < N; i++) {
                int id = scanner.nextInt();
                double suma = scanner.nextDouble();
                String data = scanner.next();
                String tip = scanner.next();
                TipTranzactie tipTranzactie = TipTranzactie.valueOf(tip);
                tranzactii.add(new Tranzactie(id, suma, data, tipTranzactie, STATUS_PENDING));
            }

            writeRecords(tranzactii);

            try (RandomAccessFile raf = new RandomAccessFile(OUTPUT_FILE, "rw")) {
                while (scanner.hasNext()) {
                    String command = scanner.next();
                    if ("READ".equals(command)) {
                        int idx = scanner.nextInt();
                        printRecord(raf, idx);
                    } else if ("UPDATE".equals(command)) {
                        int idx = scanner.nextInt();
                        String statusToken = scanner.next();
                        int status = parseStatus(statusToken);
                        raf.seek((long) idx * RECORD_SIZE + 23);
                        raf.writeByte(status);
                        System.out.printf("Updated [%d]: %s%n", idx, formatStatus(status));
                    } else if ("PRINT_ALL".equals(command)) {
                        for (int i = 0; i < N; i++) {
                            printRecord(raf, i);
                        }
                    }
                }
            }
        }
    }

    private static void writeRecords(List<Tranzactie> tranzactii) throws IOException {
        File outDir = new File("output");
        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(OUTPUT_FILE))) {
            for (Tranzactie tranzactie : tranzactii) {
                byte[] record = new byte[RECORD_SIZE];

                ByteBuffer.wrap(record, 0, 4).order(ByteOrder.LITTLE_ENDIAN).putInt(tranzactie.getId());
                ByteBuffer.wrap(record, 4, 8).order(ByteOrder.LITTLE_ENDIAN).putDouble(tranzactie.getSuma());

                byte[] dataBytes = tranzactie.getData().getBytes();
                int dataLen = Math.min(10, dataBytes.length);
                System.arraycopy(dataBytes, 0, record, 12, dataLen);
                for (int i = 12 + dataLen; i < 22; i++) {
                    record[i] = (byte) ' ';
                }

                record[22] = (byte) (tranzactie.getTip() == TipTranzactie.CREDIT ? 0 : 1);
                record[23] = (byte) tranzactie.getStatus();

                dos.write(record);
            }
        }
    }

    private static void printRecord(RandomAccessFile raf, int idx) throws IOException {
        byte[] buffer = new byte[RECORD_SIZE];
        raf.seek((long) idx * RECORD_SIZE);
        raf.readFully(buffer);

        int id = ByteBuffer.wrap(buffer, 0, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
        double suma = ByteBuffer.wrap(buffer, 4, 8).order(ByteOrder.LITTLE_ENDIAN).getDouble();
        String data = new String(buffer, 12, 10).trim();
        TipTranzactie tip = buffer[22] == 0 ? TipTranzactie.CREDIT : TipTranzactie.DEBIT;
        int status = Byte.toUnsignedInt(buffer[23]);

        System.out.printf(
                Locale.US,
                "[%d] id=%d data=%s tip=%s suma=%.2f RON status=%s%n",
                idx,
                id,
                data,
                tip,
                suma,
                formatStatus(status)
        );
    }

    private static int parseStatus(String token) {
        if ("0".equals(token) || "PENDING".equals(token)) {
            return STATUS_PENDING;
        }
        if ("1".equals(token) || "PROCESSED".equals(token)) {
            return STATUS_PROCESSED;
        }
        if ("2".equals(token) || "REJECTED".equals(token)) {
            return STATUS_REJECTED;
        }
        throw new IllegalArgumentException("Unknown status: " + token);
    }

    private static String formatStatus(int status) {
        switch (status) {
            case STATUS_PENDING:
                return "PENDING";
            case STATUS_PROCESSED:
                return "PROCESSED";
            case STATUS_REJECTED:
                return "REJECTED";
            default:
                return "UNKNOWN";
        }
    }
}
