package com.amelio.amelio_exercise.acceptance;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

public class StockPriceTest extends AcceptanceTests {

   @Test
   void givenStockPriceAndValidStockName_thenStockPriceIsReturned() {
      givenStockPrice("stock-1", 42L);

      ResponseEntity<Long> response = getStockPrice("stock-1", Long.class);

      assertEquals(OK, response.getStatusCode());
      assertEquals(42L, response.getBody());
   }

   @Test
   void givenUnknownStockName_then404() {
      ResponseEntity<String> response = getStockPrice("unknown-stock", String.class);
      assertEquals(NOT_FOUND, response.getStatusCode());
   }
}
