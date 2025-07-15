package com.amelio.amelio_exercise.acceptance;

import com.amelio.amelio_exercise.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.List;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@ActiveProfiles("ACCEPTANCE")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AcceptanceTests {

   private static final ParameterizedTypeReference<List<String>> POPULAR_STOCKS_RESPONSE_TYPE
      = new ParameterizedTypeReference<>() {};

   @Autowired
   protected TestRestTemplate testRestTemplate;

   @Autowired
   private StockRepository stockRepository;

   protected void givenStockPrice(String name, long price) {
      givenStockPrice(name, price, 0);
   }

   protected void givenStockPrice(String name, long price, long epochSecond) {
      stockRepository.updateStockPrice(name, price, Instant.ofEpochSecond(epochSecond));
   }

   protected <U> ResponseEntity<U> getStockPrice(String stock, Class<U> responseType) {
      return testRestTemplate.exchange(
         RequestEntity.get("/stocks/" + stock)
            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .build(),
         responseType);
   }

   protected ResponseEntity<List<String>> getPopularStocks() {
      return testRestTemplate.exchange(
         RequestEntity.get("/popular-stocks")
            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .build(),
         POPULAR_STOCKS_RESPONSE_TYPE);
   }

   protected ResponseEntity<Long> getSumStocks() {
      return testRestTemplate.exchange(
         RequestEntity.get("/sum-stocks")
            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .build(),
         Long.class);
   }
}
