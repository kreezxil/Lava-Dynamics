package com.eleksploded.lavadynamics.threaded;

import java.util.concurrent.CountDownLatch;

public class WaitingValue<T> {
	private T value = null;
	private Object[] args;
	CountDownLatch latch = new CountDownLatch(1);
	
	public WaitingValue(Object... args) {
		this.args = args;
	}
	
	public boolean isPresent() {
		return value != null;
	}
	
	public T get() {
		return value;
	}
	
	public void serve(T value) {
		this.value = value;
		latch.countDown();
	}
	
	public T getOncePresent() {
		try {
			latch.await();
			return value;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Object[] getArgs() {
		return args;
	}
}
