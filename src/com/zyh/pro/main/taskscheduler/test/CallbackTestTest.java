package com.zyh.pro.main.taskscheduler.test;

import com.zyh.pro.main.taskscheduler.main.CallbackTask;
import org.junit.Test;

import java.util.concurrent.Executors;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CallbackTestTest {
	@Test
	public void multi_done() throws InterruptedException {
		CallbackTask task = new CallbackTask(2);
		doneWithDelay(task, 500);
		doneWithDelay(task, 1000);
		task.waitForCompletion();
		assertThat(task.completedCount(), is(2));
	}

	@Test
	public void simple_test() throws InterruptedException {
		CallbackTask task = new CallbackTask();
		assertThat(task.isDone(), is(false));

		doneWithDelay(task, 500);

		task.waitForCompletion();
		assertThat(task.isDone(), is(true));
	}

	private void doneWithDelay(CallbackTask task, int delay) {
		Executors.newSingleThreadExecutor().submit(() -> {
			try {
				Thread.sleep(delay);
			} catch (InterruptedException ignored) {}
			System.out.println("call done()");
			task.done();
		});
	}
}
