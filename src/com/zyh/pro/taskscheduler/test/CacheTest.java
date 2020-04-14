package com.zyh.pro.taskscheduler.test;

import com.zyh.pro.taskscheduler.main.Cache;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class CacheTest {
	@Test
	public void size() {
		Cache<Integer> cache = new Cache<>(() -> 0);
		assertThat(cache.size(), is(0));
		Integer item = cache.get();
		cache.add(item);
		assertThat(cache.size(), is(1));
	}

	@Test
	public void to_string() {
		Cache<Integer> cache = new Cache<>(() -> 0);
		Integer item1 = cache.get();
		Integer item2 = cache.get();
		cache.add(item1);
		cache.add(item2);
		assertThat(cache.toString(), is("Cache size 2: [0, 0]"));
	}

	@Test
	public void get_add() {
		Cache<Object> cache = new Cache<>(Object::new);
		Object one = cache.get();
		cache.add(one);
		assertEquals(one, cache.get());
	}
}
