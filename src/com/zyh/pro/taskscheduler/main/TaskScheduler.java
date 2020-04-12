package com.zyh.pro.taskscheduler.main;

import java.util.List;
import java.util.function.LongSupplier;

import static com.zyh.pro.taskscheduler.main.ScheduledTask.get;
import static java.lang.System.currentTimeMillis;

public class TaskScheduler {

	private final OrderedQueue<ScheduledTask> tasks;

	private final TasksExecutor tasksExecutor;

	private final LongSupplier startTimeSupplier;

	private long startTime;

	public TaskScheduler() {
		tasks = new OrderedQueue<>();
		tasksExecutor = new TasksExecutor(tasks);
		startTimeSupplier = () -> startTime;
	}

	public void addTask(Runnable task, long delay) {
		tasks.add(get(task, delay, startTimeSupplier));
		tasksExecutor.interruptSleep();
	}

	public void addTask(Scheduled scheduled) {
		addTask(scheduled.getTask(), scheduled.getDelay());
	}

	public void addTasks(List<Scheduled> schedules) {
		schedules.forEach(this::addTask);
	}

	public void start() {
		startTime = currentTimeMillis();
		tasksExecutor.start();
	}

	public void shutdownNow() {
		tasksExecutor.shutdownNow();
	}

	public void shutdown() {
		tasksExecutor.shutdown();
	}

	public boolean isShutdown() {
		return tasksExecutor.isShutdown();
	}

	public interface Scheduled {
		Runnable getTask();

		long getDelay();
	}
}
