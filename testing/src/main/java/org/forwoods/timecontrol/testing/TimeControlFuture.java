package org.forwoods.timecontrol.testing;

import java.lang.Thread.State;
import java.time.Instant;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.google.common.util.concurrent.ListenableFuture;

public class TimeControlFuture<V> implements Comparable<TimeControlFuture<V>>,ListenableFuture<V> {
	
	V result;
	ExecutionException executionException;
	Callable<V> task;
	
	Instant scheduledTime;
	
	boolean isDone;
	boolean isCancelling;
	Thread executingThread;
	CountDownLatch doneLatch = new CountDownLatch(1);

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		if (isDone) return false;
		isCancelling = true;
		isDone=true;
		if (executingThread!=null && mayInterruptIfRunning) {
			executingThread.interrupt();
		}
		return true;
	}

	@Override
	public boolean isCancelled() {
		return isCancelling;
	}

	@Override
	public boolean isDone() {
		return isDone;
	}

	@Override
	public V get() throws InterruptedException, ExecutionException {
		doneLatch.await();
		if (executionException!=null) {
			throw executionException;
		}
		return result;
	}

	@Override
	public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		// TODO Auto-generated method stub
		return null;
		//This method is going to fake the timeout
	}

	@Override
	public void addListener(Runnable listener, Executor executor) {
		// TODO Auto-generated method stub
		
	}
	
	protected boolean isBlockedOrDone() {
		//TODO
		if (isDone) return true;
		else  {
			ThreadGroup group = executingThread.getThreadGroup();
			Thread[] threads = new Thread[1000];
			//int count 
			//for (Thread t:group.enumerate(list))
			State s = executingThread.getState();
			switch (s) {
			case BLOCKED:
			case TIMED_WAITING:
			case TERMINATED:
			case WAITING:
				return true;
			case NEW:
			case RUNNABLE:
			default:
				return false;
			}
		}
	}

	@Override
	public int compareTo(TimeControlFuture<V> o) {
		return this.scheduledTime.compareTo(o.scheduledTime);
	}

	public void execute() {
		executingThread = Thread.currentThread();
		try {
			result = task.call();
		} catch (Exception e) {
			executionException = new ExecutionException(e);
		}
		doneLatch.countDown();
	}
	
	
}
