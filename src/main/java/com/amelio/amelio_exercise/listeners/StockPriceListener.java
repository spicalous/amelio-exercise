package com.amelio.amelio_exercise.listeners;

import java.time.Instant;

public interface StockPriceListener {
   void onStockPriceUpdate(String name, long price, Instant updateTimestamp);
}
