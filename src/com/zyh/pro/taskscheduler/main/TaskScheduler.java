package com.zyh.pro.taskscheduler.main;

import java.util.List;
import java.util.function.LongSupplier;

import static com.zyh.pro.taskscheduler.main.ScheduledTask.get;
import static java.lang.System.currentTimeMillis;

public class TaskScheduler {

	private final OrderedQueue<ScheduledTask> tasks;

	private final TasksExecutor tasksExecutor;

	private final LongSupplier startTimeSupplier;

	private final Thread service;

	private long startTime; // FIXME 2020/4/14  wait for me!!! startTime to factory

	public TaskScheduler() {
		tasks = new OrderedQueue<>();
		tasksExecutor = new TasksExecutor();
		service = new Thread(tasksExecutor);
		startTimeSupplier = () -> startTime;
	}

	public void addTask(Runnable task, long delay) {
		tasks.add(get(task, delay, startTimeSupplier));
		tasksExecutor.interruptSleep();
	}

	public void addTask(Scheduled scheduled) {
		addTask(scheduled::doTask, scheduled.getDelay());
	}

	public void addTasks(List<Scheduled> schedules) {
		schedules.forEach(this::addTask);
	}

	public void start() {
		startTime = currentTimeMillis();
		service.start();
	}

	public void shutdownNow() {
		tasksExecutor.isShutdownNow = true;
		tasks.shutdown();
	}

	public void shutdown() {
		tasksExecutor.isShutdown = true;
		tasks.shutdown();
	}

	public boolean isShutdown() {
		return tasksExecutor.isShutdown;
	}

	public interface Scheduled {
		void doTask();

		long getDelay();
	}

	private class TasksExecutor implements Runnable {

		private volatile boolean isShutdownNow;

		private volatile boolean isShutdown;

		private final LockedBoolean isSleeping;

		private TasksExecutor() {
			isSleeping = new LockedBoolean();
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

		private void sleep() throws InterruptedException {
			isSleeping.toTrue();

			Thread.sleep(tasks.peek().getDelayForSleep(startTime));

			synchronized (isSleeping) {
				isSleeping.toFalse();
				if (Thread.currentThread().isInterrupted())
					Thread.sleep(0); // active the interruption
			}
		}

		private void doNextTask() throws InterruptedException {
			if (tasks.isEmpty() && isShutdown)
				throw new InterruptedException("empty tasks, shutdown...");

			ScheduledTask taken = tasks.takeIf(task -> task.timeIsUpTo(startTime));
			if (taken != null) {
				System.out.println("doTask delayed: " + (currentTimeMillis() - taken.toTimestamp(startTime)));
				taken.doTask();
			}
		}

		private void interruptSleep() {
			synchronized (isSleeping) {
				if (isSleeping.yes())
					service.interrupt();
			}
		}
	}
}
