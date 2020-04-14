package com.zyh.pro.taskscheduler.test;

import com.zyh.pro.taskscheduler.main.ScheduledTask;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class SchedulerTaskTest {
	@Test
	public void compareTo() {
		ScheduledTask biggerDelay = ScheduledTask.get(() -> System.out.println("whatever"), 1000, () -> 666);
		ScheduledTask lowerDelay = ScheduledTask.get(() -> System.out.println("whatever"), 500, () -> 666);
		assertTrue(biggerDelay.compareTo(lowerDelay) > 0);
	}

	@Test
	public void to_string() {
		ScheduledTask task = ScheduledTask.get(() -> System.out.println("whatever"), 1000, () -> 666);
		assertThat(task.toString(), is("Task(" + 1000 + ")"));
	}
}
