package com.zyh.pro.taskscheduler.main;

public class TasksExecutor implements Runnable { // FIXME 2020/4/13  wait for me!!!  may together with TaskScheduler

	private final OrderedQueue<ScheduledTask> tasks;

	private final Thread service;

	private volatile boolean isShutdownNow;

	private volatile boolean isShutdown;

	private volatile boolean isNewTaskComes;

	TasksExecutor(OrderedQueue<ScheduledTask> tasks) {
		this.tasks = tasks;
		service = new Thread(this);
	}

	void start() {
		service.start();
	}

	void interruptSleep() {
		isNewTaskComes = true;
		service.interrupt();
	}

	private void sleep() throws InterruptedException {
		if (isNewTaskComes) {
			isNewTaskComes = false;
			return;
		}
		Thread.sleep(tasks.peek().getDelayForSleep());
	}

	@Override
	public void run() {
		while (!isShutdownNow) {
			try {
				doNextTask();
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

	private void doNextTask() throws InterruptedException {
		if (tasks.isEmpty() && isShutdown)
			throw new InterruptedException("empty tasks, shutdown...");

		if (tasks.peek().timeIsUp())
			tasks.take().doTask();
	}

	public void shutdownNow() {
		isShutdownNow = true;
		tasks.closeQueue();
	}

	public void shutdown() {
		isShutdown = true;
		tasks.closeQueue();
	}

	public boolean isShutdown() {
		return isShutdown;
	}
}
