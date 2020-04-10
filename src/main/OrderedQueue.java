package main;

import java.util.LinkedList;
import java.util.List;

public class OrderedQueue<E extends Comparable<E>> {

	private final List<E> store;

	private boolean isClosed;

	public OrderedQueue() {
		store = new LinkedList<>();
	}

	public synchronized void add(E addend) {
		if (store.isEmpty() || lessThat(lastElement(), addend)) {
			insertToLast(addend);
			return;
		}

		// find its position
		for (int at = 0; at < store.size(); at++) {
			if (!lessThat(store.get(at), addend)) {
				insertTo(at, addend);
				return;
			}
		}
	}

	public synchronized E take() throws InterruptedException {
		if (isClosedWhileWaiting())
			return null;
		return store.remove(0);
	}

	public synchronized E peek() throws InterruptedException {
		if (isClosedWhileWaiting())
			return null;
		return store.get(0);
	}

	private boolean isClosedWhileWaiting() throws InterruptedException {
		while (isEmpty()) {
			if (isClosed) return true;
			wait();
		}
		return false;
	}

	public void closeQueue() {
		isClosed = true;
		synchronized (this) {
			notifyAll();
		}
	}

	@Override
	public String toString() {
		return store.toString();
	}

	private void insertTo(int index, E addend) {
		store.add(index, addend);

		// notify the take waiter
		synchronized (this) {
			notify();
		}
	}

	private boolean lessThat(E one, E another) {
		return one.compareTo(another) < 0;
	}

	private E lastElement() {
		return store.get(store.size() - 1);
	}

	private void insertToLast(E addend) {
		insertTo(store.size(), addend);
	}

	public int size() {
		return store.size();
	}

	public boolean isEmpty() {
		return store.isEmpty();
	}

	public boolean isClosed() {
		return isClosed;
	}
}
