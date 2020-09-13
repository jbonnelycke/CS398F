import java.util.ArrayList;

public class BinaryHeapPriorityQueue<T> /*implements Comparable<T>*/ {
	
	private static final int DEFAULT_CAPACITY = 10;
	
	private int heap_size;
	private ArrayList<T> A;
	
	public BinaryHeapPriorityQueue() {
		heap_size = 0;
		A = new ArrayList<T>(DEFAULT_CAPACITY);
	}
	
	public BinaryHeapPriorityQueue(int capacity) {
		heap_size = 0;
		A = new ArrayList<T>(capacity);
	}
	
	public BinaryHeapPriorityQueue(T[] items) {
		heap_size = 0;
		A = new ArrayList<T>(items.length);
		for (T item : A) {
			insert(item);
			heap_size++;
		}
	}
	
	public void insert(T key) {
		heap_size = heap_size + 1;
		A.set(heap_size, Integer.MIN_VALUE); // TODO figure this out
		increaseKey(heap_size, key);
	}
	
	public T ExtractMax() throws Exception {
		if (heap_size < 1)
			throw new Exception("heap underflow");
		T max = A.get(1);
		A.set(1, A.get(heap_size));
		heap_size = heap_size - 1;
		this.maxHeapify(1);
		return max;
		
	}
	
	public void increaseKey(int i, T key) throws Exception {
		if ((int) key < (int) A.get(i)) // TODO Fix casting 
			throw new Exception ("new key is smaller than current key");
		A.set(i, key);
		while (i > 1 && (int) A.get(parent(i)) < (int) A.get(i)) { // TODO Fix casting
			T temp = A.get(i);
			A.set(i, A.get(parent(i)));
			A.set(parent(i), temp);
			
			i = parent(i);
		}
	}
	
	private void maxHeapify(int i) {
		int largest;
		int l = left(i);
		int r = right(i);
		if (l < heap_size && (int) A.get(l) > (int) A.get(r)) // TODO Fix casting
			largest = l;
		else largest = i;
		if (r <= heap_size && (int) A.get(r) > (int) A.get(largest)) // TODO Fix casting
			largest = r;
		if (largest != i) {
			T temp = A.get(i);
			A.set(i, A.get(largest));
			A.set(largest, temp);
			maxHeapify(largest);
		}
	}
	
	private int left(int i) {
		return 2 * i;
	}
	
	private int right(int i) {
		return 2 * i + 1;
	}
	
	private int parent(int i) {
		return (int) Math.floor(i / 2);
	}

//	@Override
//	private int compareTo(T item) {
//		if (item instanceof Integer) {
//			if (this < item) {
//				
//			}
//		}
//		return 0;
//	}
}
