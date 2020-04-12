package com.zyh.pro.main.taskscheduler.test;

import com.zyh.pro.main.taskscheduler.main.CallbackTask;
import com.zyh.pro.main.taskscheduler.main.OrderedQueue;
import org.junit.Test;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.Executors;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class OrderedQueueTest {

	@Test
	public void multi_test() throws InterruptedException {
		OrderedQueue<Integer> queue = new OrderedQueue<>();

		CallbackTask task = new CallbackTask(4);
		List<Integer> result = new Vector<>();

		asyncAdd(queue, task);
		asyncAdd(queue, task);

		asyncTake(queue, task, result);
		asyncTake(queue, task, result);

		task.waitForCompletion();
		System.out.println("result = " + result);
		System.out.println("queue = " + queue);
		assertThat(result.size() + queue.size(), is(2000));
	}

	@Test
	public void take_test() throws InterruptedException {
		OrderedQueue<Integer> queue = new OrderedQueue<>();
		Executors.newSingleThreadExecutor().submit(() -> {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			queue.add(1);
		});
		assertThat(queue.take(), is(1));
	}

	@Test
	public void peek() throws InterruptedException {
		OrderedQueue<Integer> queue = new OrderedQueue<>();
		Executors.newSingleThreadExecutor().submit(() -> {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			queue.add(1);
		});
		assertThat(queue.peek(), is(1));
	}

	@Test
	public void simple_append() {
		OrderedQueue<Integer> queue = getIntegerOrderedQueue();
		assertThat(queue.size(), is(8));
		assertThat(queue.toString(), is("[0, 0, 1, 2, 3, 5, 7, 9]"));
	}

	private void asyncTake(OrderedQueue<Integer> queue, CallbackTask task, List<Integer> result) {
		Executors.newSingleThreadExecutor().submit(() -> {
			while (!queue.isClosed() || !queue.isEmpty()) {
				push(queue, result);
				push(queue, result);
			}
			task.done();
		});
	}

	private void asyncAdd(OrderedQueue<Integer> queue, CallbackTask task) {
		Executors.newSingleThreadExecutor().submit(() -> {
			for (int i = 0; i < 1000; i++) {
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				queue.add(i);
			}
			task.done();
			queue.closeQueue();
		});
	}

	private void push(OrderedQueue<Integer> queue, List<Integer> result) {
		try {
			Integer take = queue.take();
			if (take != null)
				result.add(take);
		} catch (InterruptedException ignored) {
			System.out.println("push: queue was closed.");
		}
	}

	private OrderedQueue<Integer> getIntegerOrderedQueue() {
		OrderedQueue<Integer> queue = new OrderedQueue<>();
		queue.add(2);
		queue.add(1);
		queue.add(7);
		queue.add(3);
		queue.add(9);
		queue.add(0);
		queue.add(0);
		queue.add(5);
		return queue;
	}
}
