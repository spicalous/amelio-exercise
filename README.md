# Amelio take home exercise

## Tasks

> Have a backend that spins up and initialises those 10 tickers at 1000 pounds each.

- To keep it simple, I have defined a Map of stock names to prices. 
- `StockRepository` is responsible for read/writing of stocks data.

> Publish a HTTP endpoint that allows us to query the current value of a given stock, e.g. "/stocks/stock-1" would return 1000 when the server just started, and probably a different value one second later.
- Created `/stocks/{name}` endpoint that delegates to stock repository.
- Returns `404` if stock name does not exist

> Publish a second HTTP endpoint that allows us to query the names of the three more popular stocks. Stock popularity is set by the amount of times each ticker value has been queried using the previous endpoint, since our server started. In case of a tie you can arbitrarily return any of the tied stock names. We can publish this under a "/popular-stocks" endpoint. 
- Created `PopularStockService` that would keep track of queried stocks
- Updated `/stocks/{name}` to register a query by stock name
- Added `/popular-stocks` to return top 3 stocks

> Lastly, we'd like to have a third endpoint that returns the sum of the current value of all stocks. This is a last minute requirement by our scientists; they'll need it in order to debug their formula. Their only request is that this endpoint always returns a stable sum of values for all stocks in the same second; i.e. the sum of stock values should take into account the sum of values of all stocks for the latest second when they are all available. In other words, it shouldn't mix stock values from the previous calculation iteration and the current one. We can publish this as "/sum-stocks".
- Created `StockPriceService` which spawns a thread for each stock and publishes a stock price every second using supplied stock price function
- Created `SumStocksService` which keeps track of the latest set of stock prices
- Amended the `StockRepository` to publish update of prices to the `SumStocksService`

## Additional notes

### Acceptance tests

- Spin up the application with the `ACCEPTANCE` spring profile.
- Perform contract based testing and test from the "users" perspective i.e. capturing business requirements.
- 100% App testing and deployment MUST be automated within pipeline to production.
