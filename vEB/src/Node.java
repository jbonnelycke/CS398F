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
 *  Node(int priority, int value) {
 *	public int getPriority() {
 *	public int getValue() {
 *	public void setPriority(int newPriority) {
 *	public void setValue(int newValue) {
 *	public String toString() {
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


public class Node {
	private int priority;
	private int value;

	/**
	 * Default constructor that sets a new node with priority and value
	 * @param priority
	 * @param value
	 */
	Node(int priority, int value) {
		this.priority = priority;
		this.value = value;
	}

	// Standard getters and setters
	
	public int getPriority() {
		return priority;
	}

	public int getValue() {
		return value;
	}

	public void setPriority(int newPriority) {
		priority = newPriority;
	}

	public void setValue(int newValue) {
		value = newValue;
	}
	
	/**
	 * toString for the Node object
	 */
	@Override
	public String toString() {
		return "<" + priority + ", " + value + ">";
	}
}