package com.amelio.amelio_exercise.configuration;

import com.amelio.amelio_exercise.repository.StockRepository;
import com.amelio.amelio_exercise.service.PopularStockService;
import com.amelio.amelio_exercise.service.StockPriceService;
import com.amelio.amelio_exercise.service.SumStocksService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

@Configuration(proxyBeanMethods = false)
public class ApplicationConfiguration {

   @Bean
   ConcurrentMap<String, Long> stocks() {
      return new ConcurrentHashMap<>(Map.of(
         "stock-1", 10000L,
         "stock-2", 10000L,
         "stock-3", 10000L,
         "stock-4", 10000L,
         "stock-5", 10000L,
         "stock-6", 10000L,
         "stock-7", 10000L,
         "stock-8", 10000L,
         "stock-9", 10000L,
         "stock-10", 10000L
      ));
   }

   @Bean
   SumStocksService sumStocksService(ConcurrentMap<String, Long> stocks,
                                     @Value("${get-sum-timeout-seconds}") int getSumTimeoutSeconds) {
      return new SumStocksService(stocks.size(), getSumTimeoutSeconds);
   }

   @Bean
   StockRepository stockRepository(ConcurrentMap<String, Long> stocks,
                                   SumStocksService sumStocksService) {
      return new StockRepository(stocks, List.of(sumStocksService));
   }

   @Bean
   PopularStockService popularStockService(ConcurrentMap<String, Long> stocks) {
      Map<String, Long> initialState = stocks.keySet().stream().collect(toMap(Function.identity(), (name) -> 0L));
      return new PopularStockService(new ConcurrentHashMap<>(initialState));
   }

   @Bean
   @Profile("!ACCEPTANCE") // For more deterministic acceptance tests, we publish our own price updates
   StockPriceService stockPriceService(ConcurrentMap<String, Long> stocks,
                                       StockRepository stockRepository) {
      return new StockPriceService(stocks.keySet(), stockRepository, Executors.newScheduledThreadPool(stocks.size()));
   }
}
