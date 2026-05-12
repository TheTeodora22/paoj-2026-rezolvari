package com.pao.laboratory11.exercise3;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.*;     

public final class CustomCollectors {

    private CustomCollectors() {
    }

    public static Collector<Transaction, ?, Snapshot> toSnapshot(int topN) {
        return Collector.of(
                Agg::new,
                Agg::accumulate,
                Agg::combine,
                agg -> agg.finish(topN));
    }

    private static final class Agg {
        private final Map<String, Long> byCountry = new HashMap<>();
        private final Map<String, Long> byChannel = new HashMap<>();
        private BigDecimal total = BigDecimal.ZERO;
        private final List<Transaction> all = new ArrayList<>();

        void accumulate(Transaction tx) {
            byCountry.merge(tx.getCountry(), 1L, Long::sum);
            byChannel.merge(tx.getChannel(), 1L, Long::sum);
            total = total.add(tx.getAmount());
            all.add(tx);
        }

        static Agg combine(Agg left, Agg right) {
            mergeInto(left.byCountry, right.byCountry);
            mergeInto(left.byChannel, right.byChannel);
            left.total = left.total.add(right.total);
            left.all.addAll(right.all);
            return left;
        }

        Snapshot finish(int topN) {
            List<Transaction> top = all.stream()
                    .sorted(Comparator.comparing(Transaction::getAmount)
                            .reversed()
                            .thenComparingInt(Transaction::getId))
                    .limit(Math.max(0, topN))
                    .collect(Collectors.toList());
            return new Snapshot(byCountry, byChannel, total, top);
        }
    }

    private static void mergeInto(Map<String, Long> target, Map<String, Long> source) {
        source.forEach((k, v) -> target.merge(k, v, Long::sum));
    }
}
