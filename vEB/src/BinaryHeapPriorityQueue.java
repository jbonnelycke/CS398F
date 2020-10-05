/*************************************************************************
 *
 *  Pace University
 *  Fall 2020
 *  Advanced Data Structures and Computational Geometry
 *
 *  Course: CS 398F
 *  Author: Jack Bonnelycke
 *  Collaborators: none
 *  References: Introduction to Algorithms by Cormen et al.
 *
 *  Assignment: #1
 *  Problem: Compare the run times of Priority Queues implemented with Binary Heaps and Van Emde Boas Trees for
 *  		 construction and m operations on both.
 *  Description: In this assignment, I implemented two types of priority queue, one with a binary heap and one with
 *  			 a Van Emde Boas tree. In both I implemented the constructor and primary functions: insert(), 
 *  			 extractMax(), and increaseKey(). Once these were both implemented, I hypothesized construction and
 *  			 m operation running times, tested it with n insertions and m operations, and lastly analyzed the
 *  			 results.
 *
 *  Input: Universe size u, items to insert n, and m operations.
 *  Output: The run times for construction and n insertions on both data structures and for the same m operations on
 *  		both data structures.
 *
 *  Visible data fields:
 *  none
 *
 *  Visible methods:
 *  public BinaryHeapPriorityQueue() {
 *  public BinaryHeapPriorityQueue(Integer[] items) throws Exception {
 *  public void insert(int value, int priority) throws Exception {
 *  public Node ExtractMax() throws Exception {
 *  public void increaseKeyRandom(int priority, int value) throws Exception {
 *
 *
 *   Remarks
 *   -------
 *
 *   1. Insertion is O(lgn) so inserting n times is O(nlgn).
 *   2. All three operations are O(lgn) in the binary heap implementation of priority queue. If we do m of these
 *   	operations then that's O(mlgn), which then amortized over those m operations is O(lgn)
 *   3. Insertion is O(lglgu), so n times will be O(nlglgu).
 *   4. As above, all three operations are the same, O(lglgu), so m times is O(mlglgu) amortized over the m 
 *   	operations is O(lglgu).
 *   
 *   	(for these first two tables, u = 1048576, while in the third I played around with having a tight u)
 *   
 *   6. construction time |  n = 10^2  |  n = 10^3  |  n = 10^4  |  n = 10^5  | n = 10^6
 *        binary heap     |  5088400   |  6052200   |  9179200   |  21180600  | 57974200
 *         vEB tree       |  198816700 |  183842600 |  217866500 |  362887000 | 2334545300
 *
 *      m >= n operations |  n = 10^2  |  n = 10^3  |  n = 10^4  |  n = 10^5  | n = 10^6
 *        binary heap     |   507500   |  1454100   |  7303100   |  101359000 | 6046404900
 *         vEB tree       |  140452600 |  13024100  |  14027100  |  148561300 | 1203503900
 *         
 *      tight-bound u     |  n = 10^2  |  n = 10^3  |  n = 10^4  |  n = 10^5  | n = 10^6
 *      				  |  u = 256   |  u = 1024	|  u = 16384 | u = 262144 | u = 1048576
 *      construction	  |  3827800   |  5675300   |  18420700  |  177800000 | 981366200
 *      m >= n operations |  1814300   |  2570600   |  15440500  |  130834300 | 1127774900
 *      
 *   7. Binary heap construction: The run times observed make sense. This is because we have
 *   	lgn which is pretty close to constant, times n. So mostly in the result we will see
 *      the effects of n. And looking at the numbers, it generally does that. Not quite though,
 *      and I think the reason is that O(lgn) for insertions is a pretty loose bound since 
 *      half the items are at the bottom of the "tree" that a binary heap uses. A pretty close
 *      relation can be seen at n = 10^4 to n = 10^5 which hops from 9179200 to 21180600. I
 *      should also note there could be unexpected overhead from how I implemented both of 
 *      the data structures to keep track of priority and value, as well as achieving random
 *      order.
 *      
 *      VEB construction: In this construction, the run times make sense again. The lglgu
 *      isn't really seen, and we just get the effects of n. This is seen best between 
 *      n = 10^5 and n = 10^6 when the number of digits increases by one from 362887000 to
 *      2334545300. While not part of the big-O, it doesn't help adding the base u time from
 *      making the empty tree before adding anything in.
 *      
 *      Binary Heap operations: Again, we see mostly linear run times. From n = 10^5 to 
 *      n = 10^6 we see a good example, with an extra digit added when going from 101359000
 *      to 6036404900.
 *      
 *      VEB operations: From 10^4 to 10^5 for n, we see linear run times in the numbers
 *      18420700 and 177800000. 
 *      
 *      I know question 7 is not asking our opinion, but I just found it fascinating that
 *      such an exciting data structure didn't perform very well in practice. I am certain
 *      there is a lot of overhead going on, but even when I have a tighter-bound u, it
 *      barely performs better than the binary heap. The most noticeable different is at
 *      n = 10^6, so maybe it just needs a larger number of operations to really feel the
 *      difference.
 *
 *************************************************************************/

