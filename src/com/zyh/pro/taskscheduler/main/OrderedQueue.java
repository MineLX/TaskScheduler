package com.zyh.pro.taskscheduler.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class OrderedQueue<E extends Comparable<E>> {

	private final List<E> list;

	private boolean isShutdown;

	public OrderedQueue() {
		list = new ArrayList<>();
	}

	public synchronized void add(E e) {
		if (isShutdown) {
			System.out.println("shutdown add");
			throw new IllegalStateException("queue has been shutdown, add is forbidden.");
		}

		// add to list, notify the element takers
		list.add(getPositionOf(e), e);
		notifyAll();
	}

	public synchronized E take() throws InterruptedException {
		waitForElement();
		return list.remove(0);
	}

	public synchronized E peek() throws InterruptedException {
		waitForElement();
		return list.get(0);
	}

	private synchronized void waitForElement() throws InterruptedException {
		while (isEmpty()) {
			if (isShutdown) throw new IllegalStateException("queue has been shutdown while waiting...");
			wait();
		}
	}

	public synchronized boolean isEmpty() {
		return list.isEmpty();
	}

	public synchronized void shutdown() {
		isShutdown = true;
		notifyAll();
	}

	public synchronized boolean isShutdown() {
		return isShutdown;
	}

	@Override
	public String toString() {
		return list.toString();
	}

	private int getPositionOf(E addend) {
		if (isEmpty())
			return 0;

		int left = 0;
		int right = list.size();
		int middleIndex;

		while (left != (middleIndex = (left + right) / 2)) {
			int compareResult = addend.compareTo(list.get(middleIndex));
			if (compareResult < 0) {            // lower than middleElement
				right = middleIndex;
			} else if (compareResult > 0) {     // bigger than middleElement
				left = middleIndex;
			} else {
				return middleIndex + 1;
			}
		}

		if (addend.compareTo(list.get(left)) < 0)
			return left;
		return right;
	}

	public synchronized E takeIf(Predicate<E> predicate) throws InterruptedException {
		return predicate.test(peek()) ? take() : null;
	}

	public synchronized Optional<E> optionalIf(Predicate<E> predicate) throws InterruptedException {
		if (predicate.test(peek()))
			return Optional.of(take());
		return Optional.empty();
	}
}
