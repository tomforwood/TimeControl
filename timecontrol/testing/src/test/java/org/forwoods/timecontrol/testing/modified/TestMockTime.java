package org.forwoods.timecontrol.testing.modified;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.*;

import java.time.Instant;
import java.util.concurrent.Future;

import org.forwoods.timecontrol.testing.MockClock;
import org.forwoods.timecontrol.timecontrolapi.TimeFactory;
import org.junit.Before;
import org.junit.Test;

public class TestMockTime {

	private MockClock mockClock;
	private HelloWorld hello;

	@Before
	public void setUp() throws Exception {
		MockClock.mockTime();
		mockClock = (MockClock) TimeFactory.getClock();
		mockClock.setNow(Instant.ofEpochMilli(0));
		hello = new HelloWorld();
	}

	@Test
	public void testSleep() throws InterruptedException {
		Future<String> result = mockClock.executeNow(()->hello.delayedGreeting());
		Thread.sleep(10);//would normally be plenty of time to wait for 
		//greeting to return
		assertFalse(result.isDone());
		mockClock.advance(1);
		
	}

}
