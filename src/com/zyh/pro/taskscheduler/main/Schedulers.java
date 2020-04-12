package com.zyh.pro.taskscheduler.main;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Schedulers {
	public static <T extends BeScheduled<T>> TaskScheduler track(List<T> schedules, Consumer<T> tasksConsumer) {
		TaskScheduler result = new TaskScheduler();
		result.addTasks(schedules.stream()
				.map(beSchedule -> beSchedule.toScheduled(tasksConsumer))
				.collect(Collectors.toList()));
		return result;
	}
}
