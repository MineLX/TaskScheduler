package com.zyh.pro.taskscheduler.test;

import com.zyh.pro.taskscheduler.main.BlockedQueue;
import com.zyh.pro.taskscheduler.main.CallbackTask;
import org.junit.Test;

import java.util.concurrent.Executors;

public class WeiredTest {

	@Test
	public void simple_test() throws InterruptedException {
		CallbackTask task = new CallbackTask(400);

		BlockedQueue<Integer> taskScheduler = new BlockedQueue<>();

		asyncAddTask(taskScheduler);
		asyncAddTask(taskScheduler);
		asyncAddTask(taskScheduler);
		asyncAddTask(taskScheduler);

		asyncTakeTask(task, taskScheduler);
		asyncTakeTask(task, taskScheduler);

		task.waitForCompletion();
	}

	private void asyncTakeTask(CallbackTask task, BlockedQueue<Integer> taskScheduler) {
		Executors.newSingleThreadExecutor().submit(() -> {
			while (!taskScheduler.isEmpty()) {
				try {
					taskScheduler.take();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				task.done();
			}
			System.out.println("end loop");
		});
	}

	private void asyncAddTask(BlockedQueue<Integer> taskQueue) {
		Executors.newSingleThreadExecutor().submit(() -> {
			for (int i = 0; i < 100; i++)
				taskQueue.add(i);
			System.out.println("over addTask");
//			taskQueue.shutdown();
		});
	}
}
