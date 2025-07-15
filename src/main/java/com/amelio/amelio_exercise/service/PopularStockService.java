package com.amelio.amelio_exercise.service;

import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;

@RequiredArgsConstructor
public class PopularStockService {

   private static final Comparator<Entry<String, Long>> VALUE_COUNT_COMPARATOR = (c1, c2) -> {
      if (c1.getValue().longValue() == c2.getValue().longValue()) {
         return 0;
      }
      return c1.getValue() < c2.getValue() ? 1 : -1;
   };

   private final ConcurrentMap<String, Long> popularStockTracker;

   public void registerQuery(String stockName) {
      popularStockTracker.compute(stockName, (key, existing) -> existing == null ? 1L : existing + 1);
   }

   public List<String> getPopularStocks() {
      return popularStockTracker.entrySet().stream()
         .sorted(VALUE_COUNT_COMPARATOR)
         .limit(3)
         .map(Entry::getKey)
         .toList();
   }
}
