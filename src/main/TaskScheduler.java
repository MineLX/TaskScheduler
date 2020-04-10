package main;

import java.util.function.LongSupplier;

import static java.lang.System.currentTimeMillis;

public class TaskScheduler {

	private final OrderedQueue<ScheduledTask> tasks;

	private final DelayRunner delayRunner;

	private final LongSupplier startTimeSupplier;

	private long startTime;

	public TaskScheduler() {
		tasks = new OrderedQueue<>();
		delayRunner = new DelayRunner(tasks);
		startTimeSupplier = () -> startTime;
	}

	public void addTask(Runnable task, int delay) {
		tasks.add(ScheduledTask.get(task, delay, startTimeSupplier));
		delayRunner.interruptSleep();
	}

	public void start() {
		startTime = currentTimeMillis();
		delayRunner.start();
	}
}
