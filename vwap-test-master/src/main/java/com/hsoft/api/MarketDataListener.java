package com.hsoft.api;

public interface MarketDataListener {

  /**
   * Invoked when a new transaction is reported
   *
   * @param productId Identifier of the product on which the transaction occurred
   * @param quantity  Quantity of the transaction
   * @param price     Price of the transaction
   */
  void transactionOccurred(String productId,
                           long quantity,
                           double price);
}