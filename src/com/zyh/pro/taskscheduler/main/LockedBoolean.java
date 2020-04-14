package com.zyh.pro.taskscheduler.main;

public class LockedBoolean {

	private volatile boolean flag;

	public synchronized void toTrue() {
		flag = true;
	}

	public synchronized void toFalse() {
		flag = false;
	}

	public synchronized boolean yes() {
		return flag;
	}
}
