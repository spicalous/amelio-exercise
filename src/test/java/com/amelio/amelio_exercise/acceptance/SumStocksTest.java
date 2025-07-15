package com.amelio.amelio_exercise.acceptance;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.OK;

public class SumStocksTest extends AcceptanceTests {

   @Test
   void givenNoPricesAreTracked_thenDefaultValueIsReturned() {
      ResponseEntity<Long> response = getSumStocks();

      assertEquals(OK, response.getStatusCode());
      assertEquals(0, response.getBody());
   }

   @Test
   @DirtiesContext
   void givenCompleteSetOfPrices_thenSumIsReturned() {
      givenStockPrice("stock-1", 1);
      givenStockPrice("stock-2", 2);
      givenStockPrice("stock-3", 3);
      givenStockPrice("stock-4", 4);
      givenStockPrice("stock-5", 5);
      givenStockPrice("stock-6", 6);
      givenStockPrice("stock-7", 7);
      givenStockPrice("stock-8", 8);
      givenStockPrice("stock-9", 9);
      givenStockPrice("stock-10", 10);

      ResponseEntity<Long> response = getSumStocks();

      assertEquals(OK, response.getStatusCode());
      assertEquals(55, response.getBody());
   }

   @Test
   @DirtiesContext
   void givenInCompleteSetOfPrices_whenNewSetOfPrices_thenNewSumIsReturned() {
      // old timestamp
      givenStockPrice("stock-1", 1, 0);
      givenStockPrice("stock-2", 2, 0);
      givenStockPrice("stock-3", 3, 0);
      givenStockPrice("stock-4", 4, 0);
      givenStockPrice("stock-5", 5, 0);
      // new timestamp

      givenStockPrice("stock-1", 10, 1);
      givenStockPrice("stock-2", 20, 1);
      givenStockPrice("stock-3", 30, 1);
      givenStockPrice("stock-4", 40, 1);
      givenStockPrice("stock-5", 50, 1);
      givenStockPrice("stock-6", 60, 1);
      givenStockPrice("stock-7", 70, 1);
      givenStockPrice("stock-8", 80, 1);
      givenStockPrice("stock-9", 90, 1);
      givenStockPrice("stock-10", 100, 1);

      ResponseEntity<Long> response = getSumStocks();

      assertEquals(OK, response.getStatusCode());
      assertEquals(550, response.getBody());
   }
}
