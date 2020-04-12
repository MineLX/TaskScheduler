package com.zyh.pro.taskscheduler.test;

public class SleepingStateTest {

	private static boolean sleeping;

	private static Thread service;

	private static final Object lock = new Object();

	public static void main(String[] args) throws InterruptedException {
		service = new Thread(SleepingStateTest::run);
		service.start();
		Thread.sleep(1900);
		interruptSleep();
	}

	private static void interruptSleep() throws InterruptedException {
		synchronized (lock) {
			System.out.println("sleeping = " + sleeping);
			if (sleeping) {
				System.out.println("start interruption");
				Thread.sleep(100);
				System.out.println("interrupt");
				service.interrupt();
			}
			System.out.println("interruption end " + service.getState());
		}
	}

	private static void sleep() throws InterruptedException {
		synchronized (lock) {
			sleeping = true;
			System.out.println("sleep true");
		}
		Thread.sleep(2000);
		synchronized (lock) {
			sleeping = false;
			if (service.isInterrupted())
				Thread.sleep(0); // active interruption
			System.out.println("sleep false");
		}
	}

	public static void run() {
		while (true) {
//			System.out.println("turn to sleep...");
			try {
				sleep();
				System.out.println("out of sleep catch block...");
			} catch (InterruptedException e) {
				System.out.println("i catch it...");
			}

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
