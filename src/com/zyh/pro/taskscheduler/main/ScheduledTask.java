package com.zyh.pro.taskscheduler.main;

import java.util.function.LongSupplier;

import static java.lang.System.currentTimeMillis;

public class ScheduledTask implements Comparable<ScheduledTask> {

	private static final Cache<ScheduledTask> CACHE = new Cache<>(ScheduledTask::new);

	private LongSupplier startTime;

	private Runnable task;

	private long delay;

	private ScheduledTask() {}

	void doTask() {
		task.run();
		CACHE.add(this);
	}

	boolean timeIsUpTo(long startTime) {
		return currentTimeMillis() >= toTimestamp(startTime) - 1;
	}

	long toTimestamp(long startTime) {
		return startTime + delay;
	}

	long getDelayForSleep(long startTime) {
		long result = (toTimestamp(startTime) - currentTimeMillis()) / 2;
		return result <= 0 ? 0 : result;
	}

	@Override
	public int compareTo(ScheduledTask another) {
		return (int) Math.signum(delay - another.delay);
	}

	@Override
	public String toString() {
		return "Task(" + delay + ")";
	}

	public static ScheduledTask get(Runnable task, long delay, LongSupplier startTime) {
		ScheduledTask result = CACHE.get();
		result.task = task;
		result.delay = delay;
		result.startTime = startTime;
		return result;
	}
}
