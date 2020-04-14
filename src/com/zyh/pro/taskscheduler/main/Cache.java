package com.zyh.pro.taskscheduler.main;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class Cache<E> { // FIXME 2020/4/14  wait for me!!!  prevent to add the same object

	private final List<E> weight = new LinkedList<>();

	private final Supplier<E> newGetter;

	public Cache(Supplier<E> newGetter) {
		this.newGetter = newGetter;
	}

	public synchronized E get() {
		if (weight.isEmpty())
			return newGetter.get();
		return weight.remove(0);
	}

	public synchronized void add(E item) {
		weight.add(item);
	}

	public synchronized int size() {
		return weight.size();
	}

	@Override
	public String toString() {
		return "Cache " + "size " + weight.size() + ": " + weight.toString();
	}
}