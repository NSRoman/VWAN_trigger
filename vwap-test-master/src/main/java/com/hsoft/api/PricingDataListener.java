package com.hsoft.api;

public interface PricingDataListener {

  /**
   * Invoked when a new fair value is reported
   *
   * @param productId Identifier of the product for which the new fair value is provided
   * @param fairValue New fair price for the product
   */
  void fairValueChanged(String productId, double fairValue);
}