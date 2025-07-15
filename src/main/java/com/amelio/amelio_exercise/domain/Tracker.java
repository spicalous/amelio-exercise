package com.amelio.amelio_exercise.domain;

import lombok.AllArgsConstructor;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@AllArgsConstructor
public class Tracker {

   private final CompletableFuture<Long> future;
   private final int expectedStockCount;
   private ConcurrentHashMap<String, Long> prices;
   private long currentTrackedEpochSecond;

   public static Tracker build(int expectedStockCount) {
      return new Tracker(new CompletableFuture<>(), expectedStockCount, new ConcurrentHashMap<>(expectedStockCount), 0L);
   }

   public void track(String name, long price, Instant updateTimestamp) {
      // new epoch second, start waiting for 10 new updates
      if (currentTrackedEpochSecond < updateTimestamp.getEpochSecond()) {
         currentTrackedEpochSecond = updateTimestamp.getEpochSecond();
         prices = new ConcurrentHashMap<>();
      }
      if (currentTrackedEpochSecond == updateTimestamp.getEpochSecond()) {
         prices.put(name, price);
         if (prices.size() == expectedStockCount) {
            long sum = prices.values().stream().mapToLong(Long::longValue).sum();
            future.complete(sum);
         }
      }
   }

   public boolean isComplete() {
      return future.isDone();
   }

   public long getSum(int timeout, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
      return future.get(timeout, timeUnit);
   }
}
