package org.forwoods.timecontrol;

public class DefaultSleepFactory implements Sleeper {

	@Override
	public void sleep(long millis) throws InterruptedException {
		Thread.sleep(millis);
	}

}
