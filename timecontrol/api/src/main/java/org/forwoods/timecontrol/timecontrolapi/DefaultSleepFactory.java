package org.forwoods.timecontrol.timecontrolapi;

public class DefaultSleepFactory implements SleepFactory {

	@Override
	public void sleep(long millis) throws InterruptedException {
		Thread.sleep(millis);
	}

}
