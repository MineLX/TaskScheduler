package com.zyh.pro.main.taskscheduler.main;

public class DelayRunner implements Runnable {

	private final OrderedQueue<ScheduledTask> tasks;

	private final Thread service;

	private volatile boolean sleeping;

	private volatile boolean isShutdown;

	DelayRunner(OrderedQueue<ScheduledTask> tasks) {
		this.tasks = tasks;

		service = new Thread(this);
	}

	void start() {
		service.start();
	}

	void interruptSleep() {
		if (sleeping)
			service.interrupt();
	}

	@Override
	public void run() {
		while (!isShutdown) {
			try {
				doTaskIfTimeIsUp();
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}

			try {
				sleep();
			} catch (InterruptedException ignored) {
			}
		}
	}

	private void sleep() throws InterruptedException {
		sleeping = true;
		Thread.sleep(tasks.peek().getDelayForSleep());
		sleeping = false;
	}

	private void doTaskIfTimeIsUp() throws InterruptedException {
		synchronized (tasks) {
			if (tasks.peek().timeIsUp())
				tasks.take().doTask();
		}
	}

	public void stop() {
		isShutdown = true;
		tasks.closeQueue();
	}
}
