package com.amelio.amelio_exercise.repository;

import com.amelio.amelio_exercise.listeners.StockPriceListener;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class StockRepository {

   private final Map<String, Long> stockPriceByName;
   private final List<StockPriceListener> stockPriceListeners;

   public Long getStockPrice(String name) {
      return stockPriceByName.get(name);
   }

   public void updateStockPrice(String name, long newPrice, Instant updateTime) {
      stockPriceByName.put(name, newPrice);
      stockPriceListeners.forEach(listener -> listener.onStockPriceUpdate(name, newPrice, updateTime));
   }
}
