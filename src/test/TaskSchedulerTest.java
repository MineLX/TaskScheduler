package test;

import main.CallbackTask;
import main.TaskScheduler;
import org.junit.Test;

public class TaskSchedulerTest {
	@Test
	public void delayed_started() throws InterruptedException {
		CallbackTask task = new CallbackTask();

		TaskScheduler scheduler = new TaskScheduler();
		scheduler.addTask(task::done, 1000);
		scheduler.start();

		task.waitForCompletion();
	}

	@Test
	public void simple_test() throws InterruptedException {
		CallbackTask task = new CallbackTask(100);

		TaskScheduler taskScheduler = new TaskScheduler();
		for (int i = 0; i < 100; i++)
			taskScheduler.addTask(task::done, i * 200);
		taskScheduler.start();

		task.waitForCompletion();
	}
}
