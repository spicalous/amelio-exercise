package com.amelio.amelio_exercise.unit.service;

import com.amelio.amelio_exercise.service.SumStocksService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SumStocksServiceTest {

   private SumStocksService sumStocksService;

   @BeforeEach
   void setUp() {
      sumStocksService = new SumStocksService(3, 0);
   }

   @Test
   void givenNoPricesAreTracked_whenGetSum_thenTimeoutException() {
      assertThrows(TimeoutException.class, () -> sumStocksService.getSumStocks());
   }

   @Test
   void givenInCompleteSetOfStockPrices_whenGetSum_thenTimeoutException() {
      sumStocksService.onStockPriceUpdate("stock-1", 1L, Instant.ofEpochMilli(1));
      sumStocksService.onStockPriceUpdate("stock-2", 2L, Instant.ofEpochMilli(1));

      assertThrows(TimeoutException.class, () -> sumStocksService.getSumStocks());
   }

   @Test
   void givenCompleteSetOfStockPrices_whenGetSum_thenCorrectSumIsReturned() throws ExecutionException, InterruptedException, TimeoutException {
      sumStocksService.onStockPriceUpdate("stock-1", 1L, Instant.ofEpochMilli(1));
      sumStocksService.onStockPriceUpdate("stock-2", 2L, Instant.ofEpochMilli(1));
      sumStocksService.onStockPriceUpdate("stock-3", 3L, Instant.ofEpochMilli(1));

      assertEquals(6L, sumStocksService.getSumStocks());
   }

   @Test
   void givenCompleteSetOfStockPrices_whenNewTimestampPriceUpdates_thenCurrentPricesAreStillReturned() throws ExecutionException, InterruptedException, TimeoutException {
      sumStocksService.onStockPriceUpdate("stock-1", 1L, Instant.ofEpochMilli(1));
      sumStocksService.onStockPriceUpdate("stock-2", 2L, Instant.ofEpochMilli(1));
      sumStocksService.onStockPriceUpdate("stock-3", 3L, Instant.ofEpochMilli(1));
      sumStocksService.onStockPriceUpdate("stock-1", 4L, Instant.ofEpochMilli(2));
      sumStocksService.onStockPriceUpdate("stock-2", 5L, Instant.ofEpochMilli(2));

      assertEquals(6L, sumStocksService.getSumStocks());
   }

   @Test
   void givenSecondCompleteSetOfStockPrices_whenGetSum_thenSecondPricesAreReturned() throws ExecutionException, InterruptedException, TimeoutException {
      sumStocksService.onStockPriceUpdate("stock-1", 1L, Instant.ofEpochMilli(1));
      sumStocksService.onStockPriceUpdate("stock-2", 2L, Instant.ofEpochMilli(1));
      sumStocksService.onStockPriceUpdate("stock-3", 3L, Instant.ofEpochMilli(1));
      sumStocksService.onStockPriceUpdate("stock-1", 4L, Instant.ofEpochMilli(2));
      sumStocksService.onStockPriceUpdate("stock-2", 5L, Instant.ofEpochMilli(2));
      sumStocksService.onStockPriceUpdate("stock-3", 6L, Instant.ofEpochMilli(2));

      assertEquals(15L, sumStocksService.getSumStocks());
   }
}
