package org.forwoods.timecontrol.testing;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.forwoods.timecontrol.timecontrolapi.TimeFactory;

public class MockClock extends Clock {

	PriorityQueue<TimeControlFuture<?>> scheduledEvents;
	MockTimeExecutor executor;
	Set<TimeControlFuture<?>> executing;

	public static void mockTime() {
		TimeFactory.setClock(new MockClock());
		TimeFactory.setSleepFactory(new MockSleepFactory());
	}
	
	private Instant now;

	public MockClock() {
		now = Instant.ofEpochMilli(0);
		executing = Collections.newSetFromMap(new ConcurrentHashMap<TimeControlFuture<?>,Boolean>());
		executor = new MockTimeExecutor();
	}
	
	public <V> Future<V> executeAt(Instant when, Callable<V> what) {
		TimeControlFuture<V> future = new TimeControlFuture<>();
		future.scheduledTime = when;
		future.task = what;
		if (!when.isAfter(now)) {
			executeNow(future);
		}
		return future;
	}
	
	public Future<Object> executeAt(Instant when, Runnable what) {
		TimeControlFuture<Object> future = new TimeControlFuture<>();
		future.scheduledTime = when;
		future.task = Executors.callable(what);
		if (!when.isAfter(now)) {
			executeNow(future);
		}
		return future;
	}
	
	public <V> Future<V> executeNow(Callable<V> what) {
		return executeAt(now, what);
	}
	
	private <V> void executeNow(TimeControlFuture<V> future) {
		executing.add(future);
		executor.executeTask(future);
	}

	public void advance(long millis) {
		while (!executing.isEmpty()) {
			for (TimeControlFuture<?> future: executing) {
				if (future.isBlockedOrDone()) {
					executing.remove(future);
				}
			}
		}
	}

	@Override
	public ZoneId getZone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Clock withZone(ZoneId zone) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Instant instant() {
		return now;
	}

	public void setNow(Instant now) {
		this.now = now;		
	}

}
