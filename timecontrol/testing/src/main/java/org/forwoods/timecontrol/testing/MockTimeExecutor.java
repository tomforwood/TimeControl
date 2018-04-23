package org.forwoods.timecontrol.testing;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;

/**
 * @author Tom
 *
 */
public class MockTimeExecutor {
	public static final int corePoolSize = 5;

	ConcurrentLinkedQueue<TaskRunner> runners= new ConcurrentLinkedQueue<>();
	final ThreadGroup group;
	
	
	
	public MockTimeExecutor() {
		group = new ThreadGroup("MockTimeRunners");
	}


	public void executeTask(TimeControlFuture<?> future) {
		TaskRunner runner = runners.poll();
		if (runner==null) {
			runner = new TaskRunner();
			Thread taskThread = new Thread(group, runner);
			taskThread.start();
		}
		runner.taskTransfer.add(future);
	}
	
	
	/**
	 * @param a TaskRunner that doesn't have anything to do right now
	 * @return whether the thread should continue running
	 */
	private boolean returnThreadToPool(TaskRunner runner) {
		if (runners.size()>corePoolSize) {
			return false;
		}
		runners.add(runner);
		return true;
	}
	
	private class TaskRunner implements Runnable {
		TransferQueue<TimeControlFuture<?>> taskTransfer;
		boolean running = true;
		
		public TaskRunner() {
			taskTransfer = new LinkedTransferQueue<>();
		}

		@Override
		public void run() {
			while (running) {
				try {
					TimeControlFuture<?> task = taskTransfer.take();
					task.execute();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				running = returnThreadToPool(this);
			}
		}
		
	}
}
