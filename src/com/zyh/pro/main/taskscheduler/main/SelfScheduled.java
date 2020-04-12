package com.zyh.pro.main.taskscheduler.main;

import java.util.function.Consumer;

public interface SelfScheduled<Self extends SelfScheduled<Self>> extends BeScheduled<Self> { // FIXME 2020/4/12  wait for me!!!   toAbstractClass

	long getDelay();

	Self self();

	default TaskScheduler.Scheduled toScheduled(Consumer<Self> consumer) {
		return new TaskScheduler.Scheduled() {
			@Override
			public Runnable getTask() {
				return () -> consumer.accept(self());
			}

			@Override
			public long getDelay() {
				return SelfScheduled.this.getDelay();
			}
		};
	}
}
