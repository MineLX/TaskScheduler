package com.zyh.pro.main.taskscheduler.main;

import java.util.LinkedList;
import java.util.List;
import java.util.function.LongSupplier;

import static java.lang.System.currentTimeMillis;

public class ScheduledTask implements Comparable<ScheduledTask> {

	private static final List<ScheduledTask> FREE_TASKS = new LinkedList<>();

	private LongSupplier startTime;

	private Runnable task;

	private long delay;

	private ScheduledTask(LongSupplier startTime, Runnable task, long delay) {
		this.startTime = startTime;
		this.task = task;
		this.delay = delay;
	}

	void doTask() {
		System.out.println("doTask delayed: " + (currentTimeMillis() - getTimestamp()));
		task.run();
		toCache(this);
	}

	boolean timeIsUp() {
		return currentTimeMillis() >= getTimestamp() - 1;
	}

	long getDelayForSleep() {
		long result = (getTimestamp() - currentTimeMillis()) / 2;
		return result <= 0 ? 0 : result;
	}

	private long getTimestamp() {
		return startTime.getAsLong() + delay;
	}

	@Override
	public int compareTo(ScheduledTask another) {
		return (int) Math.signum(getTimestamp() - another.getTimestamp());
	}

	@Override
	public String toString() {
		return "Task(" + getTimestamp() + ")";
	}

	static ScheduledTask get(Runnable task, long delay, LongSupplier startTime) {
		if (FREE_TASKS.isEmpty())
			return new ScheduledTask(startTime, task, delay);
		return cache(task, delay, startTime);
	}

	private synchronized static ScheduledTask cache(Runnable task, long delay, LongSupplier startTime) {
		ScheduledTask result = FREE_TASKS.remove(0);
		result.task = task;
		result.delay = delay;
		result.startTime = startTime;
		return result;
	}

	private synchronized static void toCache(ScheduledTask scheduledTask) {
		FREE_TASKS.add(scheduledTask);
	}
}
