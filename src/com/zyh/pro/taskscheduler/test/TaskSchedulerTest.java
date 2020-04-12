package com.zyh.pro.taskscheduler.test;

import com.zyh.pro.taskscheduler.main.CallbackTask;
import com.zyh.pro.taskscheduler.main.TaskScheduler;
import org.junit.Test;

import java.util.concurrent.Executors;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TaskSchedulerTest {
	@Test
	public void shutdown() throws InterruptedException {
		CallbackTask task = new CallbackTask(2);
		TaskScheduler scheduler = new TaskScheduler();
		pushDoneTask(task, scheduler, 1000);
		scheduler.start();
		pushDoneTask(task, scheduler, 2000);
		scheduler.shutdown();
		task.waitForCompletion();
		assertThat(scheduler.isShutdown(), is(true));
		Thread.sleep(200);
	}

	@Test
	public void schedules() throws InterruptedException {
		CallbackTask task = new CallbackTask();
		TaskScheduler scheduler = new TaskScheduler();
		pushDoneTask(task, scheduler, 2000);
		scheduler.start();
		task.waitForCompletion();
	}

	@Test
	public void simple_test() throws InterruptedException {
		CallbackTask task = new CallbackTask(200);

		TaskScheduler taskScheduler = new TaskScheduler();
		taskScheduler.start();

		asyncAddTask(task, taskScheduler);
		asyncAddTask(task, taskScheduler);

		task.waitForCompletion();
	}

	private void asyncAddTask(CallbackTask task, TaskScheduler taskScheduler) {
		Executors.newSingleThreadExecutor().submit(() -> {
			for (int i = 0; i < 100; i++)
				taskScheduler.addTask(task::done, i * 50);
			System.out.println("over");
		});
	}

	private void pushDoneTask(CallbackTask task, TaskScheduler scheduler, final int delay) {
		scheduler.addTask(new TaskScheduler.Scheduled() {
			@Override
			public Runnable getTask() {
				return task::done;
			}

			@Override
			public long getDelay() {
				return delay;
			}
		});
	}
}
