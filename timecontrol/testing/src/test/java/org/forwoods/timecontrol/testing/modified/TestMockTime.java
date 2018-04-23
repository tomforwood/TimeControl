package org.forwoods.timecontrol.testing.modified;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.*;

import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.forwoods.timecontrol.testing.MockClock;
import org.forwoods.timecontrol.testing.MockSleepFactory;
import org.forwoods.timecontrol.TimeFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TestMockTime {

	private MockClock mockClock;
	private HelloWorld hello;
	private MockSleepFactory mockSleep;

	@Before
	public void setUp() throws Exception {
		MockClock.mockTime();
		mockClock = (MockClock) TimeFactory.getClock();
		mockSleep = ((MockSleepFactory)TimeFactory.getSleepFactory());
		hello = new HelloWorld();
	}
	
	@Test
	public void testGetInstant() {
		Instant i=hello.getInstant();
		assertThat(i.getEpochSecond()==0);
		mockClock.advance(10);
		i=hello.getInstant();
		assertThat(i.getEpochSecond()==10);
	}
	
	@Test
	public void testSleepSimple() throws InterruptedException, ExecutionException {
		assertEquals(0, mockClock.millis());
		mockSleep.setFastTime(true);
		String s=hello.delayedGreeting();
		//normally this call would block for a day
		//but this test is over instantly and the virtual time has advanced by a day
		assertEquals(1000L*60*60*24, mockClock.millis());
	}

	@Test
	public void testSleepThreaded() throws InterruptedException, ExecutionException {
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.DAYS, new LinkedBlockingQueue<>());
		Future<String> result = threadPoolExecutor.submit(()->hello.delayedGreeting());

		assertFalse(result.isDone());
		mockClock.advance(1000L*60*60*24);
		assertEquals("Hello World!", result.get());
		assertTrue(result.isDone());
	}

}
