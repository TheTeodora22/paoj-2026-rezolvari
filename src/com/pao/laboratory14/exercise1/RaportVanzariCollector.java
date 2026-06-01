package com.pao.laboratory14.exercise1;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collector;

public final class RaportVanzariCollector {
    private RaportVanzariCollector() {
    }

    public static Collector<Bilet, ?, RaportVanzari> toRaport() {
        return Collector.of(
                Accumulator::new,
                Accumulator::add,
                Accumulator::combine,
                Accumulator::finish);
    }

    private static final class Accumulator {
        private final Map<TipBilet, double[]> data = new EnumMap<>(TipBilet.class);

        void add(Bilet bilet) {
            double[] stats = data.computeIfAbsent(bilet.getTip(), k -> new double[2]);
            stats[0]++;
            stats[1] += bilet.getPret();
        }

        Accumulator combine(Accumulator other) {
            for (Map.Entry<TipBilet, double[]> entry : other.data.entrySet()) {
                double[] stats = data.computeIfAbsent(entry.getKey(), k -> new double[2]);
                stats[0] += entry.getValue()[0];
                stats[1] += entry.getValue()[1];
            }
            return this;
        }

        RaportVanzari finish() {
            Map<TipBilet, Long> numarPerTip = new EnumMap<>(TipBilet.class);
            Map<TipBilet, Double> incasariPerTip = new EnumMap<>(TipBilet.class);
            double totalGlobal = 0;
            long totalBilete = 0;

            for (Map.Entry<TipBilet, double[]> entry : data.entrySet()) {
                long count = (long) entry.getValue()[0];
                double suma = entry.getValue()[1];
                numarPerTip.put(entry.getKey(), count);
                incasariPerTip.put(entry.getKey(), suma);
                totalGlobal += suma;
                totalBilete += count;
            }

            double medieGlobala = totalBilete > 0 ? totalGlobal / totalBilete : 0;

            long maxCount = numarPerTip.values().stream()
                    .mapToLong(Long::longValue)
                    .max()
                    .orElse(0L);
            TipBilet tipCelMaiPopular = Arrays.stream(TipBilet.values())
                    .filter(t -> numarPerTip.getOrDefault(t, 0L) == maxCount)
                    .findFirst()
                    .orElse(null);

            return new RaportVanzari(
                    Collections.unmodifiableMap(numarPerTip),
                    Collections.unmodifiableMap(incasariPerTip),
                    totalGlobal,
                    medieGlobala,
                    tipCelMaiPopular);
        }
    }
}
