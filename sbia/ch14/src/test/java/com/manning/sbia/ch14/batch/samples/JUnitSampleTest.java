/**
 *
 */
package com.manning.sbia.ch14.batch.samples;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Sample unit.
 *
 * @author bazoud
 *
 */
public class JUnitSampleTest {
  @Before
  public void setUp() {
    // initialize something
  }

  @After
  public void tearDown() {
    // do something
  }

  @Test
  public void testMethod1() {
    // do something
    assertTrue(true);
  }

  @Test
  public void testMethod2() {
    // do something
    assertNotNull("string");
  }
}
