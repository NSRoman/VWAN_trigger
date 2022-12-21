package com.hsoft.internal;

import com.hsoft.api.MarketDataListener;
import com.hsoft.api.PricingDataListener;

/**
 * !! internal only !!
 * <p>
 * Candidate doesn't have to care about this file. It is as an external used to provide the features needed for the practice
 */
public interface C {
  void a(MarketDataListener var1);

  void b(PricingDataListener var1);

  void c();
}
