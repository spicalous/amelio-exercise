package com.amelio.amelio_exercise.unit.service;

import com.amelio.amelio_exercise.service.PopularStockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class PopularStockServiceTest {

   private PopularStockService popularStockService;

   @BeforeEach
   void setUp() {
      popularStockService = new PopularStockService(new ConcurrentHashMap<>(Map.of(
         "stock-1", 0L,
         "stock-2", 0L,
         "stock-3", 0L,
         "stock-4", 0L
      )));
   }

   @Test
   void givenNoStocksHaveBeenQueried_thenReturnsAnyThreeStocks() {
      List<String> actual = popularStockService.getPopularStocks();

      assertEquals(3, actual.size());
   }

   @Test
   void givenThreeTopStocks_thenOnlyThreeTopStocksReturned() {
      registerQueryNTimes("stock-3", 3);
      registerQueryNTimes("stock-2", 2);
      registerQueryNTimes("stock-1", 1);

      List<String> actual = popularStockService.getPopularStocks();

      assertEquals(List.of("stock-3", "stock-2", "stock-1"), actual);
   }

   @Test
   void givenTwoTiedStocks_thenEitherIsReturned() {
      registerQueryNTimes("stock-3", 3);
      registerQueryNTimes("stock-2", 3);
      registerQueryNTimes("stock-1", 1);

      List<String> actual = popularStockService.getPopularStocks();

      // ordering is not guaranteed
      // assert list has 3 items
      assertEquals(3, actual.size());
      assertTrue(actual.contains("stock-3"));
      assertTrue(actual.contains("stock-2"));
      // assert third stock is "stock-1"
      assertEquals("stock-1", actual.get(2));
   }

   private void registerQueryNTimes(String name, int times) {
      for (int i = 0; i < times; i++) {
         popularStockService.registerQuery(name);
      }
   }
}