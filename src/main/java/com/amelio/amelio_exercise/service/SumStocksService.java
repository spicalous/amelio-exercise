package com.amelio.amelio_exercise.service;

import com.amelio.amelio_exercise.domain.Tracker;
import com.amelio.amelio_exercise.listeners.StockPriceListener;

import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SumStocksService implements StockPriceListener {

   private final int expectedStockCount;
   private final int getSumTimeoutSeconds;
   private Tracker previousPriceTracker;
   private Tracker currentPriceTracker;

   public SumStocksService(int expectedStockCount, int getSumTimeoutSeconds) {
      this.expectedStockCount = expectedStockCount;
      this.getSumTimeoutSeconds = getSumTimeoutSeconds;
      this.previousPriceTracker = Tracker.build(expectedStockCount);
      this.currentPriceTracker = Tracker.build(expectedStockCount);
   }

   @Override
   public void onStockPriceUpdate(String name, long price, Instant updateTimestamp) {
      if (!previousPriceTracker.isComplete()) {
         previousPriceTracker.track(name, price, updateTimestamp);
      } else {
         currentPriceTracker.track(name, price, updateTimestamp);
         if (currentPriceTracker.isComplete()) {
            previousPriceTracker = currentPriceTracker;
            currentPriceTracker = Tracker.build(expectedStockCount);
         }
      }
   }

   public long getSumStocks() throws ExecutionException, InterruptedException, TimeoutException {
      return previousPriceTracker.getSum(getSumTimeoutSeconds, TimeUnit.SECONDS);
   }
}
