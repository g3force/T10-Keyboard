package edu.dhbw.t10.type;

public class Ringbuffer<T> {
	
	private T[]			buffer;
	private boolean	full;
	private int			start, end, maxLength;
	
	
	@SuppressWarnings("unchecked")
	public Ringbuffer(int length) {
		maxLength = length;
		buffer = (T[]) new Object[maxLength];
	}
	
	
	private int next(int n) {
		return (n + 1) % maxLength;
	}
	
	
	public boolean isEmpty() {
		return (start == end) && !full;
	}
	
	
	public boolean isFull() {
		return full;
	}
	
	
	public void add(T t) throws Exception {
		if (isFull())
			remove();
		
		buffer[end] = t;
		end = next(end);
		full = (start == end);
		
		// System.out.println(t);
	}
	
	
	public T remove() throws Exception {
		if (isEmpty())
			throw new Exception("Array ist empty!");
		
		T t = buffer[start];
		start = next(start);
		full = false;
		
		// System.out.println(t);
		
		return t;
	}
	
}