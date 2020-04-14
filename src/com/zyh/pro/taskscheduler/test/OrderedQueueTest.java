package com.zyh.pro.taskscheduler.test;

import com.zyh.pro.taskscheduler.main.OrderedQueue;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.Executors;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class OrderedQueueTest {
	@Test
	public void takeIf() throws InterruptedException {
		OrderedQueue<Integer> queue = new OrderedQueue<>();
		queue.add(1);
		queue.add(2);
		queue.add(3);
		assertThat(queue.takeIf(value -> value == 1), is(1));
		assertNull(queue.takeIf(value -> value == 1));
		assertThat(queue.takeIf(value -> value == 2), is(2));
	}

	@Test
	public void peek() throws InterruptedException {
		OrderedQueue<Integer> queue = new OrderedQueue<>();
		Executors.newSingleThreadExecutor().submit(() -> {
			queue.add(0);
		});
		assertThat(queue.peek(), is(0));
		assertThat(queue.isEmpty(), is(false));
		assertThat(queue.take(), is(0));
	}

	@Test
	public void shutdown() throws InterruptedException {
		OrderedQueue<Integer> queue = new OrderedQueue<>();
		queue.add(1);
		queue.add(2);
		queue.add(3);
		queue.shutdown();
		try {
			queue.add(4);
		} catch (IllegalStateException e) {
			System.out.println("e.getMessage() = " + e.getMessage());
			assertThat(queue.take(), is(1));
			assertThat(queue.take(), is(2));
			assertThat(queue.take(), is(3));
			return;
		}
		fail("can't be there");
	}

	@Test
	public void ordered() {
		ArrayList<Integer> result = new ArrayList<>();
		OrderedQueue<Integer> queue = new OrderedQueue<>();
		Random random = new Random();
		for (int i = 0; i < 300; i++) {
			int value = random.nextInt(50);
			queue.add(value);
			result.add(value);
		}
		Collections.sort(result);
		assertThat(queue.toString(), is(result.toString()));
	}
}
