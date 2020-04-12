package com.zyh.pro.main.taskscheduler.main;

import java.util.List;
import java.util.function.LongSupplier;

import static java.lang.System.currentTimeMillis;
import static com.zyh.pro.main.taskscheduler.main.ScheduledTask.get;

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

	public void addTask(Runnable task, long delay) {
		tasks.add(get(task, delay, startTimeSupplier));
		delayRunner.interruptSleep();
	}

	public void addTask(Scheduled scheduled) {
		addTask(scheduled.getTask(), scheduled.getDelay());
	}

	public void addTasks(List<Scheduled> schedules) {
		schedules.forEach(this::addTask);
	}

	public void start() {
		startTime = currentTimeMillis();
		delayRunner.start();
	}

	public void stop() {
		delayRunner.stop();
	}

	public interface Scheduled {
		Runnable getTask();

		long getDelay();
	}
}
