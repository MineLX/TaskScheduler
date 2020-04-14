package com.zyh.pro.taskscheduler.test;

public class WaitTest {

	private static final Object anotherLock = new Object();

	public static void main(String[] args) throws InterruptedException {
		new Thread(() -> {
			try {
				Thread.sleep(1000);
				System.out.println("thread start order");
				order();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();

		order();
	}

	private synchronized static void order() throws InterruptedException {
		System.out.println(Thread.currentThread().getName() + " order method");
		order2();
	}

	private static void order2() throws InterruptedException {
		synchronized (anotherLock) {
			System.out.println(Thread.currentThread().getName() + " order2 method");
			anotherLock.wait();
		}
	}
}
