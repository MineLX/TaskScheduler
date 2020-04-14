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
		CallbackTask task = new CallbackTask(400);

		TaskScheduler taskScheduler = new TaskScheduler();
		taskScheduler.start();
		long used = System.currentTimeMillis();

		asyncAddTask(task, taskScheduler);
		asyncAddTask(task, taskScheduler);
		asyncAddTask(task, taskScheduler);
		asyncAddTask(task, taskScheduler);

//		taskScheduler.addTask(() -> System.out.println("hello world   " + (System.currentTimeMillis() - used)), 10000);
		task.waitForCompletion();
	}

	private void asyncAddTask(CallbackTask task, TaskScheduler taskScheduler) {
		Executors.newSingleThreadExecutor().submit(() -> {
			System.out.println(Thread.currentThread() + "  -> start");
			try {
				for (int i = 0; i < 100; i++)
					taskScheduler.addTask(task::done, i);
			} catch (Exception everything) {
				System.out.println("great job");
				everything.printStackTrace();
			}
			System.out.println(Thread.currentThread() + "  -> end");
			System.out.println("over count");
		});
	}

	private void pushDoneTask(CallbackTask task, TaskScheduler scheduler, final int delay) {
		scheduler.addTask(new TaskScheduler.Scheduled() {
			@Override
			public void doTask() {
				task.done();
			}

			@Override
			public long getDelay() {
				return delay;
			}
		});
	}
}
