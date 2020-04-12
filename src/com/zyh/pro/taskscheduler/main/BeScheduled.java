package com.zyh.pro.taskscheduler.main;

import java.util.function.Consumer;

public interface BeScheduled<Self extends BeScheduled<Self>> {
	TaskScheduler.Scheduled toScheduled(Consumer<Self> consumer);
}
