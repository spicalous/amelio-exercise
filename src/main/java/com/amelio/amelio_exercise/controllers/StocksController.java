package com.amelio.amelio_exercise.controllers;

import com.amelio.amelio_exercise.repository.StockRepository;
import com.amelio.amelio_exercise.service.PopularStockService;
import com.amelio.amelio_exercise.service.SumStocksService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StocksController {

   private final StockRepository stockRepository;
   private final PopularStockService popularStockService;
   private final SumStocksService sumStocksService;

   @GetMapping("/stocks/{name}")
   public Long getStockPrice(@PathVariable String name) {
      Long price = stockRepository.getStockPrice(name);
      if (price == null) {
         log.warn("Stock price not found for name={}", name);
         // we could also define a more application specific exception
         // and handle them using @ControllerAdvice / @ExceptionHandler
         throw new ResponseStatusException(NOT_FOUND, "Stock price not found");
      }
      popularStockService.registerQuery(name);
      return price;
   }

   @GetMapping("/popular-stocks")
   public List<String> getPopularStocks() {
      return popularStockService.getPopularStocks();
   }

   @GetMapping("/sum-stocks")
   public long getSumStocks() {
      try {
         return sumStocksService.getSumStocks();
      } catch (Exception e) {
         log.error("Exception summing stocks", e);
         return 0;
      }
   }
}
