package com.teamsoft.framework.common.model;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * 枚举集合封装
 * @author zhangcc
 * @version 2017/9/22
 */
public class ArrayEnumeration<E> implements Enumeration<E> {
	// 存放数据集合
	private List<E> data = new ArrayList<>();
	// 当前数据下标
	private int index;

	/**
	 * 添加元素
	 * @param e 待添加的元素
	 */
	public void add(E e) {
		data.add(e);
	}

	/**
	 * 是否有更多元素
	 */
	public boolean hasMoreElements() {
		return index < data.size();
	}

	/**
	 * 下一个元素
	 */
	public E nextElement() {
		if (!hasMoreElements()) {
			throw new NoSuchElementException();
		}
		return data.get(index++);
	}
}