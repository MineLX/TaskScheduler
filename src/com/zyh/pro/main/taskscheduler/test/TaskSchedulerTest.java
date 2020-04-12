package com.zyh.pro.main.taskscheduler.test;

import com.zyh.pro.main.taskscheduler.main.CallbackTask;
import com.zyh.pro.main.taskscheduler.main.TaskScheduler;
import org.junit.Test;

import java.util.Collections;

public class TaskSchedulerTest {
	@Test
	public void schedules() throws InterruptedException {
		CallbackTask task = new CallbackTask();
		TaskScheduler scheduler = new TaskScheduler();
		scheduler.addTask(new TaskScheduler.Scheduled() {
			@Override
			public Runnable getTask() {
				return task::done;
			}

			@Override
			public long getDelay() {
				return 2000;
			}
		});
		scheduler.start();
		task.waitForCompletion();
	}

	@Test
	public void simple_test() throws InterruptedException {
		CallbackTask task = new CallbackTask(100);

		TaskScheduler taskScheduler = new TaskScheduler();
		taskScheduler.start();

		for (int i = 0; i < 100; i++)
			taskScheduler.addTask(task::done, i * 50);

		task.waitForCompletion();
	}
}
