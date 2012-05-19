/**
 *
 */
package com.manning.sbia.ch14.batch.samples;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;

/**
 * @author bazoud
 *
 */
public class MockitoSampleTest {

  @SuppressWarnings("unchecked")
  @Test
  public void mockInterface() {
    // mock an interface
    List<String> mockedList = mock(List.class);
    mockedList.add("one");
    mockedList.clear();
    verify(mockedList, times(1)).add("one");
    verify(mockedList, times(1)).clear();
    verifyNoMoreInteractions(mockedList);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void mockConcreteClass() {
    // mock a concrete class
    LinkedList<String> mockedList = mock(LinkedList.class);
    when(mockedList.get(0)).thenReturn("first");
    assertEquals("first", mockedList.get(0));
  }

  @Test
  public void spy() {
    List<String> list = new LinkedList<String>();
    List<String> spy = Mockito.spy(list);
    spy.add("one");
    spy.add("two");
    verify(spy).add("one");
    verify(spy).add("two");
  }
}
