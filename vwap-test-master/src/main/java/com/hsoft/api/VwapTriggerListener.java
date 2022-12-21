package com.hsoft.api;

public interface VwapTriggerListener {

  /**
   * Invoked when current VWAP value is greater than current fair price
   *
   * @param productId Identifier of the product for which the new fair value is provided
   * @param vwap      VWAP value for the product
   * @param fairValue Fair price for the product
   */
  void vwapTriggered(String productId, double vwap, double fairValue);

}
