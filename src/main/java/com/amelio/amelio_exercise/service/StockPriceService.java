package com.amelio.amelio_exercise.service;

import com.amelio.amelio_exercise.repository.StockRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.time.Clock;
import java.time.Instant;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.max;
import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
public class StockPriceService {

   private static final int INITIAL_DELAY_SECONDS = 0;
   private static final int PERIOD_SECONDS = 1;

   public StockPriceService(Set<String> stockNames,
                            StockRepository stockRepository,
                            ScheduledExecutorService executorService) {
      // schedule an update every second for each stock
      stockNames.forEach(stockName -> executorService.scheduleAtFixedRate(() -> {
         Long currentPrice = stockRepository.getStockPrice(stockName);
         long nextPrice = getNextStockPrice(currentPrice);
         stockRepository.updateStockPrice(stockName, nextPrice, Instant.now(Clock.systemUTC()));
      }, INITIAL_DELAY_SECONDS, PERIOD_SECONDS, SECONDS));
   }

   @SneakyThrows
   private long getNextStockPrice(long value) {
      Thread.sleep(800);
      return max(1, value + ThreadLocalRandom.current().nextInt(-10, 11));
   }
}
