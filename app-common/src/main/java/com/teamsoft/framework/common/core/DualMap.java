package com.teamsoft.framework.common.core;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 双层Map封装
 */
public class DualMap<T, K, V> {
	private final Map<T, LinkedHashMap<K, V>> data;

	public DualMap() {
		data = new LinkedHashMap<>();
	}

	public void remove(T t) {
		data.remove(t);
	}

	public void remove(T t, K k) {
		LinkedHashMap<K, V> child = data.get(t);
		if (child != null) {
			child.remove(k);
		}
	}

	public void put(T t, K k, V v) {
		LinkedHashMap<K, V> map = data.computeIfAbsent(t, k1 -> new LinkedHashMap<>());
		map.put(k, v);
	}

	public V get(T t, K k) {
		LinkedHashMap<K, V> map = data.get(t);
		if (map == null) {
			return null;
		}
		return map.get(k);
	}

	public List<V> getList(T t) {
		LinkedHashMap<K, V> map = data.get(t);
		if (map == null) {
			return null;
		}
		return new ArrayList<>(map.values());
	}
}