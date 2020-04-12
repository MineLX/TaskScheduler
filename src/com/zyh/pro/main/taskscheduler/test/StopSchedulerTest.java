package com.zyh.pro.main.taskscheduler.test;

import com.zyh.pro.main.taskscheduler.main.TaskScheduler;

public class StopSchedulerTest {
	public static void main(String[] args) throws InterruptedException {
		TaskScheduler scheduler = new TaskScheduler();
		scheduler.start();
		Thread.sleep(2000);
		scheduler.stop();
	}
}
