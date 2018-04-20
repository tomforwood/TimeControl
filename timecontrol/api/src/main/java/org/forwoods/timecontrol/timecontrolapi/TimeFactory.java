package org.forwoods.timecontrol.timecontrolapi;

import java.time.Clock;

public class TimeFactory {

	private static Clock clock;
	private static SleepFactory sleepFactory = new DefaultSleepFactory();

	public static void sleep(long millis) throws InterruptedException {
		sleepFactory.sleep(millis);
	}
	
	public static Clock getClock() {
		return clock;
	}

	public static void setClock(Clock clock) {
		TimeFactory.clock = clock;
	}
	
	public static void setSleepFactory(SleepFactory sleepFactory) {
		TimeFactory.sleepFactory = sleepFactory;
	}

}
