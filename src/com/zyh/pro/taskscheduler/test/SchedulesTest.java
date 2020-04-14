package com.zyh.pro.taskscheduler.test;

import com.zyh.pro.taskscheduler.main.CallbackTask;
import com.zyh.pro.taskscheduler.main.Schedulers;
import com.zyh.pro.taskscheduler.main.SelfScheduled;
import org.junit.Test;

import static java.util.Arrays.asList;

public class SchedulesTest {
	@Test
	public void track() throws InterruptedException {
		CallbackTask task = new CallbackTask(3);

		Schedulers.track(asList(
				new Kick(1000, 60),
				new Kick(1000, 60),
				new Kick(1500, 30)
		), kick -> task.done()).start();

		task.waitForCompletion();
	}

	private static class Kick implements SelfScheduled<Kick> {
		int delay;

		int rate;

		Kick(int delay, int rate) {
			this.delay = delay;
			this.rate = rate;
		}

		@Override
		public long getDelay() {
			return delay;
		}

		@Override
		public Kick self() {
			return this;
		}
	}
}
