package com.amelio.amelio_exercise.unit.domain;

import com.amelio.amelio_exercise.domain.Tracker;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

class TrackerTest {

   @Test
   void givenNewlyCreatedTracker_thenIsCompleteIsFalse() {
      Tracker tracker = Tracker.build(3);
      assertFalse(tracker.isComplete());
   }

   @Test
   void givenTwoOfThreeStockUpdatesWithSameTimestamp_thenTrackerIsNotComplete() {
      Tracker tracker = Tracker.build(3);

      tracker.track("stock-1", 1L, Instant.ofEpochSecond(1));
      tracker.track("stock-2", 2L, Instant.ofEpochSecond(1));

      assertFalse(tracker.isComplete());
   }

   @Test
   void givenCompleteStockUpdatesWithSameTimestamp_thenTrackerIsComplete() {
      Tracker tracker = Tracker.build(3);

      tracker.track("stock-1", 1L, Instant.ofEpochSecond(1));
      tracker.track("stock-2", 2L, Instant.ofEpochSecond(1));
      tracker.track("stock-3", 3L, Instant.ofEpochSecond(1));

      assertTrue(tracker.isComplete());
   }

   @Test
   void givenCompleteStockUpdatesWithSameTimestamp_thenSumStocksReturnsSumOfPrices() throws ExecutionException, InterruptedException, TimeoutException {
      Tracker tracker = Tracker.build(3);

      tracker.track("stock-1", 1L, Instant.ofEpochSecond(1));
      tracker.track("stock-2", 2L, Instant.ofEpochSecond(1));
      tracker.track("stock-3", 3L, Instant.ofEpochSecond(1));

      assertEquals(6L, tracker.getSum(0, TimeUnit.MILLISECONDS));
   }

   @Test
   void givenIncompleteStockPriceForTimestampOne_whenNewTimestampTracked_thenTrackerIsNotComplete() {
      Tracker tracker = Tracker.build(3);

      tracker.track("stock-1", 1L, Instant.ofEpochSecond(1));
      tracker.track("stock-2", 2L, Instant.ofEpochSecond(1));
      tracker.track("stock-3", 3L, Instant.ofEpochSecond(2));

      assertFalse(tracker.isComplete());
   }

   @Test
   void givenIncompleteStockPriceForTimestampOne_whenNewTimestampTrackedAndCompleted_thenTrackerIsComplete() throws ExecutionException, InterruptedException, TimeoutException {
      Tracker tracker = Tracker.build(3);

      tracker.track("stock-1", 1L, Instant.ofEpochSecond(1)); // discarded
      tracker.track("stock-2", 2L, Instant.ofEpochSecond(1)); // discarded
      tracker.track("stock-3", 3L, Instant.ofEpochSecond(2));
      tracker.track("stock-2", 4L, Instant.ofEpochSecond(2));
      tracker.track("stock-1", 5L, Instant.ofEpochSecond(2));

      assertTrue(tracker.isComplete());
      assertEquals(12L, tracker.getSum(0, TimeUnit.MILLISECONDS));
   }
}