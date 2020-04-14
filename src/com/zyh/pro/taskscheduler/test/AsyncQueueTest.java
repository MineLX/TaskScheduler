package com.zyh.pro.taskscheduler.test;

import com.zyh.pro.taskscheduler.main.CallbackTask;
import com.zyh.pro.taskscheduler.main.OrderedQueue;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Collections.synchronizedList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class AsyncQueueTest {

	@Test
	public void multi() throws InterruptedException {
		CallbackTask task = new CallbackTask(7500);

		OrderedQueue<Integer> queue = new OrderedQueue<>();

		List<Integer> list = synchronizedList(new LinkedList<>());

		add(queue, 0, 1000);
		add(queue, 1000, 2000);
		add(queue, 2000, 3000);
		add(queue, 3000, 4000);
		add(queue, 4000, 5000);
		add(queue, 5000, 6000);
		add(queue, 6000, 7000);
		add(queue, 7000, 7500);
		take(queue, list, task);
		take(queue, list, task);
		take(queue, list, task);
		take(queue, list, task);
		take(queue, list, task);
		take(queue, list, task);
		take(queue, list, task);
		take(queue, list, task);
		take(queue, list, task);
		take(queue, list, task);

		task.waitForCompletion();
		for (Integer integer : list) {
			assertNotNull(integer);
		}

		Collections.sort(list);
		System.out.println("list = " + list);
		for (int i = 0; i < 7500; i++)
			assertThat(list.get(i), is(i));
		System.out.println("victory");
	}

	private void take(OrderedQueue<Integer> queue, List<Integer> result, CallbackTask task) {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		executorService.submit(() -> {
			while (!queue.isEmpty()) {
				try {
					result.add(queue.take());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				task.done();
			}
		});
		executorService.shutdown();
	}

	private void add(OrderedQueue<Integer> queue, int start, int end) {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		executorService.submit(() -> {
			for (int index = start; index < end; index++) {
				queue.add(index);
			}
		});
		executorService.shutdown();
	}
}
