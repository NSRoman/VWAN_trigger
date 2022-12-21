package com.hsoft.internal;

import com.hsoft.api.MarketDataListener;
import com.hsoft.api.PricingDataListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

import static com.hsoft.internal.A.M;
import static com.hsoft.internal.A.P;

/**
 * !! internal only !!
 * <p>
 * Candidate doesn't have to care about this file. It is as an external used to provide the features needed for the practice
 */
public class B implements C {

  private static final Logger log = LogManager.getLogger(B.class);
  public static final String T = "TEST_PRODUCT";

  static {
    log.info("Java: " + System.getProperty("java.runtime.version"));
  }

  private final Collection<MarketDataListener> l1 = new CopyOnWriteArrayList<>();
  private final Collection<PricingDataListener> l2 = new CopyOnWriteArrayList<>();
  private final int n;

  private boolean f = false;
  private final List<D> dd = new ArrayList<>();

  public B(int n) {
    this.n = n;
  }

  public void a(MarketDataListener listener) {
    this.l1.add(listener);
  }

  public void b(PricingDataListener listener) {
    this.l2.add(listener);
  }

  public void c() {
    this.dd.add(new D(P, T, -1L, 100.0));
    this.dd.add(new D(M, T, 1000L, 99.0));
    this.dd.add(new D(M, T, 500L, 101.0));
    this.dd.add(new D(M, T, 600L, 101.0));
    this.dd.add(new D(P, T, -1L, 101.0));
    this.dd.add(new D(M, T, 700L, 101.0));
    this.dd.add(new D(M, T, 2500L, 102.0));
    this.dd.add(new D(P, T, -1L, 101.5));
    this.dd.add(new D(M, T, 100L, 102.0));
    this.dd.add(new D(P, T, -1L, 102.0));
    this.dd.add(new D(P, T, -1L, 101.0));
    log.info("Starting to publish data");
    ExecutorService executor = Executors.newFixedThreadPool(n);
    List<Future<?>> futures = new ArrayList<>();

    for (int i = 0; i < n; ++i) {
      futures.add(executor.submit(new R()));
    }

    for (Future<?> value : futures) {
      try {
        value.get();
      } catch (InterruptedException | ExecutionException e) {
        throw new RuntimeException(e);
      }
    }

    log.info("All data were published");
    executor.shutdown();
  }

  private void m(String productId, long quantity, double price) {
    this.l1.forEach(l -> l.transactionOccurred(productId, quantity, price));
  }

  private void p(String productId, double price) {
    this.l2.forEach(l -> l.fairValueChanged(productId, price));
  }

  private class R implements Runnable {
    public void run() {
      while (true) {
        D d;
        synchronized (B.this.dd) {
          if (B.this.dd.isEmpty()) {
            return;
          }

          if (Math.random() < 0.0001 && !B.this.f) {
            d = B.this.dd.remove(0);
            B.this.f = true;
          } else {
            String productId = "product_" + Math.round(Math.random() * 100.0);
            A type = Math.random() < 0.75 ? M : P;
            long quantity = Math.round(Math.random() * 10000.0);
            double price = 100.0 + (Math.random() - 0.5) * 5.0;
            d = new D(type, productId, quantity, price);
          }
        }

        if (d.a == M) {
          B.this.m(d.s, d.l, d.d);
        } else {
          B.this.p(d.s, d.d);
        }

        synchronized (B.this.dd) {
          B.this.f = false;
        }
      }
    }
  }

  private static final class D {
    private final A a;
    private final String s;
    private final long l;
    private final double d;

    private D(A a, String s, long l, double d) {
      this.a = a;
      this.s = s;
      this.l = l;
      this.d = d;
    }
  }
}
