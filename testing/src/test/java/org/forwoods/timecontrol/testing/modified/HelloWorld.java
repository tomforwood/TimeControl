package org.forwoods.timecontrol.testing.modified;

import java.time.Instant;
import java.time.LocalDate;

import org.forwoods.timecontrol.TimeFactory;

/**
 * This class contains application code to be tested by the test case TestMockTime
 * @author TomForwood
 *
 */
public class HelloWorld {
	
	/**
	 * @return a greeting tomorrow
	 * @throws InterruptedException
	 */
	public String delayedGreeting() throws InterruptedException {
		TimeFactory.sleep(1000L*60*60*24);
		return "Hello World!";
	}
	
	public long getMillisTime() {
		return TimeFactory.getClock().millis();
	}
	
	public Instant getInstant() {
		return TimeFactory.getClock().instant();
	}
	
	public LocalDate getDate() {
		return LocalDate.now(TimeFactory.getClock());
	}

}
