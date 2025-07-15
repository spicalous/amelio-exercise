package com.amelio.amelio_exercise.unit.repository;

import com.amelio.amelio_exercise.listeners.StockPriceListener;
import com.amelio.amelio_exercise.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StockRepositoryTest {

   @Mock
   private StockPriceListener mockStockPriceListener;

   private StockRepository repository;

   @BeforeEach
   void setUp() {
      repository = new StockRepository(
         new HashMap<>(Map.of("stock-1", 10000L)),
         List.of(mockStockPriceListener));
   }

   @Test
   void whenGetStockPrice_thenReturnsPriceForSpecifiedStock() {
      Long actual = repository.getStockPrice("stock-1");

      assertEquals(10000L, actual);
   }

   @Test
   void whenGetStockPriceForNonExistingStock_thenReturnsNull() {
      Long actual = repository.getStockPrice("non-existing-stock");

      assertNull(actual);
   }

   @Test
   void whenUpdatingStockPrice_thenNewStockPriceIsReturned() {
      Long before = repository.getStockPrice("stock-1");
      assertEquals(10000L, before);

      repository.updateStockPrice("stock-1", 42L, Instant.ofEpochMilli(0));

      Long actual = repository.getStockPrice("stock-1");
      assertEquals(42L, actual);
   }

   @Test
   void whenUpdatingStockPrice_thenStockPriceListenersAreUpdated() {
      repository.updateStockPrice("stock-1", 42L, Instant.ofEpochMilli(0));

      verify(mockStockPriceListener).onStockPriceUpdate(eq("stock-1"), eq(42L), eq(Instant.ofEpochMilli(0)));
   }
}