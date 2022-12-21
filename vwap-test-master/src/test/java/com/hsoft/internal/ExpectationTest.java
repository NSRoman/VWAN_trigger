package com.hsoft.internal;

import com.hsoft.api.VwapTriggerListener;
import com.hsoft.practice.VwapTrigger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import static com.hsoft.internal.B.T;
import static org.junit.jupiter.api.Assertions.*;

/**
 * !! internal only !!
 * <p>
 * Candidate doesn't have to care about this file. It must not be modified. It is used to validate the solution.
 */
public class ExpectationTest {

  private VwapTrigger vwapTrigger;
  private S l;

  @BeforeEach
  void s() {
    l = new S();
    vwapTrigger = new VwapTrigger(l);
  }

  @Test
  @Timeout(value = 1, unit = TimeUnit.MINUTES, threadMode = Timeout.ThreadMode.SAME_THREAD)
  public void singleThreaded() {
    a(1);
  }

  @RepeatedTest(5)
  @Timeout(value = 1, unit = TimeUnit.MINUTES, threadMode = Timeout.ThreadMode.SAME_THREAD)
  public void multiThreaded() {
    a(4);
  }

  private void a(int n) {
    C p = com.hsoft.internal.D.m(n);
    p.a(vwapTrigger);
    p.b(vwapTrigger);
    p.c();

    m(100.0, 100.04761904761905);
    m(101.0, 101.09433962264151);
    m(101.5, 101.5909090909091);
    m(101.0, 101.5909090909091);
    assertFalse(l.h(), () -> "no more event expected" + l.n());
  }

  private static final double EPSILON = 0.00001;

  private void m(double theo, double vwap) {
    assertTrue(l.h(), "expected more event");
    S.D data = l.n();
    assertEquals(T, data.s, "incorrect productId received");
    assertEquals(theo, data.d1, EPSILON, "incorrect theo received");
    assertEquals(vwap, data.d2, EPSILON, "incorrect vwap received");
  }


  private static class S implements VwapTriggerListener {

    private final Queue<D> ds = new ConcurrentLinkedQueue<>();

    @Override
    public void vwapTriggered(String productId, double vwap, double fairValue) {
      if (T.equals(productId))
        ds.offer(new D(productId, fairValue, vwap));
    }

    public D n() {
      return ds.remove();
    }

    public boolean h() {
      return !ds.isEmpty();
    }

    protected static class D {
      public final String s;
      public final double d1;
      public final double d2;

      public D(String s, double d1, double d2) {
        this.s = s;
        this.d1 = d1;
        this.d2 = d2;
      }
    }
  }
}