import java.util.ArrayList;
import java.util.Random;

public class BinaryHeapPriorityQueue /* implements Comparable<T> */ {

	private int heap_size;
	private ArrayList<Node> A;
	private Random rand;

	/**
	 * Default constructor. Just initializes values.
	 */
	public BinaryHeapPriorityQueue() {
		heap_size = 0;
		A = new ArrayList<Node>();
		rand = new Random();
	}

	/**
	 * Constructor that accepts an array of items to insert and then inserts
	 * all of them.
	 * @param items is the list of items to insert into the priority queue
	 */
	public BinaryHeapPriorityQueue(Integer[] items) throws Exception {
		this();
		A.add(0, null);
		// goes through the list adding items
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null) { // checks to make sure that the item to insert actually exists
				insert(i, items[i]);
			}
		}
	}

	/**
	 * Inserts an item into the priority queue.
	 * @param value
	 * @param priority
	 * @throws Exception
	 */
	public void insert(int value, int priority) throws Exception {
		Node temp = new Node(Integer.MIN_VALUE, value);
		heap_size = heap_size + 1;
		A.add(heap_size, temp);
		increaseKey(heap_size, priority);
	}

	/**
	 * Extracts the largest value in the priority queue
	 * @return the item that was just extracted
	 * @throws Exception thrown when the heap is too small
	 */
	public Node ExtractMax() throws Exception {
		// checks to make sure that the heap hasn't gotten too small
		if (heap_size < 1)
			throw new Exception("heap underflow");
		Node max = A.get(1);
		A.set(1, A.get(heap_size));
		heap_size = heap_size - 1;
		maxHeapify(1);
		return max;
	}

	/**
	 * Increase the item at index i's priority to the passed value
	 * @param i is the index of the item to update
	 * @param priority
	 * @throws Exception is thrown when you decrease the value instead
	 */
	private void increaseKey(int i, int priority) throws Exception {
		if (priority < A.get(i).getPriority())
			throw new Exception("new key is smaller than current key");
		A.get(i).setPriority(priority);
		while (i > 1 && (A.get(parent(i)).getPriority() < A.get(i).getPriority())) {
			Node temp = A.get(i);
			A.set(i, A.get(parent(i)));
			A.set(parent(i), temp);

			i = parent(i);
		}
	}
	
	/**
	 * An edited version of increase key just to randomly pick an item to
	 * increase for the sake of testing. 
	 * @param priority
	 * @param value
	 * @throws Exception is thrown when you decrease the value instead
	 */
	public void increaseKeyRandom(int priority, int value) throws Exception {
		int i = rand.nextInt(heap_size) + 1;
		if (priority < A.get(i).getPriority())
			throw new Exception("new key is smaller than current key");
		A.get(i).setPriority(priority);
		A.get(i).setValue(value);
		while (i > 1 && (A.get(parent(i)).getPriority() < A.get(i).getPriority())) {
			Node temp = A.get(i);
			A.set(i, A.get(parent(i)));
			A.set(parent(i), temp);

			i = parent(i);

		}
	}

	/**
	 * Adjusts the binary heap to have a proper structure such that the max is on the
	 * top. 
	 * @param i the item around which it is "max heaped"
	 */
	private void maxHeapify(int i) {
		int largest;
		int l = left(i);
		int r = right(i);
		// the below two if's and else check of the root and children, which is 
		// the largest to be max heap'ed
		if (l <= heap_size && A.get(l).getPriority() > A.get(r).getPriority())
			largest = l;
		else
			largest = i;
		if (r <= heap_size && A.get(r).getPriority() > A.get(largest).getPriority())
			largest = r;
		// if the root isn't already the max, swap the nodes around so that the max 
		// is the root
		if (largest != i) {
			Node temp = A.get(i);
			A.set(i, A.get(largest));
			A.set(largest, temp);
			maxHeapify(largest);
		}
	}

	/**
	 * @param i 
	 * @return i's left child
	 */
	private int left(int i) {
		return 2 * i;
	}

	/**
	 * @param i 
	 * @return i's right child
	 */
	private int right(int i) {
		return 2 * i + 1;
	}

	/**
	 * @param i 
	 * @return i's parent
	 */
	private int parent(int i) {
		return (int) Math.floor(i / 2);
	}

	/**
	 * @return the size of the heap
	 */
	public int getSize() {
		return heap_size;
	}

	/**
	 * Prints the heap in array order. Just for testing.
	 */
	public void printArray() {
		for (int i = 1; i < A.size(); i++) {
			System.out.print(A.get(i).toString() + ", ");
		}
	}
}
