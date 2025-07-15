package com.amelio.amelio_exercise.acceptance;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.OK;

public class PopularStocksTest extends AcceptanceTests {

   @Test
   void givenNoStocksQueried_thenAnyThreeStocksAreReturned() {
      ResponseEntity<List<String>> response = getPopularStocks();

      assertEquals(OK, response.getStatusCode());
      assertEquals(3, response.getBody().size());
   }

   @Test
   void givenThreeTopStocks_thenOnlyThreeTopStocksReturned() {
      queryNTimes("stock-3", 3);
      queryNTimes("stock-2", 2);
      queryNTimes("stock-1", 1);

      ResponseEntity<List<String>> response = getPopularStocks();

      assertEquals(List.of("stock-3", "stock-2", "stock-1"), response.getBody());
   }

   @Test
   void givenTwoTiedStocks_thenEitherIsReturned() {
      queryNTimes("stock-3", 3);
      queryNTimes("stock-2", 3);
      queryNTimes("stock-1", 1);

      ResponseEntity<List<String>> response = getPopularStocks();
      List<String> actual = response.getBody();

      // ordering is not guaranteed
      // assert list has 3 items
      assertEquals(3, actual.size());
      assertTrue(actual.contains("stock-3"));
      assertTrue(actual.contains("stock-2"));
      // assert third stock is "stock-1"
      assertEquals("stock-1", actual.get(2));
   }

   private void queryNTimes(String name, int times) {
      for (int i = 0; i < times; i++) {
         getStockPrice(name, Long.class);
      }
   }
}
