package org.forwoods.timecontrol.testing;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.forwoods.timecontrol.TimeFactory;

public class MockClock extends Clock {

	PriorityBlockingQueue<TimeControlFuture<?>> scheduledEvents;
	MockTimeExecutor executor;
	Set<TimeControlFuture<?>> executing;

	public static void mockTime(Instant time) {
		TimeFactory.setClock(new MockClock(time));
		TimeFactory.setSleepFactory(new MockSleepFactory());
	}
	
	public static void mockTime() {
		TimeFactory.setClock(new MockClock());
		TimeFactory.setSleepFactory(new MockSleepFactory());
	}
	
	private Instant now;

	public MockClock() {
		now = Instant.ofEpochMilli(0);
		executing = Collections.newSetFromMap(new ConcurrentHashMap<TimeControlFuture<?>,Boolean>());
		executor = new MockTimeExecutor();
		scheduledEvents = new PriorityBlockingQueue<>();
	}
	
	public MockClock(Instant now) {
		this.now = now;
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

	public void advance(long millis) {
		advanceBy(millis, TimeUnit.MILLISECONDS);
	}
	
	public void advanceBy(long timeCount, TimeUnit unit) {
		Instant goal = now.plus(timeCount, convertTimeUnit(unit));
		advanceTo(goal);
	}
	
	public void advanceTo(Instant goal) {
		while (!scheduledEvents.isEmpty() && !scheduledEvents.peek().scheduledTime.isAfter(goal)) {
			TimeControlFuture<?> next = scheduledEvents.poll();
			this.now = next.scheduledTime;
			runOut(next);
		}
		this.now = goal;
	}
	
	private void runOut(TimeControlFuture<?> future) {
		executor.executeTask(future);
		while (!future.isBlockedOrDone()) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private ChronoUnit convertTimeUnit(TimeUnit unit) {
		switch (unit) {
		case DAYS:
			return ChronoUnit.DAYS;
		case HOURS:
			return ChronoUnit.HOURS;
		case MICROSECONDS:
			return ChronoUnit.MICROS;
		case MILLISECONDS:
			return ChronoUnit.MILLIS;
		case MINUTES:
			return ChronoUnit.MINUTES;
		case NANOSECONDS:
			return ChronoUnit.NANOS;
		case SECONDS:
		default:
			return ChronoUnit.SECONDS;		
		}
	}

}
