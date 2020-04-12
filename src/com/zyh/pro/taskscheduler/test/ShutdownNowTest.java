package com.zyh.pro.taskscheduler.test;

import com.zyh.pro.taskscheduler.main.TaskScheduler;

public class ShutdownNowTest {
	public static void main(String[] args) throws InterruptedException {
		TaskScheduler scheduler = new TaskScheduler();
		scheduler.start();
		Thread.sleep(2000);
		scheduler.shutdownNow();
	}
}
