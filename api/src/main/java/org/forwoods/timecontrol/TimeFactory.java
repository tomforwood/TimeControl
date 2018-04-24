package org.forwoods.timecontrol;

import java.time.Clock;

public class TimeFactory {

	private static Clock clock = Clock.systemDefaultZone();
	private static Sleeper sleeper = new DefaultSleepFactory();

	public static void sleep(long millis) throws InterruptedException {
		sleeper.sleep(millis);
	}
	
	public static Clock getClock() {
		return clock;
	}

	public static void setClock(Clock clock) {
		TimeFactory.clock = clock;
	}
	
	public static void setSleeper(Sleeper sleeper) {
		TimeFactory.sleeper = sleeper;
	}

	public static Sleeper getSleeper() {
		return sleeper;
	}

}
