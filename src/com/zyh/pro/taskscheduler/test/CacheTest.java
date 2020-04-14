package com.zyh.pro.taskscheduler.test;

import com.zyh.pro.taskscheduler.main.Cache;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CacheTest {
	@Test
	public void simple_test() {
		Cache<Object> cache = new Cache<>(Object::new);
		Object one = cache.get();
		cache.add(one);
		assertEquals(one, cache.get());
	}
}
