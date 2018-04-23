package org.forwoods.timecontrol.testing;

import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.forwoods.timecontrol.SleepFactory;
import org.forwoods.timecontrol.TimeFactory;

public class MockSleepFactory implements SleepFactory {

	private boolean fastTime = false;
	
	public boolean isFastTime() {
		return fastTime;
	}
	public void setFastTime(boolean fastTime) {
		this.fastTime = fastTime;
	}
	@Override
	public void sleep(long millis) throws InterruptedException {
		MockClock mockClock = (MockClock)TimeFactory.getClock();
		if (fastTime) {
			mockClock.advance(millis);
		}
		else {
			Instant when = mockClock.instant();
			when.plusMillis(millis);
			Future<Boolean> waitTask = mockClock.executeAt(when, ()->true);
			try {
				waitTask.get();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
